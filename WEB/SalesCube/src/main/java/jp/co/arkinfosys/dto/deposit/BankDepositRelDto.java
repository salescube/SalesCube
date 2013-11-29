/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.dto.deposit;
import java.io.Serializable;

/**
 * 銀行入金関連テーブル（BANK_DEPOSIT_REL_）に紐づくDTOクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class BankDepositRelDto implements Serializable {
	private static final long serialVersionUID = 1L;

	public String depositSlipId;
	public String lineNo;
	public String paymentDate;
	public String paymentName;
}

