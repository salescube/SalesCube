/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.dto.master;

import java.io.Serializable;

/**
 * レートデータ情報を管理するDTOクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class RateTrnDto implements Serializable, MasterEditDto {

	private static final long serialVersionUID = 1L;

	/** レートタイプID */
	public String rateId;

	/** 適用開始日 */
	public String startDate;

	/** レート */
	public String rate;

	/** レートデータ備考 */
	public String remarks;

	/** 更新日 */
	public String updDatetm;

	/**
	 * レートタイプIDと適用開始日を取得します.
	 * @return　 文字列の配列　[レートタイプID,適用開始日]
	 */
	@Override
	public String[] getKeys() {
		return new String[] { this.rateId, this.startDate };
	}

}
