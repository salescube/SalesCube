/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Entity;

/**
 * 自社マスタのエンティティクラスです.
 *
 * @author Ark Information Systems
 *
 */
@Entity
public class Mine implements Serializable {

    private static final long serialVersionUID = 1L;

    public String companyName;

    public String companyKana;

    public String companyAbbr;

    public String companyCeoName;

    public String companyCeoTitle;

    public String companyZipCode;

    public String companyAddress1;

    public String companyAddress2;

    public String companyTel;

    public String companyFax;

    public String companyEmail;

    public String companyWebSite;

    public String cutoffGroup;

    public String closeMonth;

    public String iniPostageType;

    public String targetPostageCharges;

    public String postage;

    public Integer stockHoldDays;

    public String stockHoldTermCalcCategory;

    public Integer minPoLotCalcDays;

    public Integer minPoLotNum;

    public Integer minPoNum;

    public Integer maxPoNumCalcDays;

    public String taxCategory;

    public String priceFractCategory;

    public String productFractCategory;

    public Integer unitPriceDecAlignment;

    public Integer numDecAlignment;

    public Integer passwordValidDays;

    public Integer totalFailCount;

    public Integer passwordHistCount;

    public Integer passwordLength;

    public String passwordCharType;


    public String logoImgPath;

    public String taxShiftCategory;

    public Integer statsDecAlignment;

    public Integer findTermInitDays;

    public String creFunc;

    public Timestamp creDatetm;

    public String creUser;

    public String updFunc;

    public Timestamp updDatetm;

    public String updUser;

    public String taxFractCategory;

    public String deliveryCustId;

    public BigDecimal deficiencyRate;

    public Integer maxEntrustPoTimelag;
}