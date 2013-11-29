/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.form;

import java.util.List;

import jp.co.arkinfosys.common.Constants;

import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.LabelValueBean;

/**
 * 検索画面の基底アクションフォームクラスです.
 *
 * @author Ark Information Systems
 *
 * @param <DTOCLASS>
 */
public abstract class AbstractSearchForm<DTOCLASS> {

	/**
	 * 更新モードか否か
	 */
	public boolean isUpdate = false;

	/**
	 * 入力画面に遷移する権限があるか否か
	 */
	public boolean isInputValid = false;

	/**
	 * 検索対象（伝票 or 明細)
	 */
	public String searchTarget = Constants.SEARCH_TARGET.VALUE_LINE;

	/**
	 * 検索結果件数
	 */
	public int searchResultCount;

	/**
	 * 検索結果リスト
	 */
	public List<DTOCLASS> searchResultList;

	/** ページ番号 */
	public int pageNo;

	/** 結果表示行数 */
	public int rowCount = Constants.PAGE_VIEW_COUNT.VIEW_INIT;

	/** ソート項目 */
	public String sortColumn;

	/** ソート順 */
	public boolean sortOrderAsc;

	/**
	 * 検索対象リスト
	 */
	public List<LabelValueBean> searchTargetList;

	/**
     * 検索時のバリデートを行います.
     * @return 表示するメッセージ
     */
	public ActionMessages validate() {
		ActionMessages errors = new ActionMessages();

		// デフォルトでは何もしない
		return errors;
	}
}
