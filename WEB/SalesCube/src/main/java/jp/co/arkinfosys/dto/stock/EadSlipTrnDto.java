/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.dto.stock;

import java.io.Serializable;

import jp.co.arkinfosys.common.CategoryTrns;
import jp.co.arkinfosys.dto.AbstractLineDto;
import jp.co.arkinfosys.dto.AbstractSlipDto;

/**
 * 入出庫伝票情報を管理するDTOクラスです.
 * @author Ark Information Systems
 *
 */
public class EadSlipTrnDto extends AbstractSlipDto<EadLineTrnDto> implements
		Serializable {

	private static final long serialVersionUID = 1L;

	// 新規作成状態の管理フラグ
	public Boolean newData;

	public String eadSlipId;

	public String eadDate;

	public String eadAnnual;

	public String eadMonthly;

	public String eadYm;

	public String userId;

	public String userName;

	public String eadSlipCategory;

	public String eadCategory;

	public String remarks;

	public String srcFunc;

	public String salesSlipId;

	public String supplierSlipId;

	public String moveDepositSlipId;

	public String stockPdate;

	public String creFunc;

	public String creDatetm;

	public String creUser;

	public String updFunc;

	public String updDatetm;

	public String updUser;

	/**
	 * 入出庫伝票明細行を作成します.
	 * @return 入出庫伝票明細行オブジェクト
	 */
	@Override
	public AbstractLineDto createLineDto() {
		return new EadLineTrnDto();
	}

	/**
	 * 入出庫伝票番号を取得します.
	 * @return 入出庫伝票番号
	 */
	@Override
	public String getKeyValue() {
		return eadSlipId;
	}

	/**
	 * 出庫情報のDTOから入庫情報のDTOを作成します.
	 * @return 入庫情報のDTO
	 */
	public EadSlipTrnDto createStockDto() {
		EadSlipTrnDto stockDto = new EadSlipTrnDto();
		// 共通
		stockDto.eadDate = this.eadDate;
		stockDto.eadAnnual = this.eadAnnual;
		stockDto.eadMonthly = this.eadMonthly;
		stockDto.eadYm = this.eadYm;
		stockDto.userId = this.userId;
		stockDto.userName = this.userName;
		stockDto.eadSlipCategory = this.eadSlipCategory;
		stockDto.remarks = this.remarks;
		stockDto.srcFunc = this.srcFunc;
		stockDto.salesSlipId = this.salesSlipId;
		stockDto.supplierSlipId = this.supplierSlipId;
		stockDto.stockPdate = this.stockPdate;
		stockDto.creFunc = this.creFunc;
		stockDto.creDatetm = this.creDatetm;
		stockDto.creUser = this.creUser;
		stockDto.updFunc = this.updFunc;
		stockDto.updDatetm = this.updDatetm;
		stockDto.updUser = this.updUser;

		// 差分
		stockDto.eadSlipId = this.moveDepositSlipId;
		stockDto.moveDepositSlipId = this.eadSlipId;
		stockDto.eadCategory = CategoryTrns.EAD_CATEGORY_ENTER;//入庫

		return stockDto;
	}
}