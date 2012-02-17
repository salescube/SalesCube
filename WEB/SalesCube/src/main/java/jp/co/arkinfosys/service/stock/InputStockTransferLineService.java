/*
 *  Copyright 2009-2010 Ark Information Systems.
 */

package jp.co.arkinfosys.service.stock;

import java.util.List;

import jp.co.arkinfosys.common.StringUtil;
import jp.co.arkinfosys.dto.stock.EadLineTrnDto;
import jp.co.arkinfosys.dto.stock.EadSlipTrnDto;
import jp.co.arkinfosys.entity.EadLineTrn;
import jp.co.arkinfosys.entity.Product;
import jp.co.arkinfosys.entity.Rack;
import jp.co.arkinfosys.service.AbstractService;
import jp.co.arkinfosys.service.EadService;
import jp.co.arkinfosys.service.exception.ServiceException;

import org.seasar.framework.beans.Converter;
import org.seasar.framework.beans.util.Beans;

/**
 *
 * 在庫移動入力明細行サービスクラスです.
 *
 */
public class InputStockTransferLineService extends CommonInputStockLineService {
	/**
	 * 在庫移動の新規登録処理を行います.
	 * <p>
	 * 在庫移動では対となる出庫伝票と入庫伝票が同時に作成されます.
	 * </p>
	 *
	 * @param slipDto 入出庫伝票DTO
	 * @param lineList 入出庫伝票明細行リスト
	 * @param deletedLineIds 削除対象入出庫伝票明細行ID文字列
	 * @param abstractServices 追加サービス
	 * @throws ServiceException
	 */
	@Override
	public void save(EadSlipTrnDto slipDto,List<EadLineTrnDto> lineList,String deletedLineIds
			, AbstractService<?>... abstractServices)
			throws ServiceException {
		if (lineList != null && lineList.size() > 0) {
			
			Integer lineNo = 0;
			for (EadLineTrnDto deliveryLineDto : lineList) {
				
				if (!StringUtil.hasLength(deliveryLineDto.productCode)) {
					continue;
				}

				

				
				deliveryLineDto.eadLineId = Long.toString(seqMakerService
						.nextval(EadService.Table.EAD_LINE_TRN));
				
				deliveryLineDto.eadSlipId = slipDto.eadSlipId;

				
				Product product = productService
						.findById(deliveryLineDto.productCode);

				
				Rack deliveryRack = rackService.findById(deliveryLineDto.rackCode);

				
				lineNo++;
				
				deliveryLineDto.lineNo = (lineNo).toString();

				
				deliveryLineDto.productAbstract = product.productName;

				
				Converter conv = createProductNumConverter();
				Number num = (Number) conv.getAsObject(deliveryLineDto.quantity);
				deliveryLineDto.quantity = num.toString();

				
				deliveryLineDto.rackName = deliveryRack.rackName;

				
				deliveryLineDto.salesLineId = null;

				
				deliveryLineDto.supplierLineId = null;

				
				EadLineTrn deliveryLineTrn = Beans.createAndCopy(EadLineTrn.class,
						deliveryLineDto).execute();

				
				eadService.insertLine(deliveryLineTrn);



				

				
				EadSlipTrnDto stockDto = slipDto.createStockDto();

				
				EadLineTrnDto stockLineDto = deliveryLineDto.createStockDto();

				
				stockLineDto.eadLineId = Long.toString(seqMakerService
						.nextval(EadService.Table.EAD_LINE_TRN));
				
				stockLineDto.eadSlipId = stockDto.eadSlipId;

				
				Rack stockRack = rackService.findById(stockLineDto.rackCodeDest);

				
				stockLineDto.rackCode = stockLineDto.rackCodeDest;

				
				stockLineDto.rackName = stockRack.rackName;

				
				EadLineTrn stockLineTrn = Beans.createAndCopy(EadLineTrn.class,
						stockLineDto).execute();

				
				eadService.insertLine(stockLineTrn);
			}
		}
	}

	/**
	 * 入出庫伝票明細行を削除します.<br>
	 * 未実装です.
	 * @param ids 削除対象の入出庫伝票明細行ID
	 * @return 0
	 * @throws ServiceException
	 */
	@Override
	protected int deleteRecordsByLineId(String[] ids) throws ServiceException {
		return 0;
	}

	/**
	 * 入出庫伝票明細行を登録します.<br>
	 * 未実装です.
	 * @param entity 入出庫伝票明細行エンティティ
	 * @return 0
	 * @throws ServiceException
	 */
	@Override
	protected int insertRecord(EadLineTrn entity) throws ServiceException {
		return 0;
	}

	/**
	 * 入出庫伝票番号を指定して明細行情報を取得します.<br>
	 * 未実装です.
	 * @param dto 入出庫伝票DTO
	 * @return null
	 * @throws ServiceException
	 */
	@Override
	public List<EadLineTrnDto> loadBySlip(EadSlipTrnDto dto)
			throws ServiceException {
		return null;
	}

	/**
	 * 入出庫伝票明細行を更新します.<br>
	 * 未実装です.
	 * @param entity 入出庫伝票明細行エンティティ
	 * @return 0
	 * @throws ServiceException
	 */
	@Override
	protected int updateRecord(EadLineTrn entity) throws ServiceException {
		return 0;
	}

	/**
	 * キーカラム名を返します.
	 * @return 入出庫伝票明細行テーブルのキーカラム名
	 */
	@Override
	protected String[] getKeyColumnNames() {
		return new String[] { "EAD_SLIP_ID", "EAD_LINE_ID" };
	}

	/**
	 * テーブル名を返します.
	 * @return 入出庫伝票明細行テーブル名
	 */
	@Override
	protected String getTableName() {
		return "EAD_LINE_TRN";
	}

}
