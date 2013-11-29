/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.dto.sales;


import org.apache.struts.util.LabelValueBean;

/**
 * 売上帳票発行画面の取引区分情報を管理するDTOクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class SalesCategoryDto {
	// 取引区分リスト
    public LabelValueBean salesCategory;// 取引区分

    // 取引区分選択
    public boolean salesCategorySel;
}
