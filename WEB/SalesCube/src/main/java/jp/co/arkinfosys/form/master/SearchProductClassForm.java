/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.form.master;

import java.util.List;

import jp.co.arkinfosys.dto.master.ProductClassDto;
import jp.co.arkinfosys.form.AbstractSearchForm;

import org.apache.struts.util.LabelValueBean;

/**
 * 分類画面（検索）のアクションフォームクラスです.
 * @author Ark Information Systems
 *
 */
public class SearchProductClassForm extends AbstractSearchForm<ProductClassDto> {

	/** 分類（大）コード */
	public String classCode1;

	/** 分類（中）コード */
	public String classCode2;

	/** 分類（小）コード */
	public String classCode3;

	/** 分類コード */
	public String classCode;

	/** 分類名 */
	public String className;

	/** 分類（大）リスト*/
	public List<LabelValueBean> classCode1List;

	/** 分類（中）リスト*/
	public List<LabelValueBean> classCode2List;

	/** 分類（小）リスト*/
	public List<LabelValueBean> classCode3List;

}
