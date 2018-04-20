/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.dto.setting;

import java.io.Serializable;
import java.sql.Timestamp;

import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;

/**
 * 自社情報を管理するDTOクラスです.
 *
 * @author Ark Information Systems
 *
 */
@Component(instance = InstanceType.SESSION)
public class MineDto implements Serializable {

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

    public String unitPriceDecAlignFormat;

    public String numDecAlignFormat;

    public String statsDecAlignFormat;

    public String unitPriceDecAlignFormat4xls;

    public String numDecAlignFormat4xls;

    public String statsDecAlignFormat4xls;

    public String taxFractCategory;

    public String deliveryCustId;

    public String iniPostageType;

    public String targetPostageCharges;

    public String postage;


    /**
     * 数値整形用フォーマットを設定します．
     */
    public void initDecAlignFormat() {
    	// 単価整形用
    	StringBuffer ret = new StringBuffer("###,##0");
    	StringBuffer xls = new StringBuffer("\\#\\,\\#\\#0");
    	if (unitPriceDecAlignment!=null && unitPriceDecAlignment.intValue()>0) {
    		ret.append(".");
    		xls.append("\\.");
    		for (int i=0;i<unitPriceDecAlignment.intValue();i++) {
    			ret.append("0");
    			xls.append("0");
    		}
    	}
    	xls.append("_ ");
    	unitPriceDecAlignFormat = ret.toString();
    	unitPriceDecAlignFormat4xls = xls.toString();

    	// 数量
    	ret = new StringBuffer("###,##0");
    	xls = new StringBuffer("\\#\\,\\#\\#0");
    	if (numDecAlignment!=null && numDecAlignment.intValue()>0) {
    		ret.append(".");
    		xls.append("\\.");
    		for (int i=0;i<numDecAlignment.intValue();i++) {
    			ret.append("0");
    			xls.append("0");
    		}
    	}
    	xls.append("_ ");
    	numDecAlignFormat = ret.toString();
    	numDecAlignFormat4xls = xls.toString();

    	// 統計処理用
    	ret = new StringBuffer("0");
    	xls = new StringBuffer("0");
    	if (statsDecAlignment!=null && statsDecAlignment.intValue()>0) {
    		ret.append(".");
    		xls.append("\\.");
    		for (int i=0;i<statsDecAlignment.intValue();i++) {
    			ret.append("0");
    			xls.append("0");
    		}
    	}
    	xls.append("_ ");

    	// 2010.04.22 add kaki 率表示には、「%」を付加する。
    	ret.append("%");
    	xls.append("%");

    	statsDecAlignFormat = ret.toString();
    	statsDecAlignFormat4xls = xls.toString();
    }
}
