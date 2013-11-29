/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.dto.deposit;
import java.io.Serializable;

import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;
/**
 * 銀行入金データ取込画面の結果リスト行のDTOクラスです.
 *
 * @author Ark Information Systems
 *
 */
@Component(instance = InstanceType.SESSION)
public class ImportBankDepositResultDto implements Serializable {
	private static final long serialVersionUID = 1L;

    public String status;
    public String depositSlipId;
    public String customer;
    public String paymentDate;
    public String paymentName;;
    public String lastBillPrice;
    public String paymentPrice;
    public String diffPrice;
    public String changeName;
    public String afterChangeName;
}
