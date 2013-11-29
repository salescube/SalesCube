/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.dto.master;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.seasar.struts.annotation.Required;

/**
 * レート情報を管理するDTOクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class RateDto implements Serializable, MasterEditDto {

	private static final long serialVersionUID = 1L;

	/** レートタイプID */
	public String rateId;

	/** レートタイプ名称 */
	@Required
	public String name;

	/** レートタイプ備考 */
	public String remarks;

	public String startDate;
	public String startDate1;
	public String startDate2;
	public String rate;
	public String sign;
	public String updDatetm;

	/** 削除された行 */
	public String deletedRateId;

	public List<RateTrnDto> rateTrnList = new ArrayList<RateTrnDto>();

	/**
	 * レートタイプIDを取得します.
	 * @return　 レートタイプID
	 */
	@Override
	public String[] getKeys() {
		return new String[] { this.rateId };
	}
}
