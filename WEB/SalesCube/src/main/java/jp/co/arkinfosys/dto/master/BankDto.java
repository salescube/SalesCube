/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.dto.master;

/**
 * 銀行マスタのDTOクラスです.
 *
 * @author Ark Information Systems
 */
public class BankDto implements MasterEditDto {

	/** マスタID */
	public String bankId;

	/** 銀行コード */
	public String bankCode;

	/** 銀行名 */
	public String bankName;

	/** 店番 */
	public String storeCode;

	/** 店名 */
	public String storeName;

	/** 科目 */
	public String dwbType;

	/** 科目名 */
	public String dwbName;

	/** 口座番号 */
	public String accountNum;

	/** 口座名義 */
	public String accountOwnerName;

	/** 口座名義カナ */
	public String accountOwnerKana;

	/** 備考 */
	public String remarks;

	/** 有効 */
	public String valid;


	/** 更新日時（排他制御用）*/
	public String updDatetm;

	/**
	 * 銀行マスタIDを取得します.
	 * @return　銀行マスタID
	 */
	@Override
	public String[] getKeys() {
		return new String[] { this.bankId };
	}
}
