/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.common.SlipStatusCategories;
import jp.co.arkinfosys.dto.StockInfoDto;
import jp.co.arkinfosys.dto.rorder.ROrderLineDto;
import jp.co.arkinfosys.dto.rorder.ROrderSlipDto;
import jp.co.arkinfosys.entity.Product;
import jp.co.arkinfosys.entity.RoLineTrn;
import jp.co.arkinfosys.entity.join.ProductJoin;
import jp.co.arkinfosys.service.exception.ServiceException;

import org.seasar.framework.beans.util.BeanMap;
import org.seasar.framework.beans.util.Beans;

/**
 * 受注伝票明細行サービスクラスです.
 * @author Ark Information Systems
 *
 */
public class RoLineService extends AbstractLineService<RoLineTrn,ROrderLineDto,ROrderSlipDto> {

	@Resource
	private SeqMakerService seqMakerService;

	@Resource
	private ProductStockService productStockService;

	@Resource
	private ProductService productService;

	/**
	 *
	 * パラメータ定義クラスです.
	 *
	 */
	public static class Param {
		// 受注伝票明細行
		public static final String RO_LINE_ID = "roLineId";
		public static final String STATUS = "status";
		public static final String RO_SLIP_ID = "roSlipId";
		public static final String LINE_NO = "lineNo";
		public static final String ESTIMATE_LINE_ID = "estimateLineId";
		public static final String LAST_SHIP_DATE = "lastShipDate";
		public static final String PRODUCT_CODE = "productCode";
		public static final String CUSTOMER_PCODE = "customerPcode";
		public static final String PRODUCT_ABSTRACT = "productAbstract";
		public static final String QUANTITY = "quantity";
		public static final String UNIT_PRICE = "unitPrice";
		public static final String UNIT_CATEGORY = "unitCategory";
		public static final String UNIT_NAME = "unitName";
		public static final String PACK_QUANTITY = "packQuantity";
		public static final String UNIT_RETAIL_PRICE = "unitRetailPrice";
		public static final String RETAIL_PRICE = "retailPrice";
		public static final String UNIT_COST = "unitCost";
		public static final String COST = "cost";
		public static final String TAX_CATEGORY = "taxCategory";
		public static final String CTAX_RATE = "ctaxRate";
		public static final String CTAX_PRICE = "ctaxPrice";
		public static final String REMARKS = "remarks";
		public static final String EAD_REMARKS = "eadRemarks";
		public static final String PRODUCT_REMARKS = "productRemarks";
		public static final String REST_QUANTITY = "restQuantity";
		public static final String RACK_CODE_SRC = "rackCodeSrc";
		public static final String CRE_FUNC = "creFunc";
		public static final String CRE_DATETM = "creDatetm";
		public static final String CRE_USER = "creUser";
		public static final String UPD_FUNC = "updFunc";
		public static final String UPD_DATETM = "updDatetm";
		public static final String UPD_USER = "updUser";
		public static final String RORDER_LINE_IDS = "rorderLineIds";
	}

	/**
	 *
	 * テーブル名定義クラスです.
	 *
	 */
	public static class Table {
		/** テーブル名：支払伝票明細行 */
		private static final String RO_LINE_TRN = "RO_LINE_TRN";
	}

	/**
	 * 検索条件パラメータを設定して返します.
	 * @param entity 受注伝票明細行エンティティ
	 * @return 検索条件パラメータ
	 */
	protected Map<String, Object> setLineEntityToParam(RoLineTrn entity) {
		Map<String, Object> param = super.createSqlParam();

		param.put(Param.RO_LINE_ID, entity.roLineId);

		param.put(Param.STATUS, entity.status);
		param.put(Param.RO_SLIP_ID, entity.roSlipId);
		param.put(Param.LINE_NO, entity.lineNo);
		param.put(Param.ESTIMATE_LINE_ID,
				entity.estimateLineId);
		param.put(Param.LAST_SHIP_DATE, entity.lastShipDate);
		param.put(Param.PRODUCT_CODE, entity.productCode);
		param.put(Param.CUSTOMER_PCODE, entity.customerPcode);
		param.put(Param.PRODUCT_ABSTRACT,
				entity.productAbstract);
		param.put(Param.QUANTITY, entity.quantity);
		param.put(Param.UNIT_PRICE, entity.unitPrice);
		param.put(Param.UNIT_CATEGORY, entity.unitCategory);
		param.put(Param.UNIT_NAME, entity.unitName);
		param.put(Param.PACK_QUANTITY, entity.packQuantity);
		param.put(Param.UNIT_RETAIL_PRICE,
				entity.unitRetailPrice);
		param.put(Param.RETAIL_PRICE, entity.retailPrice);
		param.put(Param.UNIT_COST, entity.unitCost);
		param.put(Param.COST, entity.cost);
		param.put(Param.TAX_CATEGORY, entity.taxCategory);
		param.put(Param.CTAX_RATE, entity.ctaxRate);
		param.put(Param.CTAX_PRICE, entity.ctaxPrice);
		param.put(Param.REMARKS, entity.remarks);
		param.put(Param.EAD_REMARKS, entity.eadRemarks);
		param.put(Param.PRODUCT_REMARKS, entity.productRemarks);
		param.put(Param.REST_QUANTITY, entity.restQuantity);
		param.put(Param.RACK_CODE_SRC, entity.rackCodeSrc);

		return param;

	}

	/**
	 * 受注伝票DTOを指定して、受注伝票明細行情報を取得します.
	 * @param dto 受注伝票DTO
	 * @return 受注伝票明細行DTOリスト
	 * @throws ServiceException
	 */
	@Override
	public List<ROrderLineDto> loadBySlip(ROrderSlipDto dto) throws ServiceException {

		if (dto == null || dto.roSlipId == null) {
			return new ArrayList<ROrderLineDto>();
		}
		// SQLパラメータを構築する
		Map<String, Object> param = super.createSqlParam();
		param.put(RoSlipService.Param.RO_SLIP_ID, dto.roSlipId);
		param.put(RoSlipService.Param.CATEGORY_ID, SlipStatusCategories.RO_LINE_STATUS);

		List<RoLineTrn> resultList = this.selectBySqlFile(RoLineTrn.class,
				"rorder/FindRoLineAndCategoryBySlipId.sql", param)
				.getResultList();
		List<ROrderLineDto> dtoList = new ArrayList<ROrderLineDto>();
		for (RoLineTrn entity : resultList) {
			ROrderLineDto lineDto = new ROrderLineDto();
			Beans.copy(entity, lineDto).execute();
			dtoList.add(lineDto);
			lineDto.lineNo = String.valueOf(dtoList.size());
		}
		return dtoList;
	}

	/**
	 * 受注伝票明細行を保存します.
	 * @param slipDto 受注伝票DTO
	 * @param lineList 受注伝票明細行DTOリスト
	 * @param deletedLineIds 削除対象受注伝票明細行ID文字列
	 * @param abstractServices 保存で使用するサービス
	 * @throws ServiceException
	 */
	@Override
	public void save(ROrderSlipDto slipDto, List<ROrderLineDto> lineList,
			String deletedLineIds
			, AbstractService<?>... abstractServices) throws ServiceException {
		try {
			short lineno = 0;
			if (lineList != null && lineList.size() > 0) {
				for (ROrderLineDto dto : lineList) {
					// 行番号は、1から通番
					lineno++;

					// 受注伝票番号を明細に設定する。
					dto.roSlipId = slipDto.roSlipId;

					RoLineTrn entity = Beans
							.createAndCopy(RoLineTrn.class, dto).dateConverter(
									Constants.FORMAT.TIMESTAMP, "updDatetm")
							.execute();

					if (dto.roLineId == null || dto.roLineId.length() == 0) {
						// 見積伝票明細番号を採番
						dto.roLineId = Long.toString(seqMakerService
								.nextval(Table.RO_LINE_TRN));
						entity.roLineId = Integer.parseInt(dto.roLineId);

						// 新規の場合のみ行番号を設定
						entity.lineNo = lineno;

						insertRecord(entity);

					} else {
						updateRecord(entity);
					}
				}

			}

			if (deletedLineIds != null && deletedLineIds.length() > 0) {
				String[] ids = deletedLineIds.split(",");
				deleteRecordsByLineId(ids);
			}

		} catch (NumberFormatException e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 受注伝票明細行を登録します.
	 * @param entity 受注伝票明細行エンティティ
	 * @return 登録件数
	 * @throws ServiceException
	 */
	@Override
	protected int insertRecord(RoLineTrn entity) throws ServiceException {
		try {
			Map<String, Object> param = setLineEntityToParam(entity);

			return this.updateBySqlFile("rorder/InsertLine.sql", param)
					.execute();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 受注伝票明細行を更新します.
	 * @param entity 受注伝票明細行エンティティ
	 * @return 更新件数
	 * @throws ServiceException
	 */
	@Override
	protected int updateRecord(RoLineTrn entity) throws ServiceException {
		try {
			Map<String, Object> param = setLineEntityToParam(entity);

			return this.updateBySqlFile("rorder/UpdateLine.sql", param)
					.execute();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 受注伝票明細行IDを指定して明細行を削除します.
	 * @param id 受注伝票明細行ID
	 * @return 削除件数
	 * @throws ServiceException
	 */
	@Override
	public int deleteRecords(String id) throws ServiceException {
		try {
			Map<String, Object> param = super.createSqlParam();
			param.put(Param.RO_SLIP_ID, id);
			return this.updateBySqlFile("rorder/DeleteLinesBySlipId.sql", param).execute();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 受注伝票明細行IDを複数指定して明細行を削除します.
	 *
	 * @param ids 削除対象の受注伝票明細行IDリスト
	 * @return 削除件数
	 * @throws ServiceException
	 */
	protected int deleteRecordsByLineId(String[] ids)
			throws ServiceException {
		if(ids.length == 0) {
			return LockResult.NOT_EXISTS;
		}

		try {
			Map<String, Object> param = super.createSqlParam();
			param.put(Param.RORDER_LINE_IDS, ids);
			return this.updateBySqlFile(
					"rorder/DeleteLinesByLineIds.sql", param)
					.execute();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 商品情報を設定します.<BR>
	 * 受注限度数 在庫管理区分 商品が削除されている場合には、0を設定します.
	 *
	 * @param lineDto　受注伝票明細行（{@link ROrderLineDto}）
	 * @throws ServiceException
	 */
	protected void setProductInfo(ROrderLineDto lineDto) throws ServiceException {

		Product product;
		try {
			product = productService.findById(lineDto.productCode);
			if ((product != null) && (product.roMaxNum != null)) {
				lineDto.roMaxNum = product.roMaxNum.toString();
			} else {
				lineDto.roMaxNum = "0";
			}
			if (product != null) {
				lineDto.stockCtlCategory = product.stockCtlCategory;
			} else {
				lineDto.stockCtlCategory = "";
			}
		} catch (ServiceException e) {
			lineDto.roMaxNum = "0";
			lineDto.stockCtlCategory = "";
		}
	}

	/**
	 * 受注伝票明細行の在庫情報を設定します.
	 *
	 * @param lineDto　受注伝票明細行（{@link ROrderLineDto}）
	 * @throws ServiceException
	 */
	protected void setStockInfo(ROrderLineDto lineDto) throws ServiceException {

		// 各商品ごとに引当可能数を計算する
		StockInfoDto stockInfo = productStockService
				.calcStockQuantityByProductCode(lineDto.productCode);
		if (stockInfo != null) {
			lineDto.possibleDrawQuantity = String
					.valueOf(stockInfo.possibleDrawQuantity);
		} else {
			lineDto.possibleDrawQuantity = "0";
		}
	}


	/**
	 * 見積伝票複写時に必要な商品情報を設定します.<BR>
	 * 商品が削除されている場合には、0または空値を設定します.
	 *
	 * @param lineDto　受注伝票明細行（{@link ROrderLineDto}）
	 * @throws ServiceException
	 */
	protected void setProductInfoFromCopy(ROrderLineDto lineDto) throws ServiceException {

		ProductJoin product;
		try {
			product = productService.findById(lineDto.productCode);
			if ((product != null) && (product.roMaxNum != null)) {
				lineDto.roMaxNum = product.roMaxNum.toString();
			} else {
				lineDto.roMaxNum = "0";
			}
			if (product != null) {
				lineDto.stockCtlCategory = product.stockCtlCategory;
				lineDto.eadRemarks = product.eadRemarks;
				lineDto.productRemarks = product.remarks;
				lineDto.supplierPcode = product.supplierPcode;
				lineDto.taxCategory = product.taxCategory;
			} else {
				lineDto.stockCtlCategory = "";
				lineDto.eadRemarks = "";
				lineDto.productRemarks = "";
				lineDto.supplierPcode = "";
				lineDto.taxCategory = "";
			}
		} catch (ServiceException e) {
			lineDto.roMaxNum = "0";
			lineDto.stockCtlCategory = "";
			lineDto.eadRemarks = "";
			lineDto.productRemarks = "";
			lineDto.supplierPcode = "";
			lineDto.taxCategory = "";
		}
	}

	/**
	 * 見積伝票複写時に必要な在庫情報を設定します.
	 *
	 * @param lineDto　受注伝票明細行（{@link ROrderLineDto}）
	 * @throws ServiceException
	 */
	protected void setStockInfoFromCpopy(ROrderLineDto lineDto) throws ServiceException {

		// 各商品ごとに引当可能数を計算する
		StockInfoDto stockInfo = productStockService
				.calcStockQuantityByProductCode(lineDto.productCode);
		if (stockInfo != null) {
			//lineDto.possibleDrawQuantity = String.valueOf(stockInfo.possibleDrawQuantity);
			lineDto.rackCodeSrc = stockInfo.rackCode;
		} else {
			//lineDto.possibleDrawQuantity = "0";
			lineDto.rackCodeSrc = "";
		}
	}


	/**
	 * 設定された伝票に関連する情報を全て読み込んでDTOに設定します.
	 *
	 * @param lineDto　受注伝票明細行（{@link ROrderLineDto}）
	 * @throws ServiceException
	 */
	public void setStockInfoForm(ROrderLineDto lineDto) throws ServiceException {

		// 商品マスタを参照して在庫数等を設定
		setProductInfo(lineDto);

		// 引当可能数設定
		setStockInfo(lineDto);
	}

	/**
	 * 伝票複写時に、関連する情報を全て取得して受注伝票明細DTOに設定します.
	 *
	 * @param lineDto　受注伝票明細行（{@link ROrderLineDto}）
	 * @throws ServiceException
	 */
	public void setProductStockInfoFromCopy(ROrderLineDto lineDto) throws ServiceException {
		// 商品情報を設定
		setProductInfoFromCopy(lineDto);

		// 在庫情報を設定
		setStockInfoFromCpopy(lineDto);
	}

	/**
	 * テーブル名を返します.
	 * @return 受注伝票明細行テーブル名
	 */
	@Override
	protected String[] getKeyColumnNames() {
		return new String[] { "RO_SLIP_ID", "RO_LINE_ID" };
	}

	/**
	 * キーカラム名を返します.
	 * @return 受注伝票明細行テーブルのキーカラム名
	 */
	@Override
	protected String getTableName() {
		return "RO_LINE_TRN";
	}

}
