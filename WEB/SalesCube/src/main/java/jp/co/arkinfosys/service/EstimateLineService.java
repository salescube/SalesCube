/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.dto.estimate.InputEstimateDto;
import jp.co.arkinfosys.dto.estimate.InputEstimateLineDto;
import jp.co.arkinfosys.entity.EstimateLineTrn;
import jp.co.arkinfosys.entity.join.EstimateLineProductJoin;
import jp.co.arkinfosys.service.exception.ServiceException;

import org.seasar.framework.beans.util.BeanMap;
import org.seasar.framework.beans.util.Beans;

/**
 *
 * 見積伝票明細行サービスクラスです.
 * @author Ark Information Systems
 */
public class EstimateLineService extends AbstractLineService<EstimateLineTrn,InputEstimateLineDto,InputEstimateDto> {
	@Resource
	private SeqMakerService seqMakerService;

	/**
	 *
	 * テーブル名定義クラスです.
	 *
	 */
	public static class Table {
		/** 見積明細伝票テーブル名 */
		public static final String LINE_TABLE_NAME = "ESTIMATE_LINE_TRN";
	}

	/**
	 *
	 * パラメータ定義クラスです.
	 *
	 */
	public static class Param {
		public static final String ESTIMATE_LINE_ID = "estimateLineId";
		public static final String ESTIMATE_SHEET_ID = "estimateSheetId";
		public static final String LINE_NO = "lineNo";
		public static final String PRODUCT_CODE = "productCode";
		public static final String CUSTOMER_PCODE = "customerPcode";
		public static final String PRODUCT_ABSTRACT = "productAbstract";
		public static final String QUANTITY = "quantity";
		public static final String UNIT_COST = "unitCost";
		public static final String UNIT_RETAIL_PRICE = "unitRetailPrice";
		public static final String COST = "cost";
		public static final String RETAIL_PRICE = "retailPrice";
		public static final String REMARKS = "remarks";
		public static final String UPD_FUNC = "updFunc";
		public static final String UPD_USER = "updUser";

		public static final String ESTIMATE_LINE_IDS = "estimateLineIds";
	}

	/**
	 * 見積伝票明細行を登録します.
	 *
	 * @param entity 見積伝票明細行エンティティ
	 * @return 登録件数
	 * @throws ServiceException
	 */
	@Override
	protected int insertRecord(EstimateLineTrn entity)
			throws ServiceException {

		try {
			Map<String, Object> param = setEntityToParam(entity);

			return this.updateBySqlFile("estimate/InsertEstimateLine.sql",
					param).execute();
		} catch (Exception e) {
			throw new ServiceException(e);
		}

	}

	/**
	 * 見積伝票明細行を更新します.
	 *
	 * @param entity 見積伝票明細行エンティティ
	 * @return 更新件数
	 * @throws ServiceException
	 */
	@Override
	protected int updateRecord(EstimateLineTrn entity)
			throws ServiceException {
		try {
			Map<String, Object> param = setEntityToParam(entity);

			return this.updateBySqlFile("estimate/UpdateEstimateLine.sql",
					param).execute();
		} catch (Exception e) {
			throw new ServiceException(e);
		}

	}

	/**
	 * 見積伝票番号を指定して見積伝票明細行情報を削除します.
	 *
	 * @param sheetId 見積伝票番号
	 * @return 削除件数
	 * @throws ServiceException
	 */
	@Override
	public int deleteRecords(String sheetId)
			throws ServiceException {

		try {
			Map<String, Object> param = super.createSqlParam();
			param.put(EstimateLineService.Param.ESTIMATE_SHEET_ID, sheetId);
			return this.updateBySqlFile(
					"estimate/DeleteEstimateLinesBySheetId.sql", param)
					.execute();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 複数の見積伝票行IDを指定して見積伝票明細行情報を削除します.
	 *
	 * @param ids 見積伝票行IDの配列
	 * @return 削除件数
	 * @throws ServiceException
	 */
	protected int deleteRecordsByLineId(String[] ids)
			throws ServiceException {

		try {
			Map<String, Object> param = super.createSqlParam();
			param.put(EstimateLineService.Param.ESTIMATE_LINE_IDS, ids);
			return this.updateBySqlFile(
					"estimate/DeleteEstimateLinesByLineIds.sql", param)
					.execute();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 検索条件パラメータを設定して返します.
	 * @param entity 見積伝票明細行エンティティ
	 * @return 検索条件パラメータ
	 */
	protected Map<String, Object> setEntityToParam(EstimateLineTrn entity) {
		Map<String, Object> param = super.createSqlParam();

		param.put(EstimateLineService.Param.ESTIMATE_LINE_ID,
				entity.estimateLineId);
		param.put(EstimateLineService.Param.ESTIMATE_SHEET_ID,
				entity.estimateSheetId);
		param.put(EstimateLineService.Param.LINE_NO, entity.lineNo);
		param.put(EstimateLineService.Param.PRODUCT_CODE, entity.productCode);
		param.put(EstimateLineService.Param.CUSTOMER_PCODE,
				entity.customerPcode);
		param.put(EstimateLineService.Param.PRODUCT_ABSTRACT,
				entity.productAbstract);
		param.put(EstimateLineService.Param.QUANTITY, entity.quantity);
		param.put(EstimateLineService.Param.UNIT_COST, entity.unitCost);
		param.put(EstimateLineService.Param.UNIT_RETAIL_PRICE,
				entity.unitRetailPrice);
		param.put(EstimateLineService.Param.COST, entity.cost);
		param.put(EstimateLineService.Param.RETAIL_PRICE, entity.retailPrice);
		param.put(EstimateLineService.Param.REMARKS, entity.remarks);

		return param;

	}

	/**
	 * 見積伝票番号を指定して、見積伝票明細行情報を取得します.
	 *
	 * @param estimateSheetId 見積番号
	 * @return 見積伝票明細行BeanMapリスト
	 * @throws ServiceException
	 */
	public List<BeanMap> findEstimateLinesBySheetIdSimple(
			String estimateSheetId) throws ServiceException {
		try {
			Map<String, Object> param = super.createSqlParam();
			param.put(EstimateLineService.Param.ESTIMATE_SHEET_ID,
					estimateSheetId);
			return this.selectBySqlFile(
					BeanMap.class,
					"estimate/FindEstimateLinesBySheetId.sql", param)
					.getResultList();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 見積伝票番号を指定して、見積伝票明細行情報を取得します.
	 *
	 * @param estimateLineIds 見積番号
	 * @return 見積伝票明細行DTOリスト
	 * @throws ServiceException
	 */
	public List<InputEstimateLineDto> findEstimateLinesByLineIds(
			String estimateLineIds) throws ServiceException {
		try {
			Map<String, Object> param = super.createSqlParam();

			param.put(EstimateLineService.Param.ESTIMATE_LINE_IDS,
					estimateLineIds);

			List<EstimateLineTrn> resultList = this.selectBySqlFile(
					EstimateLineTrn.class,
					"estimate/FindEstimateLinesByLineIds.sql", param)
					.getResultList();

			List<InputEstimateLineDto> dtoList = new ArrayList<InputEstimateLineDto>();
			for (EstimateLineTrn entity : resultList) {
				InputEstimateLineDto dto = new InputEstimateLineDto();
				Beans.copy(entity, dto).execute();
				dtoList.add(dto);
			}
			return dtoList;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 見積伝票DTOを指定して、見積伝票明細行情報を取得します.
	 *
	 * @param dto 見積伝票番号
	 * @return 見積伝票明細行DTOリスト
	 * @throws ServiceException
	 */
	@Override
	public List<InputEstimateLineDto> loadBySlip(InputEstimateDto dto) throws ServiceException {
		try {
			Map<String, Object> param = super.createSqlParam();

			param.put(EstimateLineService.Param.ESTIMATE_SHEET_ID, dto.estimateSheetId);

			List<EstimateLineProductJoin> resultList = this.selectBySqlFile(
					EstimateLineProductJoin.class,
					"estimate/FindEstimateLinesBySheetId.sql", param)
					.getResultList();

			List<InputEstimateLineDto> dtoList = new ArrayList<InputEstimateLineDto>();
			for (EstimateLineProductJoin entity : resultList) {
				InputEstimateLineDto lineDto = new InputEstimateLineDto();
				Beans.copy(entity, lineDto).execute();
				dtoList.add(lineDto);
			}
			return dtoList;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 見積伝票明細行の新規登録・更新処理を行います.
	 * @param slipDto 見積伝票DTO
	 * @param lineList 見積伝票明細行DTOリスト
	 * @param deletedLineIds 削除対象見積伝票明細行ID文字列
	 * @param abstractServices 保存で使用するサービス
	 * @throws ServiceException
	 */
	@Override
	public void save(InputEstimateDto slipDto,
			List<InputEstimateLineDto> lineList, String deletedLineIds, AbstractService<?>... abstractServices)
			throws ServiceException {
		try {
			if (lineList != null && lineList.size() > 0) {
				short i = 1;
				for (InputEstimateLineDto dto : lineList) {

					// 見積番号を明細に設定する。
					dto.estimateSheetId = slipDto.getKeyValue();

					EstimateLineTrn entity = Beans.createAndCopy(
							EstimateLineTrn.class, dto).dateConverter(
							Constants.FORMAT.TIMESTAMP, "updDatetm").execute();

					entity.lineNo = i++;
					if (dto.estimateLineId == null
							|| dto.estimateLineId.length() == 0) {
						// 見積伝票明細番号を採番
						dto.estimateLineId = Long
								.toString(seqMakerService
										.nextval(EstimateLineService.Table.LINE_TABLE_NAME));
						entity.estimateLineId = Integer
								.parseInt(dto.estimateLineId);
						insertRecord(entity);
					} else {
						updateRecord(entity);
					}
				}

			}

			if (deletedLineIds != null && deletedLineIds.length() > 0) {

				String[] ids = deletedLineIds.split(",");
				super.updateAudit(ids);
				deleteRecordsByLineId(ids);
			}

		} catch (NumberFormatException e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * キーカラム名を返します.
	 * @return 見積伝票明細行テーブルのキーカラム名
	 */
	@Override
	protected String[] getKeyColumnNames() {
		return new String[] { "ESTIMATE_SHEET_ID", "ESTIMATE_LINE_ID" };
	}

	/**
	 * テーブル名を返します.
	 * @return 見積伝票明細行テーブル名
	 */
	@Override
	protected String getTableName() {
		return "ESTIMATE_LINE_TRN";
	}
}
