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
import jp.co.arkinfosys.dto.master.BankDto;
import jp.co.arkinfosys.entity.AuditInfo;
import jp.co.arkinfosys.entity.join.BankDwb;
import jp.co.arkinfosys.form.master.AbstractEditForm;
import jp.co.arkinfosys.form.master.EditBankForm;
import jp.co.arkinfosys.service.AbstractMasterEditService;
import jp.co.arkinfosys.service.BankService;
import jp.co.arkinfosys.service.CategoryService;
import jp.co.arkinfosys.service.exception.ServiceException;

import org.apache.struts.util.LabelValueBean;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;

/**
 * 銀行マスタ編集画面のアクションクラスです.
 * @author Ark Information Systems
 *
 */
public class EditBankAction extends AbstractEditAction<BankDto, BankDwb> {

	/**
	 * 画面遷移用のマッピングクラスです.
	 * @author Ark Information Systems
	 *
	 */
	private static class Mapping {
		public static final String INPUT = "editBank.jsp";
	}

	@ActionForm
	@Resource
	public EditBankForm editBankForm;

	@Resource
	public BankService bankService;

	@Resource
	public CategoryService categoryService;

	/**
	 * 新規登録時の初期化処理を行います.<br>
	 * 処理実行後、{@link EditBankAction#getInputURL()}で取得したURIに遷移します.
	 * @return 画面遷移先のURI文字列
	 * @throws Exception
	 */
	@Execute(validator = false, reset = "initialize")
	public String index() throws Exception {
		super.init(null);
		this.initList();
		return getInputURL();
	}

	/**
	 * 編集モード時の初期化処理を行います.<br>
	 * 処理実行後、{@link EditBankAction#doEdit(String) doEdit()}で取得したURIに遷移します.
	 * @return 画面遷移先のURI文字列
	 * @throws Exception
	 */
	@Execute(validator = false, urlPattern = "edit/{bankId}", reset = "initialize")
	public String edit() throws Exception {
		this.editBankForm.bankId = StringUtil.decodeSL(this.editBankForm.bankId);
		return doEdit(getKey());
	}

	/**
	 * 登録処理を行います.<br>
	 * 処理実行後、{@link EditBankAction#doInsert()}で取得したURIに遷移します.
	 * @return 画面遷移先のURI文字列
	 * @throws Exception
	 */
	@Execute(validator = true, validate = "validate", input = "index", stopOnValidationError = false , reset = "reset")
	public String insert() throws Exception {
		//BankIDの発番
		editBankForm.bankId = bankService.makeBankId();
		return doInsert();
	}

	/**
	 * 更新処理を行います.<br>
	 * 処理実行後、{@link EditBankAction#doUpdate()}で取得したURIに遷移します.
	 * @return 画面遷移先のURI文字列
	 * @throws Exception
	 */
	@Execute(validator = true, validate = "validate", input = "initEdit", stopOnValidationError = false , reset = "reset")
	public String update() throws Exception {
		return doUpdate();
	}

	/**
	 * 削除処理を行います.<br>
	 * 処理実行後、{@link EditBankAction#doDelete()}で取得したURIに遷移します.
	 * @return 画面遷移先のURI文字列
	 * @throws Exception
	 */
	@Execute(validator = false , reset = "initialize")
	public String delete() throws Exception {
		return doDelete();
	}

	/**
	 * プルダウン要素を作成するため、オーバーライドします.
	 * @param dto 銀行マスタのDTO
	 * @throws Exception
	 * @see jp.co.arkinfosys.action.master.AbstractEditAction#doDeleteAfter(jp.co.arkinfosys.dto.master.MasterEditDto)
	 */
	@Override
	protected void doDeleteAfter(BankDto dto) throws Exception {
		this.initList();
	}

	/**
	 * プルダウン要素を作成するため、オーバーライドします.
	 * @param dto 銀行マスタのDTO
	 * @throws Exception
	 * @see jp.co.arkinfosys.action.master.AbstractEditAction#doInsertAfter(jp.co.arkinfosys.dto.master.MasterEditDto)
	 */
	@Override
	protected void doInsertAfter(BankDto dto) throws Exception {
		this.initList();
	}

	/**
	 * プルダウン要素を作成するため、オーバーライドします.
	 * @param dto 銀行マスタのDTO
	 * @throws Exception
	 * @see jp.co.arkinfosys.action.master.AbstractEditAction#doUpdateAfter(jp.co.arkinfosys.dto.master.MasterEditDto)
	 */
	@Override
	protected void doUpdateAfter(BankDto dto) throws Exception {
		this.initList();
	}

	/**
	 *
	 * @return {@link EditBankForm}
	 * @see jp.co.arkinfosys.action.master.AbstractEditAction#getActionForm()
	 */
	@Override
	protected AbstractEditForm getActionForm() {
		return this.editBankForm;
	}

	/**
	 *
	 * @return　プロパティキー文字列
	 * @see jp.co.arkinfosys.action.master.AbstractEditAction#getAlreadyExistsErrorKey()
	 */
	@Override
	protected String getAlreadyExistsErrorKey() {
		return "errors.bank.already.exists";
	}

	/**
	 *
	 * @return {@link BankDto}
	 * @see jp.co.arkinfosys.action.master.AbstractEditAction#getDtoClass()
	 */
	@Override
	protected Class<BankDto> getDtoClass() {
		return BankDto.class;
	}

	/**
	 *
	 * @return {@link Mapping#INPUT}で定義されたURI
	 * @see jp.co.arkinfosys.action.master.AbstractEditAction#getInputURL()
	 */
	@Override
	protected String getInputURL() {
		return Mapping.INPUT;
	}

	/**
	 *
	 * @return 銀行ID
	 * @see jp.co.arkinfosys.action.master.AbstractEditAction#getKey()
	 */
	@Override
	protected String getKey() {
		return this.editBankForm.bankId;
	}

	/**
	 *
	 * @return {@link MENU_ID#MASTER_BANK}で定義されたID
	 * @see jp.co.arkinfosys.action.master.AbstractEditAction#getMenuId()
	 */
	@Override
	protected String getMenuId() {
		return Constants.MENU_ID.MASTER_BANK;
	}

	/**
	 *
	 * @return {@link BankService}
	 * @see jp.co.arkinfosys.action.master.AbstractEditAction#getService()
	 */
	@Override
	protected AbstractMasterEditService<BankDto, BankDwb> getService() {
		return this.bankService;
	}

	/** 科目プルダウンリスト */
	public List<LabelValueBean> dwbTypeList = new ArrayList<LabelValueBean>();

	/**
	 * 科目プルダウン要素を作成します.
	 * @throws ServiceException
	 * @see jp.co.arkinfosys.action.master.AbstractEditAction#initList()
	 */
	@Override
	protected void initList() throws ServiceException {
		this.dwbTypeList = this.categoryService.findCategoryLabelValueBeanListById(Categories.DWB_TYPE);
	}

	/**
	 *
	 * @param key 銀行ID
	 * @return {@link BankDwb}
	 * @throws ServiceException
	 * @see jp.co.arkinfosys.action.master.AbstractEditAction#loadData(java.lang.String)
	 */
	@Override
	protected AuditInfo loadData(String key) throws ServiceException {
		return bankService.findById(key);
	}

	/**
	 * バリデートでエラーになった際の初期化処理を行います.<br>
	 * 処理実行後、{@link EditBankAction#getInputURL()}で取得したURIに遷移します.
	 * @return 画面遷移先のURI文字列
	 * @throws Exception
	 */
	@Execute(validator = false)
	public String initEdit() throws Exception {
		initList();
		this.editBankForm.editMode = true;
		return getInputURL();
	}
}
