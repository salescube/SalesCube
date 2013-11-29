/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.dto.payment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 発注伝票情報を管理するDTOクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class PoSlipDto implements Serializable{
	private static final long serialVersionUID = 1L;

	public String poSlipId;
	public String supplierCode;
	public String supplierName;
	public String rateType;
	public String taxShiftCategory;
	public String taxFractCategory;
	public String priceFractCategory;

	public List<PoSlipLineDto> poSlipLineList = new ArrayList<PoSlipLineDto>();

}
