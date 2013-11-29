/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.master;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import jp.co.arkinfosys.common.Categories;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.dto.master.CategoryDto;
import jp.co.arkinfosys.dto.master.TaxRateDto;
import jp.co.arkinfosys.entity.AuditInfo;
import jp.co.arkinfosys.entity.Category;
import jp.co.arkinfosys.entity.CategoryTrn;
import jp.co.arkinfosys.entity.TaxRate;
import jp.co.arkinfosys.form.master.AbstractEditForm;
import jp.co.arkinfosys.form.master.EditTaxRateForm;
import jp.co.arkinfosys.service.AbstractMasterEditService;
import jp.co.arkinfosys.service.CategoryService;
import jp.co.arkinfosys.service.exception.ServiceException;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.util.ActionMessagesUtil;

/**
 * 税率マスタ管理　税率編集画面のアクションクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class EditTaxRateAction extends AbstractEditAction<CategoryDto, Category> {

	protected static class Mapping {
		public static final String INPUT = "editTaxRate.jsp";
	}

	@ActionForm
	@Resource
	public EditTaxRateForm editTaxRateForm;

	@Resource
	public CategoryService categoryService;

	/**
	 * 編集モードで初期化を行います.
	 * @return 画面遷移先のURI文字列
	 * @throws Exception
	 */
	@Execute(validator = false)
	public String edit() throws Exception {
		return super.doEdit(this.getKey());
	}

	/**
	 *
	 * @param key 税区分ID
	 * @return {@link CategoryTrn}
	 * @throws ServiceException
	 * @see jp.co.arkinfosys.action.master.AbstractEditAction#loadData(java.lang.String)
	 */
	@Override
	protected AuditInfo loadData(String key) throws ServiceException {
		// ベースとなる区分データを取得する
		CategoryTrn category = categoryService.findCategoryTrnByIdAndCode(
				Categories.TAX_TYPE_CATEGORY,
				this.editTaxRateForm.taxTypeCategory);
		if (category == null) {
			this.messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"infos.delete"));
			ActionMessagesUtil.addErrors(this.httpRequest, this.messages);
			return null;
		}

		// フォーム設定
		this.editTaxRateForm.category = category;

		List<TaxRate> taxRateList = taxRateService
				.findTaxRateByTaxTypeCagory(this.editTaxRateForm.taxTypeCategory);
		this.editTaxRateForm.taxRateList = new ArrayList<TaxRateDto>();
		for (TaxRate taxRate : taxRateList) {
			TaxRateDto dto = Beans.createAndCopy(TaxRateDto.class, taxRate)
								.timestampConverter(Constants.FORMAT.TIMESTAMP)
								.timestampConverter(Constants.FORMAT.DATE, "startDate")
								.dateConverter(Constants.FORMAT.DATE)
								.execute();
			this.editTaxRateForm.taxRateList.add(dto);
		}

		return category;
	}

	/**
	 * 更新処理を行います.<br>
	 * 処理終了後、{@link EditTaxRateAction#getInputURL()}で取得したURIに遷移します.<BR>
	 * ベースとなる区分データがない場合は、{@link EditTaxRateAction#getSearchURL()}で取得したURIに遷移します.
	 * @return 画面遷移先のURI文字列
	 * @throws Exception
	 */
	@Execute(validator = true, validate = "validate", input = Mapping.INPUT, stopOnValidationError = false)
	public String update() throws Exception {
		// ベースとなる区分データレコードを取得する
		CategoryTrn category = categoryService.findCategoryTrnByIdAndCode(
				Categories.TAX_TYPE_CATEGORY,
				this.getKey());
		if (category == null) {
			this.messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"infos.delete"));
			ActionMessagesUtil.addErrors(this.httpRequest, this.messages);
			return this.getSearchURL();
		}

		// 既存レコードを更新する
		super.taxRateService.updateRecords(this.editTaxRateForm.taxRateList,
				this.getKey(),
				this.editTaxRateForm.updDatetm);


		super.init(this.getKey());

		// メッセージ設定
		this.messages.add(ActionMessages.GLOBAL_MESSAGE,
				new ActionMessage("infos.update"));
		ActionMessagesUtil.addMessages(this.httpRequest, this.messages);

		return this.getInputURL();
	}

	/**
	 *
	 * @return {@link EditTaxRateForm}
	 * @see jp.co.arkinfosys.action.master.AbstractEditAction#getActionForm()
	 */
	@Override
	protected AbstractEditForm getActionForm() {
		return this.editTaxRateForm;
	}

	/**
	 *
	 * @return {@link Mapping#INPUT}で定義されたURI文字列
	 * @see jp.co.arkinfosys.action.master.AbstractEditAction#getInputURL()
	 */
	@Override
	protected String getInputURL() {
		return Mapping.INPUT;
	}

	/**
	 * 税率マスタ検索画面のURI文字列を返します.
	 * @return URI文字列
	 */
	protected String getSearchURL() {
		return "searchTaxRate.jsp";
	}

	/**
	 *
	 * @return 税区分ID
	 * @see jp.co.arkinfosys.action.master.AbstractEditAction#getKey()
	 */
	@Override
	protected String getKey() {
		return this.editTaxRateForm.taxTypeCategory;
	}

	/**
	 *
	 * @return {@link MENU_ID#MASTER_TAX_RATE}
	 * @see jp.co.arkinfosys.action.master.AbstractEditAction#getMenuId()
	 */
	@Override
	protected String getMenuId() {
		return Constants.MENU_ID.MASTER_TAX_RATE;
	}

	/**
	 *
	 * @return {@link CategoryDto}
	 * @see jp.co.arkinfosys.action.master.AbstractEditAction#getDtoClass()
	 */
	@Override
	protected Class<CategoryDto> getDtoClass() {
		return null;
	}

	/**
	 * 未実装です.
	 * @return null
	 * @see jp.co.arkinfosys.action.master.AbstractEditAction#getService()
	 */
	@Override
	protected AbstractMasterEditService<CategoryDto, Category> getService() {
		return null;
	}

	/**
	 * 未実装です.
	 * @return null
	 * @see jp.co.arkinfosys.action.master.AbstractEditAction#getAlreadyExistsErrorKey()
	 */
	@Override
	protected String getAlreadyExistsErrorKey() {
		return null;
	}
}
