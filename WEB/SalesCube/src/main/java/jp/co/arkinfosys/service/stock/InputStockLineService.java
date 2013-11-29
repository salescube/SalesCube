/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.service.stock;

import java.util.List;

import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.dto.stock.EadLineTrnDto;
import jp.co.arkinfosys.dto.stock.EadSlipTrnDto;
import jp.co.arkinfosys.entity.EadLineTrn;
import jp.co.arkinfosys.entity.Rack;
import jp.co.arkinfosys.service.AbstractService;
import jp.co.arkinfosys.service.EadService;
import jp.co.arkinfosys.service.exception.ServiceException;

import org.seasar.framework.beans.util.Beans;

/**
 *
 * 入出庫入力明細行サービスクラスです.
 * @author Ark Information Systems
 *
 */
public class InputStockLineService extends CommonInputStockLineService {

	/**
	 * 入出庫入力明細行を削除します.<br>
	 * 未実装です.
	 * @param ids 削除対象明細行ID
	 * @return 0
	 * @throws ServiceException
	 */
	@Override
	protected int deleteRecordsByLineId(String[] ids) throws ServiceException {
		return 0;
	}

	/**
	 * 入出庫入力明細行を登録します.
	 * @param entity 入出庫入力明細行エンティティ
	 * @return 登録件数
	 * @throws ServiceException
	 */
	@Override
	protected int insertRecord(EadLineTrn entity) throws ServiceException {
		return eadService.insertLine(entity);
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
	 * 入出庫明細行を登録します.
	 * @param slipDto 入出庫伝票DTO
	 * @param lineList 入出庫伝票明細行DTOリスト
	 * @param deletedLineIds 削除対象入出庫明細行ID文字列
	 * @param abstractServices 保存で使用するサービス
	 * @throws ServiceException
	 */
	@Override
	public void save(EadSlipTrnDto slipDto, List<EadLineTrnDto> lineList,
			String deletedLineIds
			, AbstractService<?>... abstractServices) throws ServiceException {
		try {
			if (lineList != null && lineList.size() > 0) {
				short i = 1;
				for (EadLineTrnDto dto : lineList) {

					// 入出庫伝票番号を明細に設定する。
					dto.eadSlipId = slipDto.getKeyValue();

					// 棚情報を取得する
					Rack rack = rackService.findById(dto.rackCode);
					dto.rackName = rack.rackName;

					EadLineTrn entity = Beans.createAndCopy(EadLineTrn.class,
							dto).dateConverter(Constants.FORMAT.TIMESTAMP,
							"updDatetm").execute();

					entity.lineNo = i++;
					if (dto.eadLineId == null || dto.eadLineId.length() == 0) {
						// 入出庫伝票明細番号を採番
						dto.eadLineId = Long.toString(seqMakerService
								.nextval(EadService.Table.EAD_LINE_TRN));
						entity.eadLineId = Integer.parseInt(dto.eadLineId);

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
	 * 入出庫明細行を更新します.<br>
	 * 未実装です.
	 * @param entity 入出庫明細行エンティティ
	 * @return 更新件数
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
