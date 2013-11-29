/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;

import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.common.StringUtil;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.upload.FormFile;
import org.mozilla.universalchardet.UniversalDetector;

/**
 * CSV/TSV取り扱い基底アクションクラスです.
 * @author Ark Information Systems
 *
 */
public abstract class AbstractXSVUploadAction extends CommonResources {

	/**
	 * セパレータ定義クラスです.
	 * @author Ark Information Systems
	 *
	 */
	public static final class SEPARATOR {
		public static final String COMMA = ",";

		public static final String TAB = "\t";
	}

	public static final String LINE_SEPARATOR = "\r\n";	// parasoft-suppress PB.CUB.FLVA "意図されたコード"

	protected boolean isReadStop = false;

	/**
	 * 読み込みに使用する文字コード
	 */
	private String characterSet = null;

	/**
	 * CSV/TSVファイルを読み込みます.
	 * @param file 読み込むファイル
	 * @param charset ファイルの文字コード
	 * @throws Exception
	 */
	public void readXSV(FormFile file, String charset) throws Exception {
		readXSV(file.getInputStream(), charset);
	}

	/**
	 * CSV/TSVファイルを読み込みます.
	 * @param file 読み込むファイル
	 * @throws Exception
	 */
	public void readXSV(FormFile file) throws Exception {
		readXSV(file.getInputStream());
	}

	/**
	 * CSV/TSVファイルを読み込みます.
	 * @param is 読み込みファイルの{@link InputStream}
	 * @throws Exception
	 */
	public void readXSV(InputStream is) throws Exception {
		readXSV(is, Constants.CHARSET.SHIFT_JIS);
	}

	/**
	 * CSV/TSVファイルを読み込みます.
	 * @param is 読み込みファイルの{@link InputStream}
	 * @param charset 文字コード
	 * @throws Exception
	 */
	public void readXSV(InputStream is, String charset) throws Exception {
		BufferedInputStream bs = null;
		LineNumberReader lnr = null;
		try {
			bs = new BufferedInputStream(is);
			bs.mark(1024 * 8);

			String fileCharSet = this.getCharacterSet();
			if (!StringUtil.hasLength(fileCharSet)) {
				// 文字コードが指定されていない場合、先頭8kで判別する
				UniversalDetector detector = new UniversalDetector(null);
				byte[] buff = new byte[1024];
				int size = 0;
				int count = 0;
				while (8 > count && (size = bs.read(buff, 0, buff.length)) > 0
						&& !detector.isDone()) {
					detector.handleData(buff, 0, size);
					count++;
				}
				detector.dataEnd();

				// 全く読込めなかった！
				if (count == 0) {
					// 無理矢理１行チェックを実施
					processLine(1, "", new String[0]);
				}

				fileCharSet = detector.getDetectedCharset();
				if (fileCharSet == null) {
					fileCharSet = Constants.CHARSET.SHIFT_JIS;
				}
			}

			bs.reset();
			lnr = new LineNumberReader(new InputStreamReader(bs, fileCharSet));
			String line = null;
			while (true) {
				line = lnr.readLine();

				if (line == null) {
					break;
				}

				//空の行をとばす
				if (line.length()==0){
					continue;
				}

				// 改行対応（ダブルクォーテーションが偶数個になるまで追加）
				int np = this.countChar(line,"\"");
				while (np % 2 != 0) {
					String lineEx = lnr.readLine();
					if (lineEx == null) {
						throw new Exception("Bad format XSV!");
					}
					int nn = this.countChar(lineEx,"\"");
					line = line + LINE_SEPARATOR + lineEx;
					np += nn;
				}

				// 区切り文字でカットする
				String[] values = line.split(this.getSeparator(), -1);

				// ダブルクォーテーション内に区切り文字がある場合の対応（ダブルクォーテーションが偶数個になるまで結合）
				List<String> valList = new ArrayList<String>();
				for (int i=0;i<values.length;i++) {
					String val = values[i];
					np = this.countChar(val, "\"");
					while (np%2!=0) {
						if (i>=values.length) {
							throw new Exception("Bad format XSV!");
						}
						i++;  // parasoft-suppress PB.CUB.FLVA "意図されたコード"
						int nn = this.countChar(values[i], "\"");
						val = val + this.getSeparator() + values[i];
						np += nn;
					}
					valList.add(val);
				}

				values = (String[])valList.toArray(new String[0]);
				for (int i = 0; i < values.length; i++) {
					values[i] = StringUtil.removeQuote(values[i]);
				}

				processLine(lnr.getLineNumber(), line, values);

				if( this.isReadStop ){
					break;
				}
			}
		} catch (Exception e) {
			super.errorLog(e);
			throw e;
		} finally {
			if (bs != null) {
				bs.close();
			}
			if (lnr != null) {
				lnr.close();
			}
		}
	}

	/**
	 * 文字列内に存在する指定文字の数を返します.
	 * @param src 検索対象の文字列
	 * @param ch 指定文字
	 * @return 指定文字の登場回数
	 */
	private int countChar(String src, String ch) {
		int ret = 0;
		int idx = 0;
		while (true) {
			idx = src.indexOf(ch, idx);
			if (idx < 0) {
				break;
			}
			idx+=ch.length();
			ret++;
		}
		return ret;
	}

	/**
	 * エラー内容の{@link ActionMessage}を生成し、メッセージリストに追加します.<br>
	 * メッセージリストには、最大10行まで追加可能です.
	 * @param key プロパティファイルのキー
	 * @param values メッセージに埋め込む文字列の配列
	 * @return メッセージが追加できたか否か
	 */
	protected boolean addError(String key, Object[] values) {
		return addError(new ActionMessage(key, values));
	}

	/**
	 * エラー内容の{@link ActionMessage}を生成し、メッセージリストに追加します.<br>
	 * メッセージリストには、最大10行まで追加可能です.
	 * @param error エラー内容を設定した{@link ActionMessage}
	 * @return メッセージが追加できたか否か
	 */
	protected boolean addError(ActionMessage error) {
		if(isErrorsMax()) {
			return false;
		}
		messages.add(ActionMessages.GLOBAL_MESSAGE, error);
		return true;
	}

	/**
	 * メッセージリストに設定済みの{@link ActionMessage}が最大値を超えているかを返します.
	 * @return 最大値を超えているか否か
	 */
	protected boolean isErrorsMax() {
		if(messages == null) {
			messages = new ActionMessages();
		}
		if(messages.size() >= 10) {
			return true;
		}
		return false;
	}

	/**
	 * 区切り文字を返します.
	 * @return 区切り文字
	 */
	protected abstract String getSeparator();

	/**
	 * CSV/TSVファイルの1行を処理します.
	 * @param index 行番号
	 * @param line 1行分の文字列
	 * @param values セパレータで分割した値の配列
	 * @throws Exception
	 */
	protected abstract void processLine(int index, String line, String[] values)
			throws Exception;

	/**
	 * 文字コードを返します.
	 * @return 文字コード
	 */
	protected String getCharacterSet() {
		return this.characterSet;
	}

	/**
	 * 文字コードを設定します.
	 * @param characterSet 文字コード
	 */
	protected void setCharacterSet(String characterSet) {
		this.characterSet = characterSet;
	}
}
