/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.form.master;

import java.util.List;

import jp.co.arkinfosys.dto.master.CategoryGroupDto;
import jp.co.arkinfosys.form.AbstractSearchForm;

/**
 * 区分マスタ管理　区分名一覧画面のアクションフォームクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class SearchCategoryForm extends AbstractSearchForm<CategoryGroupDto> {

	public List<CategoryGroupDto> groupList;

}
