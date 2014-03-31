/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import jp.co.arkinfosys.common.CategoryTrns;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.common.StringUtil;
import jp.co.arkinfosys.dto.YmDto;
import jp.co.arkinfosys.dto.purchase.PurchaseLineDto;
import jp.co.arkinfosys.dto.purchase.PurchaseSlipDto;
import jp.co.arkinfosys.entity.PoLineTrn;
import jp.co.arkinfosys.entity.Rate;
import jp.co.arkinfosys.entity.SupplierSlipTrn;
import jp.co.arkinfosys.entity.join.PoSlipTrnJoin;
import jp.co.arkinfosys.service.exception.ServiceException;
import jp.co.arkinfosys.service.exception.UnabledLockException;

import org.seasar.framework.beans.util.Beans;

/**
 * 仕入伝票サービスクラスです.
 *
 * @author Ark Information Systems
 *
 */

public class SupplierSlipService extends AbstractSlipService<SupplierSlipTrn,PurchaseSlipDto> {

	@Resource
	private SeqMakerService seqMakerService;

	@Resource
	private PoSlipService poSlipService;

	@Resource
	private YmService ymService;

	@Resource
	private RateService rateService;

	/**
	 *
	 * パラメータ定義クラスです.
	 *
	 */
	public static class Param {
		// 仕入伝票
		public static final String SUPPLIER_SLIP_ID = "supplierSlipId";
		public static final String STATUS = "status";
		public static final String SUPPLIER_DATE = "supplierDate";
		public static final String SUPPLIER_ANNUAL = "supplierAnnual";
		public static final String SUPPLIER_MONTHLY = "supplierMonthly";
		public static final String SUPPLIER_YM = "supplierYm";
		public static final String USER_ID = "userId";
		public static final String USER_NAME = "userName";
		public static final String SUPPLIER_SLIP_CATEGORY = "supplierSlipCategory";
		public static final String SUPPLIER_CODE = "supplierCode";
		public static final String SUPPLIER_NAME = "supplierName";
		public static final String SUPPLIER_CM_CATEGORY = "supplierCmCategory";
		public static final String DELIVERY_DATE = "deliveryDate";
		public static final String RATE_ID = "rateId";
		public static final String TAX_SHIFT_CATEGORY = "taxShiftCategory";
		public static final String TAX_FRACT_CATEGORY = "taxFractCategory";
		public static final String PRICE_FRACT_CATEGORY = "priceFractCategory";
		public static final String CTAX_TOTAL = "ctaxTotal";
		public static final String PRICE_TOTAL = "priceTotal";
		public static final String FE_PRICE_TOTAL = "fePriceTotal";
		public static final String PO_SLIP_ID = "poSlipId";
		public static final String PAYMENT_SLIP_ID = "paymentSlipId";
		public static final String SUPPLIER_PAYMENT_DATE = "supplierPaymentDate";
		public static final String PAYMENT_CUTOFF_DATE = "paymentCutoffDate";
		public static final String PAYMENT_PDATE = "paymentPdate";
		public static final String REMARKS = "remarks";

		// 仕入伝票明細行
		public static final String SUPPLIER_LINE_ID = "supplierLineId";
		public static final String LINE_NO = "lineNo";
		public static final String PRODUCT_CODE = "productCode";
		public static final String SUPPLIER_PCODE = "supplierPcode";
		public static final String PRODUCT_ABSTRACT = "productAbstract";
		public static final String PRODUCT_REMARKS = "productRemarks";
		public static final String SUPPLIER_DETAIL_CATEGORY = "supplierDetailCategory";
		public static final String DELIVERY_PROCESS_CATEGORY = "deliveryProcessCategory";
		public static final String TEMP_UNIT_PRICE_CATEGORY = "tempUnitPriceCategory";
		public static final String TAX_CATEGORY = "taxCategory";
		public static final String QUANTITY = "quantity";
		public static final String UNIT_PRICE = "unitPrice";
		public static final String PRICE = "price";
		public static final String CTAX_RATE = "ctaxRate";
		public static final String CTAX_PRICE = "ctaxPrice";
		public static final String DOL_UNIT_PRICE = "dolUnitPrice";
		public static final String DOL_PRICE = "dolPrice";
		public static final String RATE = "rate";
		public static final String RACK_CODE = "rackCode";
		public static final String RACK_NAME = "rackName";
		public static final String WAREHOUSE_NAME = "warehouseName";
		public static final String PO_LINE_ID = "poLineId";
		public static final String PAYMENT_LINE_ID = "paymentLineId";

		public static final String REST_QUANTITY = "restQuantity"; // 発注伝票明細　残数

		// 作業用
		public static final String COND_ORDERED = "condOrderd"; // 発注
		public static final String COND_NOWPURCHASING = "condNowPurchasing"; // 分納中
		public static final String COND_PURCHASED = "condPurchased"; // 仕入完了
	}

	/**
	 *
	 * テーブル名定義クラスです.
	 *
	 */
	public static class Table {
		/** テーブル名：仕入伝票 */
		private static final String SUPPLIER_SLIP_TRN = "SUPPLIER_SLIP_TRN";
	}

	/**
	 * 先頭の明細行のレートと消費税率を伝票に設定します.
	 * @param supplierSlipTrnDto 仕入伝票DTO
	 * @param supplierLineTrnDtoList 仕入伝票明細行DTO
	 */
	public void setLineData(PurchaseSlipDto supplierSlipTrnDto,List<PurchaseLineDto> supplierLineTrnDtoList) {
		supplierSlipTrnDto.setLineDtoList(supplierLineTrnDtoList);

		// 先頭の明細行のレートを伝票にセットする
		supplierSlipTrnDto.rate = supplierLineTrnDtoList.get(0).rate;
	}

	/**
	 * 仕入伝票の新規登録・更新処理を行います.
	 * @param dto 仕入伝票DTO
	 * @param abstractServices 保存で使用するサービス
	 * @return ロック結果
	 * @throws ServiceException
	 * @throws UnabledLockException
	 */
	@Override
	public int save(PurchaseSlipDto dto, AbstractService<?>... abstractServices)
			throws ServiceException, UnabledLockException {

		int lockResult = LockResult.SUCCEEDED;

		if (dto.newData == null || dto.newData ) {
			// 登録する
			insertRecord(dto);
		} else {
			// 更新する
			lockResult = updateRecord(dto);
		}
		return lockResult;
	}

	/**
	 * 仕入伝票を登録します.
	 * @param dto 仕入伝票DTO
	 * @return 0(固定値)
	 * @throws ServiceException
	 */
	@Override
	protected int insertRecord(PurchaseSlipDto dto) throws ServiceException {
		String supplierSlipId = "";

		// 仕入伝票番号を発番
		try {
			supplierSlipId = (Long.toString(seqMakerService
					.nextval(Table.SUPPLIER_SLIP_TRN)));
		} catch (Exception e) {
			throw new ServiceException(e);
		}
		dto.supplierSlipId = supplierSlipId;

		// 登録 ： 仕入伝票、仕入伝票
		insertSupplierSlip(dto);

		return 0;
	}

	/**
	 * 仕入伝票を更新します.
	 * @param dto 仕入伝票DTO
	 * @return 更新件数
	 * @throws UnabledLockException
	 * @throws ServiceException
	 */
	@Override
	protected int updateRecord(PurchaseSlipDto dto)
			throws UnabledLockException, ServiceException {
		try {
			// 排他制御
			isLocked(dto.getKeyValue(),dto.updDatetm);

			// 伝票の更新
			int SuccessedLineCount = 0;
			try {
				// 仕入日から仕入年度、仕入月度、仕入年月度を取得
				setSlipDataByForm(dto);

				// Entityオブジェクトに変換
				SupplierSlipTrn slipTrn = Beans.createAndCopy(
						SupplierSlipTrn.class, dto).execute();

				// パラメータを設定
				Map<String, Object> param = setEntityToParam(slipTrn);

				// SQLクエリを投げる
				SuccessedLineCount = this.updateBySqlFile(
						"purchase/UpdateSupplierSlipTrn.sql", param).execute();

			} catch (ServiceException e) {
				e.printStackTrace();
				SuccessedLineCount = 0;
			}
			return SuccessedLineCount;

		} catch (UnabledLockException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}


	/**
	 * 仕入伝票と仕入伝票明細行を登録します.
	 * @param dto 仕入伝票DTO
	 * @throws ServiceException
	 */
	private void insertSupplierSlip(PurchaseSlipDto dto)
			throws ServiceException {
		// 仕入日から仕入年度、仕入月度、仕入年月度を取得
		setSlipDataByForm(dto);

		// Entityオブジェクトに変換
		SupplierSlipTrn slipTrn = Beans.createAndCopy(SupplierSlipTrn.class,
				dto).execute();

		// パラメータを設定
		Map<String, Object> param = setEntityToParam(slipTrn);

		// 仕入伝票登録
		this.updateBySqlFile("purchase/InsertSupplierSlipTrn.sql", param)
				.execute();

	}

	/**
	 * 伝票登録・更新後に複写元の発注伝票を更新します.
	 * @param insert 登録か否か
	 * @param dto 仕入伝票DTO
	 * @param deletedLineStoreDto 削除対象の仕入伝票明細行を保持する仕入伝票DTO
	 * @throws ServiceException
	 */
	public void afterUpsert(boolean insert, PurchaseSlipDto dto, PurchaseSlipDto deletedLineStoreDto) throws ServiceException {

		// 発注伝票を伝票複写した場合,発注伝票と伝票明細のSTATUSを更新する
		if (StringUtil.hasLength(((PurchaseSlipDto)dto).poSlipId)) {
			// 登録・更新された明細に紐付く発注明細と伝票ステータスの更新処理
			updatePoSlipAndLine(dto, true);

			if(!insert){
				updatePoSlipAndLine(deletedLineStoreDto, false);
			}
		}
	}

	/**
	 * 処理対象伝票のロック状態を確認します.
	 * @param id 仕入伝票番号
	 * @param updDatetm 更新日時
	 * @throws ServiceException
	 * @throws UnabledLockException
	 */
	protected void isLocked(String id,String updDatetm) throws ServiceException,
			UnabledLockException {
		Map<String, Object> param = super.createSqlParam();
		param.put(Param.SUPPLIER_SLIP_ID, id);
		param
				.put(AbstractService.Param.LOCK_RECORD,
						AbstractService.FOR_UPDATE);
		lockRecordBySqlFile("purchase/FindSlipBySupplierSlipId.sql", param,
				updDatetm);
	}

	/**
	 * 入力データをもとに伝票情報を設定します.
	 * @param dto 仕入伝票DTO
	 * @throws ServiceException
	 */
	private void setSlipDataByForm(PurchaseSlipDto dto)
			throws ServiceException {
		// 仕入日から仕入年度、仕入月度、仕入年月度を取得
		YmDto ymDto = ymService.getYm(dto.supplierDate);
		if (ymDto != null) {
			dto.supplierAnnual = ymDto.annual.toString();
			dto.supplierMonthly = ymDto.monthly.toString();
			dto.supplierYm = ymDto.ym.toString();
		}
	}

	/**
	 * 仕入伝票番号を指定して、仕入伝票を削除します.
	 * @param id 仕入伝票番号
	 * @param updDatetm 更新日時
	 * @return 削除件数
	 * @throws ServiceException
	 * @throws UnabledLockException
	 */
	@Override
	public int deleteById(String id, String updDatetm) throws ServiceException,
			UnabledLockException {
		// 排他制御
		isLocked(id,updDatetm);

		// 伝票の削除
		try {
			// SQLパラメータを構築する
			Map<String, Object> param = super.createSqlParam();
			param.put(SupplierSlipService.Param.SUPPLIER_SLIP_ID, id);

			return this.updateBySqlFile(
					"purchase/DeleteSlipBySupplierSlipId.sql", param)
					.execute();
		} catch (Exception e) {
			ServiceException se = new ServiceException(e);
			se.setStopOnError(true);
			throw se;
		}
	}

	/**
	 * 複写元の発注伝票と発注伝票明細行のステータスを更新します.
	 * @param dto 仕入伝票DTO
	 * @throws ServiceException
	 */
	public void updatePoStatus(PurchaseSlipDto dto) throws ServiceException{
		// 発注伝票を伝票複写した場合,発注伝票と伝票明細のSTATUSを更新する
		if (StringUtil.hasLength(((PurchaseSlipDto)dto).poSlipId)) {
			updatePoSlipAndLine(dto, false);
		}
	}

	/**
	 * 検索条件パラメータを設定して返します.
	 * @param slipTrn 仕入伝票情報エンティティ
	 * @return 検索条件パラメータ
 	 */
	private Map<String, Object> setEntityToParam(SupplierSlipTrn slipTrn) {
		Map<String, Object> param = super.createSqlParam();
		param.put(SupplierSlipService.Param.SUPPLIER_SLIP_ID,
				slipTrn.supplierSlipId); // 仕入伝票番号
		param.put(SupplierSlipService.Param.STATUS, slipTrn.status); // 状態フラグ
		param
				.put(SupplierSlipService.Param.SUPPLIER_DATE,
						slipTrn.supplierDate); // 仕入日
		param.put(SupplierSlipService.Param.SUPPLIER_ANNUAL,
				slipTrn.supplierAnnual); // 仕入年度
		param.put(SupplierSlipService.Param.SUPPLIER_MONTHLY,
				slipTrn.supplierMonthly); // 仕入月度
		param.put(SupplierSlipService.Param.SUPPLIER_YM, slipTrn.supplierYm); // 仕入年月度
		param.put(SupplierSlipService.Param.USER_ID, slipTrn.userId); // 担当者コード
		param.put(SupplierSlipService.Param.USER_NAME, slipTrn.userName); // 担当者名
		param.put(SupplierSlipService.Param.SUPPLIER_SLIP_CATEGORY,
				slipTrn.supplierSlipCategory); // 仕入伝票区分
		param
				.put(SupplierSlipService.Param.SUPPLIER_CODE,
						slipTrn.supplierCode); // 仕入先コード
		param
				.put(SupplierSlipService.Param.SUPPLIER_NAME,
						slipTrn.supplierName); // 仕入先名称
		param.put(SupplierSlipService.Param.SUPPLIER_CM_CATEGORY,
				slipTrn.supplierCmCategory); // 仕入取引区分
		param
				.put(SupplierSlipService.Param.DELIVERY_DATE,
						slipTrn.deliveryDate); // 納期
		param.put(SupplierSlipService.Param.RATE_ID, slipTrn.rateId); // レートタイプ
		param.put(SupplierSlipService.Param.TAX_SHIFT_CATEGORY,
				slipTrn.taxShiftCategory); // 税転嫁
		param.put(SupplierSlipService.Param.TAX_FRACT_CATEGORY,
				slipTrn.taxFractCategory); // 税端数処理
		param.put(SupplierSlipService.Param.PRICE_FRACT_CATEGORY,
				slipTrn.priceFractCategory); // 単価端数処理
		param.put(SupplierSlipService.Param.CTAX_TOTAL, slipTrn.ctaxTotal); // 伝票合計消費税
		param.put(SupplierSlipService.Param.CTAX_RATE, slipTrn.ctaxRate); // 伝票消費税率
		param.put(SupplierSlipService.Param.PRICE_TOTAL, slipTrn.priceTotal); // 伝票合計金額
		param.put(SupplierSlipService.Param.FE_PRICE_TOTAL,
				slipTrn.fePriceTotal); // 伝票合計外貨金額
		param.put(SupplierSlipService.Param.PO_SLIP_ID, slipTrn.poSlipId); // 発注伝票番号
		param.put(SupplierSlipService.Param.PAYMENT_SLIP_ID,
				slipTrn.paymentSlipId); // 支払伝票番号
		param.put(SupplierSlipService.Param.SUPPLIER_PAYMENT_DATE,
				slipTrn.supplierPaymentDate); // 仕入計上日
		param.put(SupplierSlipService.Param.PAYMENT_CUTOFF_DATE,
				slipTrn.paymentCutoffDate); // 支払締日時
		param
				.put(SupplierSlipService.Param.PAYMENT_PDATE,
						slipTrn.paymentPdate); // 支払締処理日
		param.put(SupplierSlipService.Param.REMARKS, slipTrn.remarks); // 備考

		return param;
	}


	/**
	 * 文字列中のカンマを削除します.
	 * @param src 変換対象文字列
	 * @return 変換後文字列
	 */
	private String deleteComma(String src) {
		return src.replaceAll(",", "");
	}

	/**
	 * 仕入伝票の登録・更新・削除に応じて発注伝票を更新します.
	 *
	 * @param inDto 仕入伝票DTO
	 * @param bUpdate 登録・更新処理か否か
	 */
	private void updatePoSlipAndLine(PurchaseSlipDto inDto, boolean bUpdate)
			throws ServiceException {
		// パラメータを設定
		Map<String, Object> param = null;


		// 紐付く発注伝票を取得する
		PoSlipTrnJoin poSlipTrnJoin = poSlipService.findPoSlipByPoSlipId(inDto.poSlipId);

		List<PurchaseLineDto> l = inDto.getLineDtoList();
		for (PurchaseLineDto lineDto : l) {
			if (!StringUtil.hasLength(lineDto.productCode)) {
				continue;
			}
			// 数量の情報を取得
			// 仕入伝票の仕入数量
			BigDecimal quantity = new BigDecimal(0);
			if (StringUtil.hasLength(lineDto.quantity)) {
				quantity = new BigDecimal( deleteComma(lineDto.quantity) );
			}
			// 仕入伝票の仕入数量（更新前）
			BigDecimal oldQuantity = new BigDecimal(0);
			if (StringUtil.hasLength(lineDto.oldQuantity)) {
				oldQuantity = new BigDecimal( deleteComma(lineDto.oldQuantity) );
			}
			PoLineTrn poLineTrn = poSlipService.getPOLineTrnByPoLineId(lineDto.poLineId);
			// 発注残数
			BigDecimal restQuantity = new BigDecimal(0);
			if (StringUtil.hasLength(lineDto.restQuantity)) {
				if(poLineTrn != null){
					restQuantity = poLineTrn.restQuantity;
				}
			}
			// 発注伝票の発注数量
			BigDecimal totalQuantity = new BigDecimal(0);
			if (StringUtil.hasLength(lineDto.totalQuantity)) {
				if(poLineTrn != null){
					totalQuantity = poLineTrn.quantity;
				}
			}

			// 他の仕入伝票で処理された数量を計算する
			BigDecimal otherQuantity = totalQuantity.subtract(restQuantity
					.add(oldQuantity));

			// 新しい残数を計算する
			BigDecimal newRestQuantity = null;
			if (bUpdate) {
				// 登録・更新
				newRestQuantity = restQuantity.subtract(quantity.subtract(oldQuantity));
			} else {
				// 削除する分を残数に加算
				newRestQuantity = restQuantity.add(quantity);
			}

			// 仕入明細のステータスから明細行のステータスを判定
			String lineStatus = Constants.STATUS_PORDER_LINE.ORDERED;

			if( poSlipTrnJoin != null && CategoryTrns.TRANSPORT_CATEGORY_ENTRUST.equals(poSlipTrnJoin.transportCategory) ) {
				// 委託発注伝票の場合、仕入が削除されたら委託出庫済み状態へ戻し、登録・更新の場合は完納状態とする
				if(bUpdate == false) {
					lineStatus = Constants.STATUS_PORDER_LINE.ENTRUST_STOCK_DELIVERED;
				} else {
					// 完納
					lineStatus = Constants.STATUS_PORDER_LINE.PURCHASED;
				}
			} else if(bUpdate == false) {
				// 仕入伝票の削除の場合、仕入明細に指定されていた完納区分には関係なく、
				// 他の仕入伝票でこの明細商品の仕入数量がない場合は未納にする。あれば仕入中にする(仕入完了に変更することはあり得ない)
				if(otherQuantity.compareTo(new BigDecimal(0)) == 0) {
					lineStatus = Constants.STATUS_PORDER_LINE.ORDERED;
				} else {
					lineStatus = Constants.STATUS_PORDER_LINE.NOWPURCHASING;
				}
			} else if(lineDto.deliveryProcessCategory.equals(CategoryTrns.DELIVERY_PROCESS_CATEGORY_FULL)){
				// 仕入伝票の登録・更新で、 仕入明細に完納が指定されていた場合
				lineStatus = Constants.STATUS_PORDER_LINE.PURCHASED;
			} else if(lineDto.deliveryProcessCategory.equals(CategoryTrns.DELIVERY_PROCESS_CATEGORY_PARTIAL)){
				//  仕入伝票の登録・更新で、仕入明細に分納が指定されていた場合
				lineStatus = Constants.STATUS_PORDER_LINE.NOWPURCHASING;
			}

			// 発注伝票明細行更新
			param = super.createSqlParam();
			param.put(SupplierSlipService.Param.PO_LINE_ID, lineDto.poLineId);
			param.put(SupplierSlipService.Param.STATUS, lineStatus);
			param.put(SupplierSlipService.Param.REST_QUANTITY, newRestQuantity);
			this.updateBySqlFile(
					"purchase/UpdatePOrderLineTrnStatusByPoLineId.sql",
					param).execute();
		}

		// 発注伝票ステータス更新
		poSlipService.updatePOrderTrnStatusByPoSlipId(((PurchaseSlipDto)inDto).poSlipId);
	}

	/**
	 * 仕入先コードを指定して、最新の締処理日を取得します.
	 * @param supplierCode 仕入先コード
	 * @return 最新の締処理日
	 */
	public SupplierSlipTrn findLatestCutoffDateBySupplierCode(
			String supplierCode) {
		Map<String, Object> param = super.createSqlParam();
		param.put(Param.SUPPLIER_CODE, supplierCode);

		return this.selectBySqlFile(SupplierSlipTrn.class,
				"purchase/FindLatestCutoffDateBySupplierCode.sql", param)
				.getSingleResult();
	}

	/**
	 * 仕入伝票番号を指定して、仕入伝票情報を取得します.
	 * @param id 仕入伝票番号
	 * @return 仕入伝票DTO
	 * @throws ServiceException
	 * @throws UnabledLockException
	 */
	@Override
	public PurchaseSlipDto loadBySlipId(String id) throws ServiceException,
			UnabledLockException {
		try {
			// SupplierSlipTrnDtoを生成する

			// 入力された仕入番号が数値であるかチェック
			try {
				Integer.valueOf(id);
			} catch (NumberFormatException e) {
				return null;
			}

			SupplierSlipTrn supplierSlipTrn = null;

			// SQLパラメータを構築する
			Map<String, Object> param = super.createSqlParam();
			param.put(SupplierSlipService.Param.SUPPLIER_SLIP_ID, id);

			supplierSlipTrn = this.selectBySqlFile(SupplierSlipTrn.class,
					"purchase/FindSlipBySupplierSlipId.sql", param)
					.getSingleResult();

			if (supplierSlipTrn == null) {
				// 伝票を取得できなかった場合はnullを返す
				return null;
			}

			PurchaseSlipDto supplierSlipTrnDto = Beans.createAndCopy(
					PurchaseSlipDto.class, supplierSlipTrn)
					.dateConverter(Constants.FORMAT.TIMESTAMP, "updDatetm")
					.execute();

			// レートタイプ名称と外貨記号を取得する
			Rate rate = rateService.findById(supplierSlipTrnDto.rateId);
			// レートIDを指定した検索なので複数はかえらない
			if (rate != null) {
				supplierSlipTrnDto.rateName = rate.name;
				supplierSlipTrnDto.sign = rate.sign;
			}

			return supplierSlipTrnDto;
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			ServiceException se = new ServiceException(e);
			se.setStopOnError(true);
			throw se;
		}
	}

	/**
	 * テーブル名を返します.
	 * @return 仕入伝票テーブル名
	 */
	@Override
	protected String getTableName() {
		return Table.SUPPLIER_SLIP_TRN;
	}

	/**
	 * キーカラム名を返します.
	 * @return 仕入伝票テーブルのキーカラム名
	 */
	@Override
	protected String getKeyColumnName() {
		return "SUPPLIER_SLIP_ID";
	}
}
