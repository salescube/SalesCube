/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.service.porder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.transaction.UserTransaction;

import jp.co.arkinfosys.action.ajax.CommonPOrderAction;
import jp.co.arkinfosys.common.CategoryTrns;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.common.SlipStatusCategories;
import jp.co.arkinfosys.common.StringUtil;
import jp.co.arkinfosys.dto.StockInfoDto;
import jp.co.arkinfosys.dto.porder.InputPOrderLineDto;
import jp.co.arkinfosys.dto.porder.InputPOrderSlipDto;
import jp.co.arkinfosys.entity.PoLineTrn;
import jp.co.arkinfosys.entity.PoSlipTrn;
import jp.co.arkinfosys.entity.join.PoLineTrnJoin;
import jp.co.arkinfosys.form.porder.InputPOrderForm;
import jp.co.arkinfosys.service.AbstractLineService;
import jp.co.arkinfosys.service.AbstractService;
import jp.co.arkinfosys.service.PoSlipService;
import jp.co.arkinfosys.service.ProductService;
import jp.co.arkinfosys.service.ProductStockService;
import jp.co.arkinfosys.service.SeqMakerService;
import jp.co.arkinfosys.service.SupplierLineService;
import jp.co.arkinfosys.service.YmService;
import jp.co.arkinfosys.service.exception.ServiceException;

import org.seasar.framework.beans.util.BeanMap;
import org.seasar.framework.beans.util.Beans;

/**
 *
 * 発注入力明細行サービスクラスです.
 * @author Ark Information Systems
 *
 */
public class InputPOrderLineService extends AbstractLineService<PoLineTrn,InputPOrderLineDto,InputPOrderSlipDto> {


	//public static class Table {
	//	/** 発注伝票明細行テーブル名 */
	//	public static final String LINE_TABLE_NAME = "PORDER_LINE_TRN";
	//}

	/**
	 * パラメータ定義クラスです.
	 */
	// 独自に代入あるいはチェックすべきパラメータ(DB対応有)
	public static class Param {
		public static final String PO_SLIP_ID = "poSlipId";
		public static final String PO_DATE = "poDate";
		public static final String PO_ANNUAL = "poAnnual";
		public static final String PO_MONTHLY = "poMonthly";
		public static final String PO_YM = "poYm";
		public static final String PO_LINE_ID = "poLineId";
		public static final String LINE_NO = "lineNo";
		public static final String PRODUCT_CODE = "productCode";
		public static final String QUANTITY = "quantity";
		public static final String REST_QUANTITY = "restQuantity";
		public static final String CRE_DATETM = "creDatetm";
		public static final String UPD_DATETM = "updDatetm";
		// public static final String LINE_UPD_DATETM = "lineUpdDatetm";
		public static final String RATE = "rate";
		public static final String SUPPLIER_RATE = "supplierRate";
		public static final String CTAX_RATE = "ctaxRate";
//		public static final String SUPPLIER_TAX_RATE = "supplierTaxRate";
		public static final String PRODUCT_CODE_LIST = "productCodeList";
		public static final String STATUS = "status";
		public static final String PO_LINE_IDS = "poLineIds";
	}

	/**
	 *
	 * ローカルのみで使用するパラメータ定義クラスです.
	 *
	 */
	// その他ローカルでしか使わないパラメータ(DB対応無)
	public static class ParamLocal {
		public static final String productIsExist = "productIsExist";
		public static final String PO_LINE_STATUS = "poLineStatusCategory";
		public static final String PRODUCT_STATUS_SALE_CANCEL = "productStatusSaleCancel";

	}

	// 発番エラー検出用初期値
	public static final Long DEFAULT_ID = -1L;
	// 有効明細行無しの場合の戻り値
	public static final Long NO_VALID_LINE = -2L;
	// 発番エラー時の戻り値
	public static final Long CANNOT_GET_ID = DEFAULT_ID;
	// 伝票登録失敗時の戻り値
	public static final Long CANNOT_CREATE_SLIP = -3L;
	public static final Long CANNOT_UPDATE_SLIP = CANNOT_CREATE_SLIP;
	public static final Long CANNOT_DELETE_SLIP = CANNOT_CREATE_SLIP;
	// アクションフォームに対応する値がないです
	public static final Long LACK_OF_VALUES = -4L;

	// 任意のタイミングでロールバックしたい
	public UserTransaction userTransaction;

	// アクションフォーム
	@Resource
	protected InputPOrderForm inputPOrderForm;

	//
	protected CommonPOrderAction commonPOrderAction;

	// 伝票と明細行のエンティティ(カラム名がほしいだけ)
	public PoSlipTrn poSlipTrn = new PoSlipTrn();
	public PoLineTrn poLineTrn = new PoLineTrn();

	// 発番のため
	@Resource
	private SeqMakerService seqMakerService;

	@Resource
	private ProductService productService;

	// 年月度取得のため
	@Resource
	protected YmService ymService;

	@Resource
	private ProductStockService productStockService;

	@Resource
	private SupplierLineService supplierLineService;

	@Resource
	private PoSlipService poSlipService;



	/**
	 * 発注伝票番号を指定して発注伝票明細行を削除します.
	 * @param id 発注伝票番号
	 * @return 削除件数
	 * @throws ServiceException
	 */
	@Override
	public int deleteRecords(String id) throws ServiceException {
		try {
			Map<String, Object> param = super.createSqlParam();
			param.put(InputPOrderSlipService.Param.PO_SLIP_ID, id);
			return this.updateBySqlFile(
					"porder/DeletePOrderLinesBySlipId.sql", param)
					.execute();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 発注伝票明細行IDを複数指定して明細行を削除します.
	 * @param ids 削除対象の発注伝票明細行IDリスト
	 * @return 削除件数
	 * @throws ServiceException
	 */
	@Override
	protected int deleteRecordsByLineId(String[] ids) throws ServiceException {
		try {
			Map<String, Object> param = super.createSqlParam();
			param.put(Param.PO_LINE_IDS, ids);
			return this.updateBySqlFile(
					"porder/DeletePOrderLinesByLineIdList.sql", param)
					.execute();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 検索条件パラメータを設定して返します.
	 * @param entity 発注伝票明細行エンティティ
	 * @return 検索条件パラメータ
	 */
	private Map<String, Object> setEntityToParam(PoLineTrn entity){

		//MAPの生成
		Map<String, Object> param = new HashMap<String, Object>();

		//アクションフォームの情報をPUT
		BeanMap AFparam = Beans.createAndCopy(BeanMap.class,entity).execute();
		param.putAll(AFparam);

		//更新日時とかPUT
		Map<String, Object> CommonParam = super.createSqlParam();
		param.putAll(CommonParam);

		return param;
	}

	/**
	 * 発注伝票明細行を登録します.
	 * @param entity 発注伝票明細行エンティティ
	 * @return 登録件数
	 * @throws ServiceException
	 */
	@Override
	protected int insertRecord(PoLineTrn entity) throws ServiceException {
		try {
			PoLineTrn e = Beans.createAndCopy(PoLineTrn.class,entity).excludesWhitespace().execute();
			Map<String, Object> param = setEntityToParam(e);

			return this.updateBySqlFile("porder/InsertPOrderLine.sql",
					param).execute();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 発注伝票DTOの情報を基に発注伝票明細行を取得します.
	 * @param dto 発注伝票DTO
	 * @return 発注伝票明細行情報リスト
	 * @throws ServiceException
	 */
	@Override
	public List<InputPOrderLineDto> loadBySlip(InputPOrderSlipDto dto)
			throws ServiceException {
		try {
			Map<String, Object> param = super.createSqlParam();

			param.put(InputPOrderSlipService.Param.PO_SLIP_ID, dto.poSlipId);
			param.put(ParamLocal.PO_LINE_STATUS, SlipStatusCategories.PO_LINE_STATUS);

			List<PoLineTrnJoin> resultList = this.selectBySqlFile(
					PoLineTrnJoin.class,
					"porder/FindPOrderLineByPOSlipId.sql", param)
					.getResultList();

			List<InputPOrderLineDto> dtoList = new ArrayList<InputPOrderLineDto>();
			for (PoLineTrnJoin entity : resultList) {
				InputPOrderLineDto lineDto = new InputPOrderLineDto();
				Beans.copy(entity, lineDto).execute();

				// 商品コード存在フラグの更新
				if(productService.existsProductCode(lineDto.productCode).exists){
					lineDto.productIsExist = String.valueOf(InputPOrderForm.CODE_EXIST);
				}else{
					lineDto.productIsExist = String.valueOf(InputPOrderForm.CODE_NOEXIST);
				}
				// 商品に対する発注残数
				StockInfoDto stockInfoDto = this.productStockService
					.calcStockQuantityByProductCode(lineDto.productCode);
				if( stockInfoDto != null ){
					Integer rest = stockInfoDto.porderRestQuantity + stockInfoDto.entrustRestQuantity;
					lineDto.productRestQuantity = rest.toString();
				}

				lineDto.quantityDB = lineDto.quantity;

				dtoList.add(lineDto);
			}
			return dtoList;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 発注伝票明細行を保存します.
	 * @param slipDto 発注伝票DTO
	 * @param lineList 発注伝票明細行リスト
	 * @param deletedLineIds 削除対象発注伝票明細行ID文字列
	 * @param abstractServices 保存で使用するサービス
	 * @throws ServiceException
	 */
	@Override
	public void save(InputPOrderSlipDto slipDto,
			List<InputPOrderLineDto> lineList, String deletedLineIds
			, AbstractService<?>... abstractServices)
			throws ServiceException {
		try {
			if (lineList != null && lineList.size() > 0) {
				short i = 1;
				for (InputPOrderLineDto dto : lineList) {

					// 見積番号を明細に設定する。
					dto.poSlipId = slipDto.getKeyValue();

					// 伝票側のレートを明細行側にコピー
					dto.rate = slipDto.supplierRate;
					// 伝票側の税率を明細行側にコピー
					dto.ctaxRate = slipDto.taxRate;

					PoLineTrn entity = Beans.createAndCopy(
							PoLineTrn.class, dto).dateConverter(
							Constants.FORMAT.DATE, "deliveryDate").dateConverter(
							Constants.FORMAT.TIMESTAMP, "updDatetm").execute();

					entity.lineNo = i++;
					if ( !StringUtil.hasLength(dto.poLineId)) {
						// 見積伝票明細番号を採番
						dto.poLineId = Long
								.toString(seqMakerService
										.nextval(PoLineTrn.TABLE_NAME));
						entity.poLineId = Integer.parseInt(dto.poLineId);
						// 発注残数を設定
						entity.restQuantity = entity.quantity;
						insertRecord(entity);
					} else {
						if(entity.restQuantity.compareTo(new BigDecimal(0)) <= 0){
							entity.status = Constants.STATUS_PORDER_LINE.PURCHASED;
						}

						//変更前の数量
						BigDecimal quantityDB = new BigDecimal( dto.quantityDB );

						//差分 = 変更前数量-変更後数量
						BigDecimal diffvalue  = quantityDB.subtract(entity.quantity);

						//数量変更後の発注残数　= 数量変更前の発注残数 - 差分
						entity.restQuantity = entity.restQuantity.subtract(diffvalue);

						updateRecord(entity);

						// 仕入伝票のステータスも変更する
						// 完納
						if(entity.restQuantity.compareTo(new BigDecimal(0)) <= 0){

							supplierLineService.updateDeliveryProcessCategory(entity.poLineId,CategoryTrns.DELIVERY_PROCESS_CATEGORY_FULL);


						}
					}
				}
			}

			if (deletedLineIds != null && deletedLineIds.length() > 0) {

				String[] ids = deletedLineIds.split(",");
				super.updateAudit(ids);
				deleteRecordsByLineId(ids);
			}


			// 発注伝票ステータス更新
			poSlipService.updatePOrderTrnStatusByPoSlipId(slipDto.poSlipId);


		} catch (NumberFormatException e) {
			throw new ServiceException(e);
		}

	}

	/**
	 * 発注伝票明細行を更新します.
	 * @param entity 発注伝票明細行エンティティ
	 * @return 更新件数
	 * @throws ServiceException
	 */
	@Override
	protected int updateRecord(PoLineTrn entity) throws ServiceException {
		try {
			PoLineTrn e = Beans.createAndCopy(PoLineTrn.class,entity).excludesWhitespace().execute();
			Map<String, Object> param = setEntityToParam(e);

			return this.updateBySqlFile("porder/UpdatePOrderLine.sql",
					param).execute();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * キーカラム名を返します.
	 * @return 発注伝票明細行テーブルのキーカラム名
	 */
	@Override
	protected String[] getKeyColumnNames() {
		return new String[] { "PO_SLIP_ID", "PO_LINE_ID" };
	}

	/**
	 * テーブル名を返します.
	 * @return 発注伝票明細行テーブル名
	 */
	@Override
	protected String getTableName() {
		return "PO_LINE_TRN";
	}
}
