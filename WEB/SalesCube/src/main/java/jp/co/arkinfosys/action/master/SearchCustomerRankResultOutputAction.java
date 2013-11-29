/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.master;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.annotation.Resource;

import jp.co.arkinfosys.action.AbstractSearchResultAction;
import jp.co.arkinfosys.dto.master.CustomerRankDto;
import jp.co.arkinfosys.entity.CustomerRankSummary;
import jp.co.arkinfosys.form.AbstractSearchForm;
import jp.co.arkinfosys.form.master.SearchCustomerRankForm;
import jp.co.arkinfosys.service.CustomerRankService;
import jp.co.arkinfosys.service.MasterSearch;
import jp.co.arkinfosys.service.exception.ServiceException;

import org.seasar.framework.beans.util.BeanMap;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;

/**
 * 顧客ランクExcel出力アクションクラスです.
 * @author Ark Information Systems
 *
 */
public class SearchCustomerRankResultOutputAction extends
		AbstractSearchResultAction<CustomerRankDto, CustomerRankSummary> {

	@ActionForm
	@Resource
	public SearchCustomerRankForm searchCustomerRankForm;

	@Resource
	public CustomerRankService customerRankService;

	/** EXCEL出力用のリスト */
	public List<CustomerRankSummary> summaryList = new ArrayList<CustomerRankSummary>();

	/**
	 * Excelに出力します.<br>
	 * 処理終了後、{@link SearchCustomerRankResultOutputAction#doSearch()}で取得したURIに遷移します.
	 * @return 画面遷移先のURI文字列
	 * @throws Exception
	 */
	@Execute(validator = false)
	public String excel() throws Exception {
		return super.doSearch();
	}

	/**
	 * 検索結果件数を返します.<BR>
	 * 未実装です.
	 * @param params パラメータを設定したマップ
	 * @return 0
	 * @throws ServiceException
	 * @see jp.co.arkinfosys.action.AbstractSearchResultAction#doCount(org.seasar.framework.beans.util.BeanMap)
	 */
	@Override
	protected int doCount(BeanMap params) throws ServiceException {
		return 0;
	}

	/**
	 * 検索を実行します.
	 * @param params 検索条件のマップ
	 * @param sortColumn ソートカラム名
	 * @param sortOrderAsc 昇順にソートするか否か
	 * @param rowCount 取得件数
	 * @param offset 取得開始位置
	 * @return {@link CustomerRankSummary}のリスト
	 * @throws ServiceException
	 * @see jp.co.arkinfosys.action.AbstractSearchResultAction#execSearch(org.seasar.framework.beans.util.BeanMap, java.lang.String, boolean, int, int)
	 */
	@Override
	protected List<CustomerRankSummary> execSearch(BeanMap params,
			String sortColumn, boolean sortOrderAsc, int rowCount, int offset)
			throws ServiceException {
		return this.customerRankService.findCustomerRankSummary(params);
	}

	/**
	 * エンティティからDTOへの変換を行います.
	 * @param entityList {@link CustomerRankSummary}のリスト
	 * @return {@link CustomerRankDto}のリスト
	 * @throws Exception
	 * @see jp.co.arkinfosys.action.AbstractSearchResultAction#exchange(java.util.List)
	 */
	@Override
	protected List<CustomerRankDto> exchange(
			List<CustomerRankSummary> entityList) throws Exception {
		this.summaryList = entityList;
		return null;
	}

	/**
	 * フォームにラベルを設定します.
	 * @throws Exception
	 * @see jp.co.arkinfosys.action.AbstractSearchResultAction#doAfterSearch()
	 */
	@Override
	protected void doAfterSearch() throws Exception {
		// システム日付
		Calendar c = Calendar.getInstance();

		// １ヶ月前
		c.add(Calendar.MONTH, -1);
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH) + 1;
		this.searchCustomerRankForm.labelSales1 = year + "年" + month + "月";

		// ２ヶ月前
		c.add(Calendar.MONTH, -1);
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH) + 1;
		this.searchCustomerRankForm.labelSales2 = year + "年" + month + "月";

		// ３ヶ月前
		c.add(Calendar.MONTH, -1);
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH) + 1;
		this.searchCustomerRankForm.labelSales3 = year + "年" + month + "月";

		// ４ヶ月前
		c.add(Calendar.MONTH, -1);
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH) + 1;
		this.searchCustomerRankForm.labelSales4 = year + "年" + month + "月";

		// ５ヶ月前
		c.add(Calendar.MONTH, -1);
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH) + 1;
		this.searchCustomerRankForm.labelSales5 = year + "年" + month + "月";

		// ６ヶ月前
		c.add(Calendar.MONTH, -1);
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH) + 1;
		this.searchCustomerRankForm.labelSales6 = year + "年" + month + "月";
	}

	/**
	 *
	 * @return {@link SearchCustomerRankForm}
	 * @see jp.co.arkinfosys.action.AbstractSearchResultAction#getActionForm()
	 */
	@Override
	protected AbstractSearchForm<CustomerRankDto> getActionForm() {
		return searchCustomerRankForm;
	}

	/**
	 *
	 * @return {@link Mapping#EXCEL}
	 * @see jp.co.arkinfosys.action.AbstractSearchResultAction#getResultURIString()
	 */
	@Override
	protected String getResultURIString() {
		return Mapping.EXCEL;
	}

	/**
	 *
	 * @return {@link CustomerRankDto}
	 * @see jp.co.arkinfosys.action.AbstractSearchResultAction#getDtoClass()
	 */
	@Override
	protected Class<CustomerRankDto> getDtoClass() {
		return null;
	}

	/**
	 * 検索画面のメニューIDを返します.<BR>
	 * 未実装です.
	 * @return null
	 * @see jp.co.arkinfosys.action.AbstractSearchResultAction#getSearchMenuID()
	 */
	@Override
	protected String getSearchMenuID() {
		return null;
	}

	/**
	 * 検索サービスを返します.<BR>
	 * 未実装です.
	 * @return null
	 * @see jp.co.arkinfosys.action.AbstractSearchResultAction#getService()
	 */
	@Override
	protected MasterSearch<CustomerRankSummary> getService() {
		return null;
	}
}
