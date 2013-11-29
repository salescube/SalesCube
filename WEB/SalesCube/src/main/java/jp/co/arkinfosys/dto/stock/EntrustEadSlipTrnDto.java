/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.dto.stock;

import jp.co.arkinfosys.dto.AbstractLineDto;
import jp.co.arkinfosys.dto.AbstractSlipDto;


/**
 * 委託入出庫伝票情報を管理するDTOクラスです.
 * @author Ark Information Systems
 *
 */
public class EntrustEadSlipTrnDto extends AbstractSlipDto<EntrustEadLineTrnDto> {

    public String entrustEadSlipId;

    public String entrustEadDate;

    public String entrustEadAnnual;

    public String entrustEadMonthly;

    public String entrustEadYm;

    public String userId;

    public String userName;

    public String entrustEadCategory;

    public String remarks;

    public String poSlipId;

    public String dispatchOrderPrintCount;

    public String supplierCode;

    public String supplierName;

    public String creFunc;

    public String creDatetm;

    public String creUser;

    public String updFunc;

    public String updDatetm;

    public String updUser;

	/**
	 * 委託入出庫伝票明細行を作成します.
	 * @return 委託入出庫伝票明細行オブジェクト
	 */
	@Override
	public AbstractLineDto createLineDto() {
		return new EntrustEadLineTrnDto();
	}

	/**
	 * 委託入出庫伝票番号を取得します.
	 * @return 委託入出庫伝票番号
	 */
	@Override
	public String getKeyValue() {
		return this.entrustEadSlipId;
	}
}