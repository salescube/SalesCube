/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.common.StringUtil;
import jp.co.arkinfosys.dto.deposit.DepositLineDto;
import jp.co.arkinfosys.dto.deposit.DepositSlipDto;
import jp.co.arkinfosys.entity.DepositLine;
import jp.co.arkinfosys.entity.DepositSlip;
import jp.co.arkinfosys.s2extend.NumberConverter;
import jp.co.arkinfosys.service.exception.ServiceException;

import org.seasar.framework.beans.util.BeanMap;
import org.seasar.framework.beans.util.Beans;

/**
 * 入金明細サービスクラスです.
 * @author Ark Information Systems
 *
 */
public class DepositLineService extends
		AbstractLineService<DepositLine, DepositLineDto, DepositSlipDto> {
	// 発番用サービス
	public SeqMakerService seqMakerService;

	public DepositLine depositLine = new DepositLine();

	/**
	 * パラメータマッピングクラスです.
	 * @author Ark Information Systems
	 *
	 */
	public static class Param {
		private static final String SORT_ORDER = "sortOrder"; // ソート方向
		private static final String ROW_COUNT = "rowCount"; // 取得件数
		private static final String OFFSET_ROW = "offsetRow"; // 取得件数
		public static final String DEPOSIT_LINE_ID = "depositLineId"; // 入金伝票行ID
		public static final String STATUS = "status"; // 状態フラグ
		public static final String DEPOSIT_SLIP_ID = "depositSlipId"; // 入金伝票番号
		public static final String DEPOSIT_LINE_NO = "lineNo"; // 入金伝票行番
		public static final String DEPOSIT_LINE_IDS = "depositLineIds";

		private static final String SORT_COLUMN_DEPOSIT_LINE = "sortColumnDepositLine"; // 行番号のソート条件
		public static final String SLIP_STATUS = "slipStatus"; // 伝票状態フラグ
		private static final String CUSTOMER_CODE = "customerCode"; // 顧客番号
		public static final String DEPOSIT_DATE = "depositDate"; // 入金日
		public static final String DEPOSIT_DATE_FROM = "depositDateFrom"; // 入金日(期間指定FROM)
		public static final String DEPOSIT_DATE_TO = "depositDateTo"; // 入金日(期間指定TO)
		private static final String SORT_COLUMN_SLIP_ID = "sortColumnSlipId"; // 伝票番号のソート条件
		private static final String BILL_CUTOFF_DATE = "billCutoffDate"; // 請求締日
		public static final String IS_CONTAIN_CLOSE_LEAK = "isContainCloseLeak";
		public static final String LEAK_CHECK_CUTOFF_DATE = "leakCheckCutoffDate";
		public static final String DEPOSIT_CATEGORY = "depositCategory";
		public static final String SALES_CUTOFF_DATE = "salesCutoffDate";

	}

	public String[] params = { Param.SORT_ORDER, Param.ROW_COUNT,
			Param.OFFSET_ROW, Param.DEPOSIT_LINE_ID, Param.STATUS,
			Param.DEPOSIT_SLIP_ID, Param.DEPOSIT_LINE_NO,
			Param.SORT_COLUMN_DEPOSIT_LINE, Param.SLIP_STATUS,
			Param.CUSTOMER_CODE, Param.DEPOSIT_DATE, Param.DEPOSIT_DATE_FROM,
			Param.DEPOSIT_DATE_TO, Param.SORT_COLUMN_SLIP_ID,
			Param.BILL_CUTOFF_DATE, Param.IS_CONTAIN_CLOSE_LEAK,
			Param.LEAK_CHECK_CUTOFF_DATE, Param.DEPOSIT_CATEGORY,
			Param.SALES_CUTOFF_DATE };

	/**
	 * 入金伝票行番のカラム名
	 */
	public static final String COLUMN_DEPOSIT_LINE_NO = "LINE_NO";
	public static final String COLUMN_DEPOSIT_SLIP_ID = "DEPOSIT_SLIP_ID";

	/**
	 * 明細行が入力されているか確認します.
	 * @param lineDto 入金明細行DTO
	 * @return 1項目でも値が設定されているか否か
	 * @throws ServiceException
	 */
	public boolean check(DepositLineDto lineDto) throws ServiceException {
		boolean bPrice = true;
		boolean bBank = true;
		boolean bRemarks = true;

		if (!StringUtil.hasLength(lineDto.price)) {
			bPrice = false;
		}
		if (!StringUtil.hasLength(lineDto.bankId)) {
			bBank = false;
		}
		if (!StringUtil.hasLength(lineDto.remarks)) {
			bRemarks = false;
		}
		// 空行は例外ではないがfalseを返す
		if ((bPrice == false) && (bBank == false) && (bRemarks == false)) {
			return false;
		}
		// 空行で無い時にはエラー
		if (!bPrice) {
			throw new ServiceException("errors.deposit.price");
		}

		return true;
	}

	/**
	 * 顧客コードを指定して、入金日が指定日以前の未請求締の入金伝票に紐付く明細行エンティティのリストを返します.
	 * @param customerCode 顧客コード
	 * @param closeDate 締処理日時の文字列
	 * @param depositCategory 入金区分
	 * @return 入金明細行エンティティのリスト
	 * @throws ServiceException
	 */
	public List<DepositLine> findOpenDepositLineByCustomerCode(
			String customerCode, String closeDate, String depositCategory)
			throws ServiceException {

		LinkedHashMap<String, Object> conditions = new LinkedHashMap<String, Object>();

		// 条件設定
		// 伝票番号が一致
		conditions.put(Param.STATUS, DepositLine.STATUS_INIT);
		conditions.put(Param.SLIP_STATUS, DepositSlip.STATUS_INIT);
		conditions.put(Param.CUSTOMER_CODE, customerCode);
		conditions.put(Param.DEPOSIT_DATE, closeDate);
		conditions.put(Param.DEPOSIT_CATEGORY, depositCategory);
		conditions.put(Param.SORT_COLUMN_SLIP_ID, COLUMN_DEPOSIT_SLIP_ID);
		conditions.put(Param.SORT_COLUMN_DEPOSIT_LINE, COLUMN_DEPOSIT_LINE_NO);
		conditions.put(Param.SORT_ORDER, Constants.SQL.ASC);

		return findByCondition(conditions, params,
				"deposit/FindOpenDepositLine.sql");

	}

	/**
	 * 顧客コードを指定して、入金日が指定日以前の未売掛締の入金伝票に紐付く明細行エンティティのリストを返します.
	 * @param customerCode 顧客コード
	 * @param closeDate 締処理日時の文字列
	 * @return 入金明細行エンティティのリスト
	 * @throws ServiceException
	 */
	public List<DepositLine> findArtOpenDepositLineByCustomerCode(
			String customerCode, String closeDate) throws ServiceException {

		LinkedHashMap<String, Object> conditions = new LinkedHashMap<String, Object>();

		// 条件設定
		// 伝票番号が一致
		conditions.put(Param.CUSTOMER_CODE, customerCode);
		conditions.put(Param.DEPOSIT_DATE, closeDate);
		conditions.put(Param.SALES_CUTOFF_DATE, null);
		conditions.put(Param.SORT_COLUMN_SLIP_ID, COLUMN_DEPOSIT_SLIP_ID);
		conditions.put(Param.SORT_COLUMN_DEPOSIT_LINE, COLUMN_DEPOSIT_LINE_NO);
		conditions.put(Param.SORT_ORDER, Constants.SQL.ASC);

		return findByCondition(conditions, params,
				"deposit/FindArtOpenDepositLine.sql");

	}

	/**
	 * 顧客コードを指定して、入金日が指定期間の入金伝票に紐付く明細行エンティティのリストを返します.
	 * @param customerCode 顧客コード
	 * @param fromDate 期間指定開始日時の文字列
	 * @param toDate 機関指定終了日時の文字列
	 * @param salesCutoffDate 売掛締日の文字列
	 * @return 明細行エンティティのリスト
	 * @throws ServiceException
	 */
	public List<DepositLine> findDepositLineByCustomerCodeBetweenDate(
			String customerCode, String fromDate, String toDate,
			String salesCutoffDate) throws ServiceException {

		LinkedHashMap<String, Object> conditions = new LinkedHashMap<String, Object>();

		// 条件設定
		// 伝票番号が一致
		conditions.put(Param.STATUS, DepositLine.STATUS_INIT);
		conditions.put(Param.SLIP_STATUS, DepositSlip.STATUS_INIT);
		conditions.put(Param.CUSTOMER_CODE, customerCode);
		conditions.put(Param.DEPOSIT_DATE_FROM, fromDate);
		conditions.put(Param.DEPOSIT_DATE_TO, toDate);
		conditions.put(Param.SORT_COLUMN_SLIP_ID, COLUMN_DEPOSIT_SLIP_ID);
		conditions.put(Param.SORT_COLUMN_DEPOSIT_LINE, COLUMN_DEPOSIT_LINE_NO);
		conditions.put(Param.SORT_ORDER, Constants.SQL.ASC);
		conditions.put(Param.SALES_CUTOFF_DATE, salesCutoffDate);

		return findByCondition(conditions, params,
				"deposit/FindDepositLineBetweenDate.sql");
	}

	/**
	 * 顧客コードを指定して、請求締日が指定日の請求締済の入金伝票に紐付く明細行エンティティのリストを返します.
	 * @param customerCode 顧客コード
	 * @param lastCutOffDate 請求締日
	 * @return 明細行エンティティのリスト
	 * @throws ServiceException
	 */
	public List<DepositLine> findCloseDepositLineByCustomerCode(
			String customerCode, String lastCutOffDate) throws ServiceException {

		LinkedHashMap<String, Object> conditions = new LinkedHashMap<String, Object>();

		// 条件設定
		// 伝票番号が一致
		conditions.put(Param.STATUS, DepositLine.STATUS_CLOSE);
		conditions.put(Param.SLIP_STATUS, DepositSlip.STATUS_CLOSE);
		conditions.put(Param.CUSTOMER_CODE, customerCode);
		conditions.put(Param.BILL_CUTOFF_DATE, lastCutOffDate);
		conditions.put(Param.SORT_COLUMN_SLIP_ID, COLUMN_DEPOSIT_SLIP_ID);
		conditions.put(Param.SORT_COLUMN_DEPOSIT_LINE, COLUMN_DEPOSIT_LINE_NO);
		conditions.put(Param.SORT_ORDER, Constants.SQL.ASC);

		return findByCondition(conditions, params,
				"deposit/FindOpenDepositLine.sql");

	}

	/**
	 * 明細IDを指定して、明細行エンティティのリストを返します.
	 * @param depositLineId 明細ID
	 * @return 明細行エンティティのリスト
	 * @throws ServiceException
	 */
	public List<DepositLine> findDepositLineByLineId(String depositLineId)
			throws ServiceException {

		LinkedHashMap<String, Object> conditions = new LinkedHashMap<String, Object>();

		// 条件設定
		// 伝票番号が一致
		conditions.put(Param.DEPOSIT_LINE_ID, depositLineId);

		return findByCondition(conditions, params,
				"deposit/FindDepositLine.sql");

	}

	/**
	 * DTOからエンティティへの変換を行います.
	 * @param fract 単価端数処理区分
	 * @param lineDto 明細行DTO
	 * @return 明細行エンティティ
	 */
	public DepositLine createAndCopy(String fract, DepositLineDto lineDto) {
		DepositLine dl = Beans
				.createAndCopy(DepositLine.class, lineDto)
				.dateConverter(Constants.FORMAT.DATE, "instDate")
				.dateConverter(Constants.FORMAT.TIMESTAMP, "creDatetm",
						"updDatetm").execute();
		return dl;
	}

	/**
	 * エンティティからDTOへの変換を行います.
	 * @param fract 単価端数処理区分
	 * @param dl 明細行エンティティ
	 * @return 明細行DTO
	 */
	public DepositLineDto createAndCopy(String fract, DepositLine dl) {
		DepositLineDto result = Beans.createAndCopy(DepositLineDto.class,dl)
				.dateConverter(Constants.FORMAT.DATE, "instDate")
					.dateConverter(Constants.FORMAT.TIMESTAMP, "creDatetm", "updDatetm")
						.execute();
		return result;
	}

	/**
	 * エンティティ内容をDTOにコピーします.
	 * @param fract 単価端数処理区分
	 * @param dl 明細行エンティティ
	 * @param lineDto 明細行DTO
	 */
	public void copy(String fract, DepositLine dl, DepositLineDto lineDto) {
		Beans.copy(dl, lineDto)
				.dateConverter(Constants.FORMAT.DATE, "instDate")
				.dateConverter(Constants.FORMAT.TIMESTAMP, "creDatetm",
						"updDatetm").execute();
	}

	/**
	 * 明細行の請求締状態を解除します.
	 * @param dl 明細行エンティティ
	 */
	public void setReOpenDepositLine(DepositLine dl) {
		// 状態
		dl.status = DepositLine.STATUS_INIT;
	}

	/**
	 *
	 * @param dto {@link DepositSlipDto}
	 * @return {@link DepositLineDto}のリスト
	 * @throws ServiceException
	 * @see jp.co.arkinfosys.service.AbstractLineService#loadBySlip(jp.co.arkinfosys.dto.AbstractSlipDto)
	 */
	@Override
	public List<DepositLineDto> loadBySlip(DepositSlipDto dto)
			throws ServiceException {
		LinkedHashMap<String, Object> conditions = new LinkedHashMap<String, Object>();

		// 条件設定
		// 伝票番号が一致
		conditions.put(Param.DEPOSIT_SLIP_ID, dto.depositSlipId);
		conditions.put(Param.SORT_COLUMN_DEPOSIT_LINE, COLUMN_DEPOSIT_LINE_NO);
		conditions.put(Param.SORT_ORDER, Constants.SQL.ASC);

		List<DepositLine> entityList = findByCondition(conditions, params,
				"deposit/FindDepositLine.sql");

		List<DepositLineDto> result = dto.getLineDtoList();
		if (result == null) {
			result = new ArrayList<DepositLineDto>();
		} else {
			result.clear();
		}

		if (entityList != null) {
			for (DepositLine dl : entityList) {
				result.add(this.createAndCopy(dto.priceFractCategory, dl));
			}
		}

		return result;
	}

	/**
	 *
	 * @param slipDto {@link DepositSlipDto}
	 * @param lineList {@link DepositLineDto}のリスト
	 * @param deletedLineIds 削除する明細行IDのカンマ区切り文字列
	 * @param abstractServices 処理内で使用するサービス
	 * @throws ServiceException
	 * @see jp.co.arkinfosys.service.AbstractLineService#save(jp.co.arkinfosys.dto.AbstractSlipDto,java.util.List, java.lang.String,jp.co.arkinfosys.service.AbstractService[])
	 */
	@Override
	public void save(DepositSlipDto slipDto, List<DepositLineDto> lineList,
			String deletedLineIds, AbstractService<?>... abstractServices) throws ServiceException {
		short lineNo = 0;
		for (DepositLineDto lineDto : lineList) {

			// 入力内容が存在する行だけを登録対象とする
			if (lineDto.isBlank()){
				continue;
			}
			lineNo++;
			lineDto.lineNo = String.valueOf(lineNo);

			lineDto.depositSlipId = slipDto.depositSlipId;
			lineDto.depositCategory = slipDto.depositCategory;

			// 明細の登録 --------------------------------

			if (lineDto.depositLineId == null || lineDto.depositLineId.length() == 0) {
				// 伝票番号の発番
				Long newLineId = seqMakerService.nextval(DepositLine.TABLE_NAME);

				lineDto.depositLineId = newLineId.toString();
			}

			DepositLine dl = this.createAndCopy(slipDto.priceFractCategory, lineDto);
			int count = updateRecord(dl);
			if (count == 0) {
				count = insertRecord(dl);
			}
		}
		if (deletedLineIds != null && deletedLineIds.length() > 0) {
			String[] ids = deletedLineIds.split(",");
			super.updateAudit(ids);
			deleteRecordsByLineId(ids);
		}
	}

	/**
	 *
	 * @param entity {@link DepositLine}
	 * @return 登録した件数
	 * @throws ServiceException
	 * @see jp.co.arkinfosys.service.AbstractLineService#insertRecord(java.lang.Object)
	 */
	@Override
	protected int insertRecord(DepositLine entity) throws ServiceException {
		// MAPの生成
		Map<String, Object> param = new HashMap<String, Object>();

		// 基礎となるカラム名(空で)をエンティティからPUT
		// BeanMap FoundationParam =
		// Beans.createAndCopy(BeanMap.class,this.depositLine).execute();
		// param.putAll(FoundationParam);

		NumberConverter conv = new NumberConverter(mineDto.priceFractCategory,
				mineDto.unitPriceDecAlignment, false);
		// アクションフォームの情報をPUT
		BeanMap AFparam = Beans.createAndCopy(BeanMap.class, entity)
				.converter(conv, "price").execute();
		param.putAll(AFparam);

		// 更新日時とかPUT
		Map<String, Object> CommonParam = super.createSqlParam();
		param.putAll(CommonParam);

		// SQLクエリを投げる
		return this.updateBySqlFile("deposit/InsertDepositLine.sql", param)
				.execute();
	}

	/**
	 *
	 * @param entity {@link DepositLine}
	 * @return 更新した件数
	 * @throws ServiceException
	 * @see jp.co.arkinfosys.service.AbstractLineService#updateRecord(java.lang.Object)
	 */
	@Override
	protected int updateRecord(DepositLine entity) throws ServiceException {
		// MAPの生成
		Map<String, Object> param = new HashMap<String, Object>();

		NumberConverter conv = new NumberConverter(mineDto.priceFractCategory,
				mineDto.unitPriceDecAlignment, false);
		// アクションフォームの情報をPUT
		BeanMap AFparam = Beans.createAndCopy(BeanMap.class, entity)
				.converter(conv, "price").execute();
		param.putAll(AFparam);

		// 更新日時とかPUT
		Map<String, Object> CommonParam = super.createSqlParam();
		param.putAll(CommonParam);

		// SQLクエリを投げる
		return this.updateBySqlFile("deposit/UpdateDepositLine.sql", param)
				.execute();
	}

	/**
	 *
	 * @param id 伝票ID
	 * @return 削除した件数
	 * @throws ServiceException
	 * @see jp.co.arkinfosys.service.AbstractLineService#deleteRecords(java.lang.String)
	 */
	@Override
	public int deleteRecords(String id) throws ServiceException {
		try {
			Map<String, Object> param = super.createSqlParam();
			param.put(Param.DEPOSIT_SLIP_ID, id);
			return this.updateBySqlFile("deposit/DeleteLinesBySlipId.sql", param).execute();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 *
	 * @param ids 明細IDの配列
	 * @return 削除した件数
	 * @throws ServiceException
	 * @see jp.co.arkinfosys.service.AbstractLineService#deleteRecordsByLineId(java.lang.String[])
	 */
	@Override
	protected int deleteRecordsByLineId(String[] ids) throws ServiceException {
		try {
			Map<String, Object> param = super.createSqlParam();
			param.put(Param.DEPOSIT_LINE_IDS, ids);
			return this.updateBySqlFile(
					"deposit/DeleteLinesByLineIds.sql", param)
					.execute();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 *
	 * @return {DEPOSIT_SLIP_ID, DEPOSIT_LINE_ID}
	 * @see jp.co.arkinfosys.service.AbstractLineService#getKeyColumnNames()
	 */
	@Override
	protected String[] getKeyColumnNames() {
		return new String[] {"DEPOSIT_SLIP_ID", "DEPOSIT_LINE_ID"};
	}

	/**
	 *
	 * @return DEPOSIT_LINE_TRN
	 * @see jp.co.arkinfosys.service.AbstractLineService#getTableName()
	 */
	@Override
	protected String getTableName() {
		return "DEPOSIT_LINE_TRN";
	}

}
