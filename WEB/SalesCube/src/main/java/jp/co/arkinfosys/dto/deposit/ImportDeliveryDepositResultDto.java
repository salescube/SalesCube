/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.dto.deposit;
import java.io.Serializable;

import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;

/**
 * 配送業者入金データ取込画面の結果リスト行のDTOクラスです.
 *
 * @author Ark Information Systems
 *
 */
@Component(instance = InstanceType.SESSION)
public class ImportDeliveryDepositResultDto implements Serializable {
    private static final long serialVersionUID = 1L;

    public String status;
    public String salesSlipId;
    public String depositSlipId;
    public String deliverySlipId;
    public String customer;
    public String deliveryDate;
    public String productPrice;
    public String salesMoney;
}
