/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.dto.stock;

import jp.co.arkinfosys.common.StringUtil;
import jp.co.arkinfosys.dto.AbstractLineDto;

/**
 * 委託入出庫明細行情報を管理するDTOクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class EntrustEadLineTrnDto extends AbstractLineDto {

    private static final long serialVersionUID = 1L;

    public String entrustEadLineId;

    public String entrustEadSlipId;

    public String checkEadLine;

    public String productCode;

    public String productAbstract;

    public String supplierPcode;

    public String quantity;

    public String remarks;

    public String poSlipId;

    public String poLineNo;

    public String poLineId;

    public String relEntrustEadLineId;

    public String entrustEadCategory;

    public String productRemarks;

    public String creFunc;

    public String creDatetm;

    public String creUser;

    public String updFunc;

    public String updDatetm;

    public String updUser;

	/**
	 * 空行かどうか判定します.
	 * @return true空行　false:空行でない
	 */
    public boolean isEmpty() {
    	if(StringUtil.hasLength(productCode)
			|| StringUtil.hasLength(quantity)
			|| StringUtil.hasLength(remarks)
		) {
        	return false;
    	}
    	return true;
    }

	/**
	 * 商品コードがnull又は空白かどうか検査します.
	 * @return true:商品コーがnullまたは空白　false:商品コードはnullでも空白でもない
	 */
	@Override
	public boolean isBlank() {
		return (this.productCode == null || this.productCode.length() == 0);
	}
}