/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.service;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.SocketException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPOutputStream;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import jp.co.arkinfosys.common.CategoryTrns;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.common.StringUtil;
import jp.co.arkinfosys.dto.DomainDto;
import jp.co.arkinfosys.dto.UserDto;
import jp.co.arkinfosys.dto.setting.MineDto;
import jp.co.arkinfosys.s2extend.NumberConverter;
import jp.co.arkinfosys.service.exception.ServiceException;
import jp.co.arkinfosys.service.exception.UnabledLockException;

import org.seasar.extension.jdbc.service.S2AbstractService;
import org.seasar.framework.beans.util.BeanMap;
import org.seasar.framework.exception.SQLRuntimeException;
import org.seasar.struts.util.MessageResourcesUtil;

/**
 * サービスの基底クラスです.
 * @author Ark Information Systems
 *
 * @param <ENTITY>
 */
public abstract class AbstractService<ENTITY> extends S2AbstractService<ENTITY> {

	/**
	 * パラメータマッピングクラスです.
	 * @author Ark Information Systems
	 *
	 */
	public static class Param {
		public static final String CRE_FUNC = "creFunc";

		public static final String CRE_DATETM = "creDatetm";

		public static final String CRE_USER = "creUser";

		public static final String UPD_FUNC = "updFunc";

		public static final String UPD_DATETM = "updDatetm";

		public static final String UPD_USER = "updUser";

		public static final String DEL_FUNC = "delFunc";

		public static final String DEL_DATETM = "delDatetm";

		public static final String DEL_USER = "delUser";

		public static final String ACTION_FUNC = "actionFunc";

		public static final String LOCK_RECORD = "lockRecord";
	}

	public static final String FOR_UPDATE = "FOR UPDATE";

	/**
	 * アクションタイプを定義するクラスです.
	 * @author Ark Information Systems
	 *
	 */
	public static class ActionType {
		/** INSERT */
		public static final String INSERT = "INSERT";

		/** UPDATE */
		public static final String UPDATE = "UPDATE";

		/** DELETE */
		public static final String DELETE = "DELETE";
	}

	/**
	 * ロック結果を定義するクラスです.
	 * @author Ark Information Systems
	 *
	 */
	public static class LockResult {
		/** 正常終了 */
		public static final int SUCCEEDED = 1;

		/** 対象が存在しない */
		public static final int NOT_EXISTS = 0;

		/** ロックされている */
		public static final int ALREADY_LOCKED = -1;

		/** 更新不可（更新されている） */
		public static final int ALREADY_UPDATED = -2;
	}

	@Resource
	protected HttpSession httpSession;

	@Resource
	protected DomainDto domainDto;

	@Resource
	protected UserDto userDto;

	@Resource
	protected MineDto mineDto;

	/**
	 * SQLファイルの格納パスをクラスパッケージツリーに変換します.
	 * @param entityClass エンティティクラス
	 * @see org.seasar.extension.jdbc.service.S2AbstractService#setEntityClass(java.lang.Class)
	 */
	protected void setEntityClass(Class<ENTITY> entityClass) {
		this.entityClass = entityClass;
		sqlFilePathPrefix = "jp/co/arkinfosys/entity/sql/";
	}

	/**
	 * 共通のSQLパラメータを作成して返します.
	 * @return パラメータマップ
	 */
	protected Map<String, Object> createSqlParam() {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put(DomainService.Param.DOMAIN_ID, this.domainDto.domainId);
		param.put(AbstractService.Param.CRE_FUNC, this.userDto.lastRequestFunc);
		param.put(AbstractService.Param.CRE_USER, this.userDto.userId);
		param.put(AbstractService.Param.UPD_FUNC, this.userDto.lastRequestFunc);
		param.put(AbstractService.Param.UPD_USER, this.userDto.userId);
		param.put(AbstractService.Param.ACTION_FUNC,
				this.userDto.lastRequestFunc);
		return param;
	}

	/**
	 * 指定された文字列の前後空白を除去し、前方一致検索文字列を作成します.
	 * @param str 加工する文字列
	 * @return 前方一致検索用に加工した文字列
	 */
	protected String createPrefixSearchCondition(String str) {
		if (str == null) {
			return null;
		}
		return StringUtil.trimBlank(str) + "%";
	}

	/**
	 * 指定された文字列の前後空白を除去し、部分検索文字列を作成します.
	 * @param str 加工する文字列
	 * @return 部分検索用に加工した文字列
	 */
	protected String createPartialSearchCondition(String str) {
		if (str == null) {
			return null;
		}
		return "%" + StringUtil.trimBlank(str) + "%";
	}

	/**
	 * 指定されたSQLファイルを使用してレコードをロックします.<br>
	 * ロックに失敗した場合は、トランザクションを終了してください.
	 * @param sql カラム定義にUPD_DATETM、UPD_USERを含むSQLファイル名
	 * @param param 使用するパラメータマップ
	 * @param dtm ロック可否判断に使用する日時
	 * @return {@link LockResult#SUCCEEDED}
	 * @throws UnabledLockException
	 * @throws ServiceException
	 */
	protected int lockRecordBySqlFile(String sql, Map<String, Object> param,
			Timestamp dtm) throws UnabledLockException, ServiceException {
		// 対象レコードを検索
		BeanMap result = null;
		UnabledLockException exResult = null;
		try {
			result = this.selectBySqlFile(BeanMap.class, sql, param)
					.getSingleResult();
		} catch (SQLRuntimeException e) {
			// ここでつかまった場合はSQLエラーかタイムアウト
			// 区別をするのはかな～りアヤシイになるのでタイムアウトで返しちゃいます。
			exResult = new UnabledLockException(
					"errors.exclusive.control.locked");
			exResult.setLockStatus(AbstractService.LockResult.ALREADY_LOCKED);
			throw exResult;
		} catch (Exception e) {
			throw new ServiceException(e);
		}

		// レコードが取得できなかった
		if (result == null) {
			exResult = new UnabledLockException(
					"errors.exclusive.control.deleted");
			exResult.setLockStatus(AbstractService.LockResult.NOT_EXISTS);
			throw exResult;
		}

		// 更新時間取得
		// nullチェックはしない！ あったら不具合で「ぬるぽ」が投げられます！
		try {
			Object updDtm = result.get(AbstractService.Param.UPD_DATETM);
			String updUser = (String) result
					.get(AbstractService.Param.UPD_USER);
			Timestamp tm = null;
			if (updDtm instanceof Timestamp) {
				tm = (Timestamp) updDtm;
			} else if (updDtm instanceof String) {
				tm = new Timestamp(new SimpleDateFormat(
						Constants.FORMAT.TIMESTAMP).parse((String) updDtm)
						.getTime());
			}

			// 更新時間判定
			if (tm != null && tm.compareTo(dtm) == 0) {
				return AbstractService.LockResult.SUCCEEDED;
			}
			// 既に更新済み
			exResult = new UnabledLockException(
					"errors.exclusive.control.updated");
			exResult.setLockStatus(AbstractService.LockResult.ALREADY_UPDATED);
			exResult.setSelfTm(dtm);
			exResult.setTargetTm(tm);
			exResult.setTargetUpdUserId(updUser);
			throw exResult;
		} catch (UnabledLockException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 指定されたSQLファイルを使用してレコードをロックします.<br>
	 * ロックに失敗した場合は、トランザクションを終了してください.
	 * @param sql カラム定義にUPD_DATETM、UPD_USERを含むSQLファイル名
	 * @param param 使用するパラメータマップ
	 * @param dtm ロック可否判断に使用する日時の文字列
	 * @return ロック結果
	 * @throws UnabledLockException
	 * @throws ServiceException
	 */
	protected int lockRecordBySqlFile(String sql, Map<String, Object> param,
			String dtm) throws UnabledLockException, ServiceException {
		try {
			// String→Timestamp変換＆判定
			Timestamp tm = new Timestamp(new SimpleDateFormat(
					Constants.FORMAT.TIMESTAMP).parse(dtm).getTime());
			return this.lockRecordBySqlFile(sql, param, tm);
		} catch (ParseException e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 指定されたSQLファイルを使用して検索を行います.
	 * @param conditions 検索条件のマップ
	 * @param params 使用するパラメータ名の配列
	 * @param sqlFileName SQLファイル名
	 * @return 検索結果のエンティティのリスト
	 * @throws ServiceException
	 */
	protected List<ENTITY> findByCondition(Map<String, Object> conditions,
			String[] params, String sqlFileName) throws ServiceException {

		// 検索条件を指定しないものも許可するのでSQLで調整してください
		// if (conditions == null) {
		// return new ArrayList<Bill>();
		// }
		try {
			Map<String, Object> param = createSqlParam();
			this.setEmptyCondition(param, params);

			for (String paramName : params) {
				if (conditions.containsKey(paramName)) {
					param.put(paramName, conditions.get(paramName));
				}
			}

			return this.selectBySqlFile(this.entityClass, sqlFileName, param)
					.getResultList();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 値が空の検索パラメータのマップを作成して返します.
	 * @param param パラメータのマップ
	 * @param params 使用するパラメータ名の配列
	 * @return 値が空の検索パラメータのマップ
	 */
	private Map<String, Object> setEmptyCondition(Map<String, Object> param,
			String[] params) {

		// 検索条件
		for (String paramName : params) {
			param.put(paramName, null);
		}
		return param;
	}

	/**
	 * 単価端数処理用のコンバータを作成して返します.
	 * @param fract 得意先マスタまたは伝票に指定されている端数処理コード
	 * @return コンバータ
	 */
	public NumberConverter createUnitPriceConverter(String fract) {
		return new NumberConverter(fract, this.mineDto.unitPriceDecAlignment,
				true);
	}

	/**
	 * 税端数処理用のコンバータを作成して返します.
	 * @param fract 得意先マスタまたは伝票に指定されている端数処理コード
	 * @return コンバータ
	 */
	public NumberConverter createTaxPriceConverter(String fract) {
		return new NumberConverter(fract, this.mineDto.unitPriceDecAlignment,
				true);
	}

	/**
	 * 税率処理用のコンバータを作成して返します.<br>
	 * 税率は小数第2位まで指定されるため、小数第3位を四捨五入します.
	 * @return コンバータ
	 */
	public NumberConverter createTaxRateConverter() {
		return new NumberConverter(CategoryTrns.FLACT_CATEGORY_HALF_UP, 2,
				false);
	}

	/**
	 * 数量端数処理用のコンバータを作成して返します.
	 * @return コンバータ
	 */
	public NumberConverter createProductNumConverter() {
		return new NumberConverter(this.mineDto.productFractCategory,
				this.mineDto.numDecAlignment, true);
	}

	/**
	 * 統計値処理用のコンバータを作成して返します.<br>
	 * %表示を行う箇所で使用します.
	 * @return コンバータ
	 */
	public NumberConverter createStatusPriceConverter() {
		return new NumberConverter(CategoryTrns.FLACT_CATEGORY_HALF_UP,
				this.mineDto.statsDecAlignment, true);
	}

	/**
	 * {@link Date java.util.Date}オブジェクトを{@link java.sql.Date}オブジェクトに変換します.
	 * @param utilDate {@link Date java.util.Date}オブジェクト
	 * @return {@link java.sql.Date}オブジェクト
	 */
	public java.sql.Date convertUtilDateToSqlDate(java.util.Date utilDate) {
		if (utilDate == null) {
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(utilDate);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return new java.sql.Date(cal.getTimeInMillis());
	}

	/**
	 * データベース操作で問題が発生した場合のメッセージを返します.
	 * @param TableType テーブルタイプ
	 * @param updType 編集タイプ
	 * @param etc その他メッセージ設定文字列
	 * @return エラーメッセージ文字列
	 */
	protected String getDbMessage(String TableType, String updType, String etc) {
		String strSystem = MessageResourcesUtil.getMessage("errors.system");
		String strBase = MessageResourcesUtil.getMessage("errors.db");
		String strTable = MessageResourcesUtil.getMessage(TableType);
		String strType = MessageResourcesUtil.getMessage(updType);
		strBase = strBase.replace("{0}", strTable);
		strBase = strBase.replace("{1}", strType);
		strBase = strBase.replace("{2}", etc);
		return strSystem.replace("{0}", strBase);
	}

	/**
	 * Java変数名をデータベースカラム名に変換します.
	 * @param variableName Java変数名
	 * @return データベースカラム名
	 */
	public String convertVariableNameToColumnName(String variableName) {
		if (variableName == null) {
			return null;
		}

		// 小文字の英字と、大文字の英字または数字が並んでいる箇所を検索する
		Pattern pattern = Pattern.compile("([a-z])([A-Z0-9])");
		Matcher matcher = pattern.matcher(variableName);

		StringBuffer buf = new StringBuffer();
		while (matcher.find()) {
			String c1 = matcher.group(1);
			String c2 = matcher.group(2);

			// マッチした箇所にアンダーバーを挿入する
			matcher.appendReplacement(buf, c1 + "_" + c2);
		}
		matcher.appendTail(buf);

		return buf.toString().toUpperCase();
	}

	/**
	 * 削除情報を更新します.
	 * @param tableName テーブル名
	 * @param keyNames 削除キーとなるカラム名またはエンティティフィールド名の配列
	 * @param keyValues 削除キー値の配列
	 */
	public void updateAudit(String tableName, String[] keyNames, Object[] keyValues) {
		Map<String, Object> param = createSqlParam();
		param.put(Param.UPD_USER, "NOLOG");
		param.put(Param.DEL_FUNC, this.userDto.lastRequestFunc);
		param.put(Param.DEL_USER, this.userDto.userId);
		param.put("tableName", tableName + "_" + this.domainDto.domainId);

		for(int i = 0; i < keyNames.length && i < keyValues.length; i++) {
			param.put("columnName" + (i+1), this.convertVariableNameToColumnName(keyNames[i]));
			param.put("keyValue" + (i+1), keyValues[i]);
		}

		this.updateBySqlFile("UpdateAudit.sql", param).execute();
	}

	/**
	 * ファイル転送時のバッファサイズ
	 */
	protected static final int BUFF_SIZE = 1024 * 8;

	/**
	 * エンコーディングタイプgzip
	 */
	protected static final String GZIP = "gzip";

	/**
	 * HTTPヘッダ定義クラスです.
	 * @author Ark Information Systems
	 *
	 */
	protected static class HTTP_HEADER {
		public static final String CONTENT_DISPOSITION = "Content-disposition";

		public static final String CONTENT_ENCODING = "Content-Encoding";

		public static final String ACCEPT_ENCODING = "accept-encoding";
	}

	/**
	 * ファイルをダウンロードします.
	 * @param filePath ダウンロードするファイルパス
	 * @param downloadFileName ダウンロードファイル名
	 * @param httpRequest {@link HttpServletRequest}
	 * @param httpResponse {@link HttpServletResponse}
	 * @throws ServiceException
	 */
	public void downloadFile(String filePath, String downloadFileName,
			HttpServletRequest httpRequest, HttpServletResponse httpResponse)
			throws ServiceException {
		try {
			File file = new File(filePath);
			if (!file.exists() || !file.canRead()) {
				return;
			}

			BufferedInputStream is = null;
			OutputStream os = null;
			try {
				is = new BufferedInputStream(new FileInputStream(file));
				os = this.createOutputStream(downloadFileName, httpRequest,
						httpResponse);
				this.transferIO(is, os);
			} finally {
				is.close();
			}
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * ダウンロードファイルの{@link OutputStream}を返します.
	 * @param fileName ファイル名
	 * @param httpRequest {@link HttpServletRequest}
	 * @param httpResponse {@link HttpServletResponse}
	 * @return ダウンロードファイルの{@link OutputStream}
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	protected OutputStream createOutputStream(String fileName,
			HttpServletRequest httpRequest, HttpServletResponse httpResponse)
			throws UnsupportedEncodingException, IOException {

		String acceptEncoding = httpRequest
				.getHeader(FileDownloadService.HTTP_HEADER.ACCEPT_ENCODING);
		boolean deflate = false;
		if (StringUtil.hasLength(acceptEncoding)
				&& acceptEncoding.contains(FileDownloadService.GZIP)) {
			// 圧縮可能であれば圧縮する
			httpResponse.setHeader(
					FileDownloadService.HTTP_HEADER.CONTENT_ENCODING,
					FileDownloadService.GZIP);
			deflate = true;
		}

		// Content-Type設定
		httpResponse.setContentType(Constants.MIME.BIN);
		// Content-Disposition設定
		httpResponse.setHeader(
				FileDownloadService.HTTP_HEADER.CONTENT_DISPOSITION,
				"attachment; filename=\""
						+ new String(fileName
								.getBytes(Constants.CHARSET.WINDOWS31J),
								Constants.CHARSET.LATIN1) + "\"");

		// OutputStreamを返す
		if (deflate) {
			return new GZIPOutputStream(new BufferedOutputStream(httpResponse
					.getOutputStream()));
		}
		return new BufferedOutputStream(httpResponse.getOutputStream());
	}

	/**
	 * InputStreamからOutputStreamへのデータ転送を行います.
	 * @param is {@link InputStream}
	 * @param os {@link OutputStream}
	 * @throws IOException
	 */
	protected void transferIO(InputStream is, OutputStream os)
			throws IOException {
		if (is == null || os == null) {
			return;
		}
		try {
			byte[] buffer = new byte[FileDownloadService.BUFF_SIZE];
			int len = 0;
			while ((len = is.read(buffer, 0, buffer.length)) != -1) {
				os.write(buffer, 0, len);
			}

			os.flush();
			if (os instanceof GZIPOutputStream) {
				((GZIPOutputStream) os).finish();
			}

		} catch (SocketException e) {
			if (!e.getMessage().contains("ClientAbortException")) {
				// ClientAbortExceptionはユーザキャンセルなので正常と判断
				throw e;
			}
		} finally {
			is.close();
			os.close();
		}
	}
}