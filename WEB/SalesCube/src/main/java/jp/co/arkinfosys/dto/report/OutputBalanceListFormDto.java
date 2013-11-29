/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.dto.report;

import java.io.Serializable;

import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;

/**
 * 残高一覧表出力画面の情報を管理するDTOクラスです.
 *
 * @author Ark Information Systems
 *
 */
@Component(instance = InstanceType.SESSION)
public class OutputBalanceListFormDto implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
	 * 検索対象
	 */
    public String outputTarget;

    /**
     * 対象年月
     */
    public String targetDate;

    /**
     * 仕入先コード
     */
    public String supplierCode;

    /**
     * 顧客コードFrom
     */
    public String customerCodeFrom;

    /**
     * 顧客コードTo
     */
    public String customerCodeTo;
}
