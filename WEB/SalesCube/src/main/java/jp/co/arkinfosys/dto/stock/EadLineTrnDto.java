/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.dto.stock;

import java.io.Serializable;
import java.sql.Timestamp;

import jp.co.arkinfosys.common.StringUtil;
import jp.co.arkinfosys.dto.AbstractLineDto;

/**
 * 入出庫伝票明細行情報を管理するDTOクラスです.
 * @author Ark Information Systems
 *
 */
public class EadLineTrnDto extends AbstractLineDto implements Serializable {

	private static final long serialVersionUID = 1L;

	public String eadLineId;

	public String eadSlipId;

	//public String eadLineNo;
	//public String lineNo;

	public String productCode;

	public String productAbstract;

	public String rackCode;

	public String rackName;

	public String rackCodeDest;

	public String rackNameDest;

	public String quantity;

	public String remarks;

	public String salesLineId;

	public String supplierLineId;

	public String creFunc;

	public Timestamp creDatetm;

	public String creUser;

	public String updFunc;

	public Timestamp updDatetm;

	public String updUser;

	public String stockCount;

	public String movableStockCount;

	public String updateQuantity;

	public String quantitySrc;
	public String quantityDest;

	public String productRemarks;

	/**
	 * 空行かどうか判定します.
	 * @return true空行　false:空行でない
	 */
	public boolean isEmpty() {
		if (StringUtil.hasLength(productCode) || StringUtil.hasLength(rackCode)
				|| StringUtil.hasLength(rackCodeDest)
				|| StringUtil.hasLength(quantity)
				|| StringUtil.hasLength(remarks)) {
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
		if (StringUtil.hasLength(productCode)) {
			return false;
		}
		return true;
	}

	/**
	 * 出庫明細行情報のDTOから入庫明細行情報のDTOを作成します.
	 * @return 入庫明細行情報のDTO
	 */
	public EadLineTrnDto createStockDto() {
		EadLineTrnDto stockLineDto = new EadLineTrnDto();

		// 共通
		stockLineDto.eadLineId = this.eadLineId;
		stockLineDto.eadSlipId = this.eadSlipId;
		//stockLineDto.eadLineNo = this.eadLineNo;
		stockLineDto.lineNo = this.lineNo;
		stockLineDto.productCode = this.productCode;
		stockLineDto.productAbstract = this.productAbstract;
		stockLineDto.rackCode = this.rackCode;
		stockLineDto.rackName = this.rackName;
		stockLineDto.rackCodeDest = this.rackCodeDest;
		stockLineDto.rackNameDest = this.rackNameDest;
		stockLineDto.quantity = this.quantity;
		stockLineDto.remarks = this.remarks;
		stockLineDto.salesLineId = this.salesLineId;
		stockLineDto.supplierLineId = this.supplierLineId;
		stockLineDto.creFunc = this.creFunc;
		stockLineDto.creDatetm = this.creDatetm;
		stockLineDto.creUser = this.creUser;
		stockLineDto.updFunc = this.updFunc;
		stockLineDto.updDatetm = this.updDatetm;
		stockLineDto.updUser = this.updUser;
		stockLineDto.stockCount = this.stockCount;
		stockLineDto.movableStockCount = this.movableStockCount;
		stockLineDto.updateQuantity = this.updateQuantity;
		stockLineDto.quantitySrc = this.quantitySrc;
		stockLineDto.quantityDest = this.quantityDest;
		stockLineDto.productRemarks = this.productRemarks;

		return stockLineDto;
	}

}