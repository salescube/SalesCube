/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.service.stock;

import javax.annotation.Resource;

import jp.co.arkinfosys.dto.stock.EadLineTrnDto;
import jp.co.arkinfosys.dto.stock.EadSlipTrnDto;
import jp.co.arkinfosys.entity.EadLineTrn;
import jp.co.arkinfosys.service.AbstractLineService;
import jp.co.arkinfosys.service.EadService;
import jp.co.arkinfosys.service.ProductService;
import jp.co.arkinfosys.service.RackService;
import jp.co.arkinfosys.service.SeqMakerService;
import jp.co.arkinfosys.service.exception.ServiceException;

/**
 * 入出庫明細行共通サービスクラスです.
 * @author Ark Information Systems
 *
 */
public abstract class CommonInputStockLineService extends AbstractLineService<EadLineTrn, EadLineTrnDto, EadSlipTrnDto> {
	@Resource
	protected EadService eadService;

	@Resource
	protected SeqMakerService seqMakerService;

    @Resource
    protected RackService rackService;

    @Resource
    protected ProductService productService;

    /**
	 * 伝票番号を指定して明細行を削除します.
	 * @param id 伝票番号
	 * @return 削除件数
	 * @throws ServiceException
     */
    @Override
    public int deleteRecords(String id) throws ServiceException{
    	// 明細行を削除する
    	return eadService.deleteLineByEadSlipId(id);
    }
}
