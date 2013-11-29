/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.dto;

import jp.co.arkinfosys.common.Constants;
/**
 * 検索結果列情報を管理するDTOクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class DetailDispItemDto {

	public String detailId;

	public String target;

	public String itemId;

	public String itemName;

	public Integer seq;

	public String esslFlag;

	public String dispFlag;

	public String sortFlag;

	public String detailFlag;

	public Integer colWidth;

	public String textAlign;

	public Integer formatType;

	public String creFunc;

	public String creDatetm;

	public String creUser;

	public String updFunc;

	public String updDatetm;

	public String updUser;

	/**
	 * 接頭語[明細]をつけた項目名を返します.
	 * @return　接頭語[明細]をつけた項目名
	 */
	public String getPrefixItemName() {
		if (Constants.FLAG.ON.equals(this.detailFlag)) {
			return "[" + Constants.SEARCH_TARGET.LABEL_LINE + "] "
					+ this.itemName;
		}
		return this.itemName;
	}
}
