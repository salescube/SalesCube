/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.ajax.master;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import jp.co.arkinfosys.action.ajax.AbstractSearchResultAjaxAction;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.dto.master.CustomerRankDto;
import jp.co.arkinfosys.entity.CustomerRank;
import jp.co.arkinfosys.form.AbstractSearchForm;
import jp.co.arkinfosys.form.master.SearchCustomerRankForm;
import jp.co.arkinfosys.s2extend.NumberConverter;
import jp.co.arkinfosys.service.CustomerRankService;
import jp.co.arkinfosys.service.MasterSearch;

import org.seasar.framework.beans.Converter;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;

/**
 * 顧客ランクマスタ画面の検索実行アクションクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class SearchCustomerRankAjaxAction extends
		AbstractSearchResultAjaxAction<CustomerRankDto, CustomerRank> {

	@ActionForm
	@Resource
	public SearchCustomerRankForm searchCustomerRankForm;

	@Resource
	public CustomerRankService customerRankService;

	@Execute(validator = false)
	public String search() throws Exception {
		return super.doSearch();
	}

	/**
	 * ENTITYのリストをDTOのリストに変換します.
	 * @param entityList {@link CustomerRank}のリスト
	 * @return {@link CustomerRankDto}のリスト
	 * @throws Exception
	 * @see jp.co.arkinfosys.action.AbstractSearchResultAction#exchange(java.util.List)
	 */
	@Override
	protected List<CustomerRankDto> exchange(List<CustomerRank> entityList)
			throws Exception {

		// 金額と値引率のフォーマット変換
		Converter rateConv = this.customerRankService
				.createStatusPriceConverter();
		Converter priceConv = new NumberConverter(
				super.mineDto.priceFractCategory, 0, true);

		List<CustomerRankDto> dtoList = new ArrayList<CustomerRankDto>();
		for (CustomerRank entity : entityList) {
			CustomerRankDto dto = Beans.createAndCopy(this.getDtoClass(),
					entity).timestampConverter(Constants.FORMAT.TIMESTAMP)
					.dateConverter(Constants.FORMAT.DATE).converter(rateConv,
							"rankRate").converter(priceConv,
							"roMonthlyAvgFrom", "roMonthlyAvgTo").execute();
			dtoList.add(dto);
		}

		return dtoList;
	}

	/**
	 * 検索画面のアクションフォームを返します.
	 * @return {@link SearchCustomerRankForm}
	 */
	@Override
	protected AbstractSearchForm<CustomerRankDto> getActionForm() {
		return this.searchCustomerRankForm;
	}

	/**
	 * 顧客ランクマスタDTOを返します.
	 * @return {@link CustomerRankDto}
	 */
	@Override
	protected Class<CustomerRankDto> getDtoClass() {
		return CustomerRankDto.class;
	}

	@Override
	protected String getResultURIString() {
		return Mapping.RESULT;
	}

	/**
	 * 検索処理を行う顧客ランクマスタサービスを返します.
	 * @return {@link CustomerRankService}
	 */
	@Override
	protected MasterSearch<CustomerRank> getService() {
		return this.customerRankService;
	}

	/**
	 * 検索画面のメニューIDを返します.
	 * @return 顧客ランクマスタ画面のメニューID
	 */
	@Override
	protected String getSearchMenuID() {
		return Constants.MENU_ID.MASTER_CUSTOMER_RANK;
	}
}
