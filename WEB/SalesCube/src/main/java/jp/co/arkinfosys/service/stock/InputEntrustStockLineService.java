/*
 *  Copyright 2009-2010 Ark Information Systems.
 */

package jp.co.arkinfosys.service.stock;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import jp.co.arkinfosys.common.CategoryTrns;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.dto.stock.EntrustEadLineTrnDto;
import jp.co.arkinfosys.dto.stock.EntrustEadSlipTrnDto;
import jp.co.arkinfosys.entity.EntrustEadLineTrn;
import jp.co.arkinfosys.entity.EntrustEadSlipTrn;
import jp.co.arkinfosys.entity.Product;
import jp.co.arkinfosys.entity.join.ProductJoin;
import jp.co.arkinfosys.service.AbstractLineService;
import jp.co.arkinfosys.service.AbstractService;
import jp.co.arkinfosys.service.EntrustEadService;
import jp.co.arkinfosys.service.PoSlipService;
import jp.co.arkinfosys.service.ProductService;
import jp.co.arkinfosys.service.SeqMakerService;
import jp.co.arkinfosys.service.exception.ServiceException;

import org.seasar.framework.beans.Converter;
import org.seasar.framework.beans.util.Beans;

/**
 * 委託入出庫伝票明細行サービスクラスです.
 * @author Ark Information Systems
 *
 */
public class InputEntrustStockLineService extends AbstractLineService<EntrustEadLineTrn,EntrustEadLineTrnDto,EntrustEadSlipTrnDto> {

	/**
	 * パラメータ定義クラスです.
	 */
	public static class Param {
		public static final String ENTRUST_EAD_SLIP_ID = "entrustEadSlipId";
		public static final String ENTRUST_EAD_CATEGORY = "entrustEadCategory";
		public static final String REMARKS = "remarks";
		public static final String ENTRUST_EAD_LINE_ID = "entrustEadLineId";
		public static final String LINE_NO = "lineNo";
		public static final String PRODUCT_CODE = "productCode";
		public static final String PRODUCT_ABSTRACT = "productAbstract";
		public static final String QUANTITY = "quantity";
		public static final String PO_LINE_ID = "poLineId";
		public static final String REL_ENTRUST_EAD_LINE_ID = "relEntrustEadLineId";
	}

	@Resource
	protected EntrustEadService entrustEadService;

    @Resource
    protected PoSlipService poSlipService;

    @Resource
    protected ProductService productService;

	@Resource
	protected SeqMakerService seqMakerService;

	/**
	 * 委託入出庫伝票番号を指定して明細情報を削除します.
	 *
	 * @param id 委託入出庫伝票番号
	 * @return 削除件数
	 * @throws ServiceException
	 */
	@Override
	public int deleteRecords(String id) throws ServiceException {
		try {
			
			List<EntrustEadLineTrn> eadLineTrnList = entrustEadService.findLineByEntrustEadSlipId(Integer.valueOf(id));
			List<EntrustEadLineTrnDto> entrustEadLineTrnDtoList = new ArrayList<EntrustEadLineTrnDto>();
			Converter conv = createProductNumConverter();
			for (EntrustEadLineTrn eadLineTrn : eadLineTrnList) {
				
				EntrustEadLineTrnDto entrustEadLineTrnDto = Beans.createAndCopy(
						EntrustEadLineTrnDto.class, eadLineTrn).converter(conv, EntrustEadService.Param.QUANTITY).execute();

				
				ProductJoin pj = productService.findById(eadLineTrn.productCode);
				if( pj != null ) {
					entrustEadLineTrnDto.productRemarks = pj.remarks;
				}

				entrustEadLineTrnDtoList.add(entrustEadLineTrnDto);
			}

			
			for (EntrustEadLineTrnDto entrustEadLineTrnDto : entrustEadLineTrnDtoList) {
				entrustEadService.deleteLineByEntrustEadLineId(Integer.parseInt(entrustEadLineTrnDto.entrustEadLineId));
				entrustEadService.updateRelEentrustEadLineIdByPoLineId(Integer.parseInt(entrustEadLineTrnDto.poLineId));

				
				if(CategoryTrns.ENTRUST_EAD_CATEGORY_ENTER.equals(entrustEadLineTrnDto.entrustEadCategory)) {
					
					poSlipService.updatePOrderLineTrnStatusByPoLineId(entrustEadLineTrnDto.poLineId, Constants.STATUS_PORDER_LINE.ORDERED );
				} else {
					
					poSlipService.updatePOrderLineTrnStatusByPoLineId(entrustEadLineTrnDto.poLineId, Constants.STATUS_PORDER_LINE.ENTRUST_STOCK_MAKED );
				}
			}
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			ServiceException se = new ServiceException(e);
			se.setStopOnError(true);
			throw se;
		}
		return 0;
	}

	/**
	 * 複数の明細番号を指定して明細情報を削除します.<br>
	 * 未実装です.
	 *
	 * @param ids 明細番号の配列
	 * @return 削除件数
	 * @throws ServiceException
	 */
	@Override
	protected int deleteRecordsByLineId(String[] ids) throws ServiceException {
		return 0;
	}

	/**
	 * 委託入出庫伝票番号を指定して明細行情報を取得します.<br>
	 * 未実装です.
	 *
	 * @param dto 委託入出庫伝票
	 * @return 委託入出庫伝票明細行リスト
	 * @throws ServiceException
	 */
	@Override
	public List<EntrustEadLineTrnDto> loadBySlip(EntrustEadSlipTrnDto dto)
			throws ServiceException {
		return null;
	}

	/**
	 * 委託入出庫明細行を登録します.
	 * @param slipDto 委託入出庫伝票DTO
	 * @param lineList 委託入出庫伝票明細行DTOリスト
	 * @param deletedLineIds 削除対象委託入出庫明細行ID文字列
	 * @param abstractServices 保存で使用するサービス
	 * @throws ServiceException
	 */
	@Override
	public void save(EntrustEadSlipTrnDto slipDto,
			List<EntrustEadLineTrnDto> lineList, String deletedLineIds
			, AbstractService<?>... abstractServices)
			throws ServiceException {
		try {
			String firstEntrnstEadLineId = ((EntrustEadLineTrnDto)lineList.get(0)).entrustEadLineId;
			if (firstEntrnstEadLineId == null || firstEntrnstEadLineId.length() == 0) {
				
				Integer lineNo = 0;
				for (EntrustEadLineTrnDto entrustEadLineTrnDto : lineList) {
					
					if ( entrustEadLineTrnDto.checkEadLine == null ) {
						continue;
					}

					if (entrustEadLineTrnDto.entrustEadLineId == null || entrustEadLineTrnDto.entrustEadLineId.length() == 0)
					{
					
					entrustEadLineTrnDto.entrustEadLineId = Long.toString(seqMakerService
							.nextval(EntrustEadService.Table.ENTRUST_EAD_LINE_TRN));
					
					entrustEadLineTrnDto.entrustEadSlipId = slipDto.entrustEadSlipId;

					
					Product product = productService
							.findById(entrustEadLineTrnDto.productCode);

					
					entrustEadLineTrnDto.lineNo = (++lineNo).toString();

					
					entrustEadLineTrnDto.productAbstract = product.productName;

					
					entrustEadLineTrnDto.entrustEadCategory = slipDto.entrustEadCategory;

					
					Converter conv = createProductNumConverter();
					Number num = (Number) conv.getAsObject(entrustEadLineTrnDto.quantity);
					entrustEadLineTrnDto.quantity = num.toString();
					}else{

					}
				}

				
				insertLine(slipDto);

			}else{
				
				for (EntrustEadLineTrnDto entrustEadLineTrnDto : lineList) {

					EntrustEadLineTrn entrustEadLineTrn = Beans.createAndCopy(EntrustEadLineTrn.class,
							entrustEadLineTrnDto).execute();

					entrustEadService.updateLineByEntrustEadLineId(entrustEadLineTrn);
				}
			}
		} catch (Exception e) {
			ServiceException se = new ServiceException(e);
			se.setStopOnError(true);
			throw se;
		}

	}

	/**
	 * 委託入出庫伝票明細行を登録します.
	 * @param entity 委託入出庫伝票明細行エンティティ
	 * @return 登録件数
	 * @throws ServiceException
	 */
	@Override
	protected int insertRecord(EntrustEadLineTrn entity)
			throws ServiceException {
		try {
			
			Map<String, Object> param = createLineSqlParam(entity);
			return this.updateBySqlFile("entrustead/InsertLine.sql", param).execute();
		} catch (Exception e) {
			ServiceException se = new ServiceException(e);
			se.setStopOnError(true);
			throw se;
		}
	}

	/**
	 * 委託入出庫伝票明細行を更新します.
	 * @param entity 委託入出庫伝票明細行エンティティ
	 * @return 更新件数
	 * @throws ServiceException
	 */
	@Override
	protected int updateRecord(EntrustEadLineTrn entity)
			throws ServiceException {
		try {
			
			Map<String, Object> param = createLineSqlParam(entity);
			return this.updateBySqlFile("entrustead/UpdateLine.sql", param).execute();
		} catch (Exception e) {
			ServiceException se = new ServiceException(e);
			se.setStopOnError(true);
			throw se;
		}
	}

	/**
	 * 明細作成時のSQLパラメータを作成します.
	 * @param entrustEadLineTrn 委託入出庫伝票明細行エンティティ
	 * @return 設定済のSQLパラメータ
	 */
	protected Map<String, Object> createLineSqlParam(EntrustEadLineTrn entrustEadLineTrn) {
		Map<String, Object> param = super.createSqlParam();
		param.put(Param.ENTRUST_EAD_LINE_ID, entrustEadLineTrn.entrustEadLineId);
		param.put(Param.ENTRUST_EAD_SLIP_ID, entrustEadLineTrn.entrustEadSlipId);
		param.put(Param.LINE_NO, entrustEadLineTrn.lineNo);
		param.put(Param.PRODUCT_CODE, entrustEadLineTrn.productCode);
		param.put(Param.PRODUCT_ABSTRACT, entrustEadLineTrn.productAbstract);
		param.put(Param.QUANTITY, entrustEadLineTrn.quantity);
		param.put(Param.REMARKS, entrustEadLineTrn.remarks);
		param.put(Param.PO_LINE_ID, entrustEadLineTrn.poLineId);
		param.put(Param.REL_ENTRUST_EAD_LINE_ID, entrustEadLineTrn.relEntrustEadLineId);
		param.put(Param.ENTRUST_EAD_CATEGORY, entrustEadLineTrn.entrustEadCategory);
		return param;
	}

	/**
	 * 委託入庫明細、委託出庫明細に、関連委託入出庫伝票行IDを設定します.
	 * @param poLineId 発注伝票行ID
	 * @throws ServiceException
	 */
	public void updateRelEentrustEadLineIdByPoLineId( Integer poLineId ) throws ServiceException  {
		try {
			
			Map<String, Object> param = super.createSqlParam();
			param.put(Param.PO_LINE_ID, poLineId );
			this.updateBySqlFile("entrustead/UpdateRelEentrustEadLineIdByPoLineId.sql", param).execute();

		} catch (Exception e) {
			ServiceException se = new ServiceException(e);
			se.setStopOnError(true);
			throw se;
		}
	}
	/**
	 * 委託入出庫伝票明細行を登録します.
	 * @param entrustEadSlipTrnDto 委託入出庫伝票DTO
	 * @throws ServiceException
	 */
	public void insertLine(EntrustEadSlipTrnDto entrustEadSlipTrnDto)
			throws ServiceException {
		try {
			
			for (EntrustEadLineTrnDto entrustEadLineTrnDto : entrustEadSlipTrnDto.getLineDtoList()) {
				
				if ( entrustEadLineTrnDto.checkEadLine == null ) {
					continue;
				}
				EntrustEadLineTrn entrustEadLineTrn = Beans.createAndCopy(EntrustEadLineTrn.class,
						entrustEadLineTrnDto).execute();

				
				insertRecord(entrustEadLineTrn);

				
				if( CategoryTrns.ENTRUST_EAD_CATEGORY_DISPATCH.equals(entrustEadLineTrnDto.entrustEadCategory) ) {
					updateRelEentrustEadLineIdByPoLineId(entrustEadLineTrn.poLineId );
				}

				
				if(CategoryTrns.ENTRUST_EAD_CATEGORY_ENTER.equals(entrustEadLineTrnDto.entrustEadCategory)) {
					
					poSlipService.updatePOrderLineTrnStatusByPoLineId(entrustEadLineTrnDto.poLineId, Constants.STATUS_PORDER_LINE.ENTRUST_STOCK_MAKED , entrustEadLineTrnDto.quantity);
				} else {
					
					poSlipService.updatePOrderLineTrnStatusByPoLineId(entrustEadLineTrnDto.poLineId, Constants.STATUS_PORDER_LINE.ENTRUST_STOCK_DELIVERED , entrustEadLineTrnDto.quantity);
				}
			}

			
			poSlipService.updatePOrderTrnStatusByPoSlipId(entrustEadSlipTrnDto.poSlipId);

		} catch (Exception e) {
			ServiceException se = new ServiceException(e);
			se.setStopOnError(true);
			throw se;
		}
	}
	/**
	 * 委託入出庫伝票番号から委託入出庫伝票DTOを作成します.
	 * @param entrustEadSlipId 委託入出庫伝票番号
	 * @return EadSlipTrnDto 委託入出庫伝票DTO
	 * @throws ServiceException
	 */
	public EntrustEadSlipTrnDto createEntrustEadSlipTrnDto(String entrustEadSlipId)
			throws ServiceException {
		try {
			
			try {
				Integer.valueOf(entrustEadSlipId);
			} catch (NumberFormatException e) {
				return null;
			}

			
			EntrustEadSlipTrn entrustEadSlipTrn = entrustEadService.findSlipByEntrustEadSlipId(Integer.valueOf(entrustEadSlipId));
			List<EntrustEadLineTrn> eadLineTrnList = entrustEadService.findLineByEntrustEadSlipId(Integer.valueOf(entrustEadSlipId));
			if (entrustEadSlipTrn == null
					|| (eadLineTrnList == null || eadLineTrnList.size() == 0)) {
				
				return null;
			}

			
			EntrustEadSlipTrnDto entrustEadSlipTrnDto = Beans.createAndCopy(
					EntrustEadSlipTrnDto.class, entrustEadSlipTrn).execute();
			List<EntrustEadLineTrnDto> entrustEadLineTrnDtoList = new ArrayList<EntrustEadLineTrnDto>();
			Converter conv = createProductNumConverter();
			for (EntrustEadLineTrn eadLineTrn : eadLineTrnList) {
				
				EntrustEadLineTrnDto entrustEadLineTrnDto = Beans.createAndCopy(
						EntrustEadLineTrnDto.class, eadLineTrn).converter(conv, EntrustEadService.Param.QUANTITY).execute();

				
				ProductJoin pj = productService.findById(eadLineTrn.productCode);
				if( pj != null ) {
					entrustEadLineTrnDto.productRemarks = pj.remarks;
				}

				entrustEadLineTrnDtoList.add(entrustEadLineTrnDto);
			}
			entrustEadSlipTrnDto.setLineDtoList(entrustEadLineTrnDtoList) ;

			return entrustEadSlipTrnDto;
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			ServiceException se = new ServiceException(e);
			se.setStopOnError(true);
			throw se;
		}
	}

	/**
	 * キーカラム名を返します.
	 * @return 委託入出庫伝票明細行テーブルのキーカラム名
	 */
	@Override
	protected String[] getKeyColumnNames() {
		return new String[] { "ENTRUST_EAD_SLIP_ID", "ENTRUST_EAD_LINE_ID" };
	}

	/**
	 * テーブル名を返します.
	 * @return 委託入出庫伝票明細行テーブル名
	 */
	@Override
	protected String getTableName() {
		return "ENTRUST_EAD_LINE_TRN";
	}

}
