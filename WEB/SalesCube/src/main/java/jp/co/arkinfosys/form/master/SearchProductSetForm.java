/*
 *  Copyright 2009-2010 Ark Information Systems.
 */

package jp.co.arkinfosys.form.master;

import jp.co.arkinfosys.dto.master.ProductSetDto;
import jp.co.arkinfosys.form.AbstractSearchForm;

/**
 * セット商品画面（検索）のアクションフォームクラスです.
 * @author Ark Information Systems
 *
 */
public class SearchProductSetForm extends AbstractSearchForm<ProductSetDto> {

	public String setProductCode; 

	public String setProductName; 

	public String productCode; 

	public String productName; 

}
