/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.master;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import jp.co.arkinfosys.common.Categories;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.common.Constants.MENU_ID;
import jp.co.arkinfosys.common.StringUtil;
import jp.co.arkinfosys.dto.master.CustomerRankDto;
import jp.co.arkinfosys.entity.AuditInfo;
import jp.co.arkinfosys.entity.CustomerRank;
import jp.co.arkinfosys.form.master.AbstractEditForm;
import jp.co.arkinfosys.form.master.EditCustomerRankForm;
import jp.co.arkinfosys.service.AbstractMasterEditService;
import jp.co.arkinfosys.service.CategoryService;
import jp.co.arkinfosys.service.CustomerRankService;
import jp.co.arkinfosys.service.exception.ServiceException;

import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.LabelValueBean;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.util.MessageResourcesUtil;

/**
 * 顧客ランク編集画面のアクションクラスです.
 * @author Ark Information Systems
 *
 */
public class EditCustomerRankAction extends
		AbstractEditAction<CustomerRankDto, CustomerRank> {

	/**
	 * 画面遷移用のマッピングクラスです.
	 * @author Ark Information Systems
	 *
	 */
	private static class Mapping {
		public static final String INPUT = "editCustomerRank.jsp";
	}

	@ActionForm
	@Resource
	public EditCustomerRankForm editCustomerRankForm;

	@Resource
	public CustomerRankService customerRankService;

	@Resource
	public CategoryService categoryService;

	/** 送料区分プルダウンリスト */
	public List<LabelValueBean> postageTypeList = new ArrayList<LabelValueBean>();

	/**
	 * 新規登録時の初期化処理を行います.<br>
	 * 処理終了後、{@link EditCustomerRankAction#getInputURL()}で取得したURIに遷移します.
	 * @return 画面遷移先のURI文字列
	 * @throws Exception
	 */
	@Execute(validator = false)
	public String index() throws Exception {
		super.init(null);
		initList();
		return getInputURL();
	}

	/**
	 * 編集モード時の初期化処理を行います.<br>
	 * 処理終了後、{@link EditCustomerRankAction#doEdit(String) doEdit()}で取得したURIに遷移します.
	 * @return 画面遷移先のURI文字列
	 * @throws Exception
	 */
	@Execute(validator = false, urlPattern = "edit/{rankCode}")
	public String edit() throws Exception {
		return doEdit(getKey());
	}

	/**
	 * 登録処理を行います.<br>
	 * 処理終了後、{@link EditCustomerRankAction#doInsert()}で取得したURIに遷移します.
	 * @return 画面遷移先のURI文字列
	 * @throws Exception
	 */
	@Execute(validator = true, validate = "validateRate,validate", input = "index", stopOnValidationError = false)
	public String insert() throws Exception {
		initList();
		return doInsert();
	}

	/**
	 * 更新処理を行います.<br>
	 * 処理終了後、{@link EditCustomerRankAction#doUpdate()}で取得したURIに遷移します.
	 * @return 画面遷移先のURI文字列
	 * @throws Exception
	 */
	@Execute(validator = true, validate = "validateRate,validate", input = "initEdit", stopOnValidationError = false)
	public String update() throws Exception {
		return doUpdate();
	}

	/**
	 * 削除処理を行います.<br>
	 * 処理終了後、{@link EditCustomerRankAction#doDelete()}で取得したURIに遷移します.
	 * @return 画面遷移先のURI文字列
	 * @throws Exception
	 */
	@Execute(validator = false)
	public String delete() throws Exception {
		return doDelete();

	}

	/**
	 * 
	 * @return {@link EditCustomerRankForm}
	 * @see jp.co.arkinfosys.action.master.AbstractEditAction#getActionForm()
	 */
	@Override
	protected AbstractEditForm getActionForm() {
		return this.editCustomerRankForm;
	}

	/**
	 * 自動発番のため、使用しません.
	 * @return null
	 * @see jp.co.arkinfosys.action.master.AbstractEditAction#getAlreadyExistsErrorKey()
	 */
	@Override
	protected String getAlreadyExistsErrorKey() {
		// 自動発番なので使わない
		return null;
	}

	/**
	 * 
	 * @return {@link CustomerRankDto}
	 * @see jp.co.arkinfosys.action.master.AbstractEditAction#getDtoClass()
	 */
	@Override
	protected Class<CustomerRankDto> getDtoClass() {
		return CustomerRankDto.class;
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
	 * 
	 * @return 顧客ランクコード
	 * @see jp.co.arkinfosys.action.master.AbstractEditAction#getKey()
	 */
	@Override
	protected String getKey() {
		return this.editCustomerRankForm.rankCode;
	}

	/**
	 * 
	 * @return {@link MENU_ID#MASTER_CUSTOMER_RANK}
	 * @see jp.co.arkinfosys.action.master.AbstractEditAction#getMenuId()
	 */
	@Override
	protected String getMenuId() {
		return Constants.MENU_ID.MASTER_CUSTOMER_RANK;
	}

	/**
	 * 
	 * @return {@link CustomerRankService}
	 * @see jp.co.arkinfosys.action.master.AbstractEditAction#getService()
	 */
	@Override
	protected AbstractMasterEditService<CustomerRankDto, CustomerRank> getService() {
		return this.customerRankService;
	}

	/**
	 * 
	 * @param key 顧客ランクコード
	 * @return {@link CustomerRank}
	 * @throws ServiceException
	 * @see jp.co.arkinfosys.action.master.AbstractEditAction#loadData(java.lang.String)
	 */
	@Override
	protected AuditInfo loadData(String key) throws ServiceException {
		CustomerRank result = this.customerRankService.findById(key);
		return result;
	}

	/**
	 * 送料区分プルダウン要素を作成します.
	 * @throws ServiceException
	 * @see jp.co.arkinfosys.action.master.AbstractEditAction#initList()
	 */
	@Override
	protected void initList() throws ServiceException {
		super.initList();

		this.postageTypeList = this.categoryService
				.findCategoryLabelValueBeanListById(Categories.POSTAGE_CATEGORY);
	}

	/**
	 * バリデートでエラーになった際の初期化処理を行います.<br>
	 * 処理実行後、{@link EditCustomerRankAction#getInputURL()}で取得したURIに遷移します.
	 * @return 画面遷移先のURI文字列
	 * @throws ServiceException
	 */
	@Execute(validator = false)
	public String initEdit() throws ServiceException {
		this.editCustomerRankForm.editMode = true;
		initList();
		return getInputURL();
	}

	/**
	 * 発番された顧客ランクコードを設定します.
	 * @param dto 顧客ランクマスタのDTO
	 * @throws Exception
	 * @see jp.co.arkinfosys.action.master.AbstractEditAction#doInsertAfter(jp.co.arkinfosys.dto.master.MasterEditDto)
	 */
	@Override
	protected void doInsertAfter(CustomerRankDto dto) throws Exception {
		// 発番された番号を設定する
		this.editCustomerRankForm.rankCode = dto.rankCode;
	}

	/**
	 * 割引率のバリデートを行います.
	 * @return 表示するメッセージ
	 */
	public ActionMessages validateRate() {
		ActionMessages errors = new ActionMessages();
		// 割引率
		String labelRankRate = MessageResourcesUtil.getMessage("labels.rankRate");
		if (StringUtil.hasLength(this.editCustomerRankForm.rankRate)){
			this.editCustomerRankForm.checkDecimal(this.editCustomerRankForm.rankRate, labelRankRate, 2, super.mineDto.statsDecAlignment, errors);
		}
		return errors;
	}

}
