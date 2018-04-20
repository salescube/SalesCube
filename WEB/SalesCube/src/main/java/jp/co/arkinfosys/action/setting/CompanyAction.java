/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.setting;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import jp.co.arkinfosys.action.CommonResources;
import jp.co.arkinfosys.action.master.EditCustomerRankAction;
import jp.co.arkinfosys.common.Categories;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.entity.Mine;
import jp.co.arkinfosys.form.setting.CompanyForm;
import jp.co.arkinfosys.service.CategoryService;
import jp.co.arkinfosys.service.MineService;
import jp.co.arkinfosys.service.exception.ServiceException;

import org.apache.commons.fileupload.FileUploadBase.SizeLimitExceededException;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.LabelValueBean;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.upload.S2MultipartRequestHandler;
import org.seasar.struts.util.ActionMessagesUtil;

/**
 * 自社情報画面のアクションクラスです.
 * @author Ark Information Systems
 *
 */
public class CompanyAction extends CommonResources {

	/**
	 * 画面遷移用のマッピングクラスです.
	 * @author Ark Information Systems
	 *
	 */
	public static class Mapping {
		public static final String INPUT = "company.jsp";
	}

	@ActionForm
	@Resource
	private CompanyForm companyForm;

	@Resource
	private MineService mineService;

	@Resource
	public CategoryService categoryService;

	/** 送料区分プルダウンリスト */
	public List<LabelValueBean> postageTypeList = new ArrayList<LabelValueBean>();

	/**
	 * 初期表示を行います.
	 * 処理終了後、{@link Mapping#INPUT}で定義されたURIに遷移します.
	 * @return 画面遷移先のURI文字列
	 * @throws Exception
	 */
	@Execute(validator = false)
	public String index() throws Exception {
		// サイズエラーチェック
		uploadErrorCheck();

		init();

		return CompanyAction.Mapping.INPUT;
	}

	/**
	 * リセット処理を行います.<br>
	 * 処理終了後、{@link Mapping#INPUT}で定義されたURIに遷移します.
	 * @return 画面遷移先のURI文字列
	 * @throws Exception
	 */
	@Execute(validator = false)
	public String reset() throws Exception {
		init();

		return CompanyAction.Mapping.INPUT;
	}

	/**
	 * リクエストのサイズが設定された最大サイズを超えていないか確認します.
	 * @throws Exception
	 */
	protected void uploadErrorCheck() throws Exception {
		SizeLimitExceededException e = (SizeLimitExceededException) super.httpRequest
				.getAttribute(S2MultipartRequestHandler.SIZE_EXCEPTION_KEY);
		if (e != null) {
			// 他のエラーは削除
			super.httpRequest.setAttribute(Globals.ERROR_KEY, null);
			// ファイルサイズエラーを書き込み
			super.messages.add(ActionMessages.GLOBAL_MESSAGE,
					new ActionMessage("errors.upload.size", e
							.getPermittedSize()));
			ActionMessagesUtil.addErrors(super.httpRequest, super.messages);

			// 読みかけのファイルの残りを読む
			InputStream is = null;
			try {
				is = this.httpRequest.getInputStream();
				byte[] buf = new byte[1024];
				while (is.read(buf) != -1) {
				}
				is.close();
				is = null;
			} catch (Exception ignore) {
			} finally {
				try {
					if(is != null) {
						is.close();
					}
				} catch (Exception ignore) {
				}
			}
		}
	}

	/**
	 * 更新処理を行います.<br>
	 * 更新完了時および何かしらの問題があった場合に、画面にメッセージを表示します.<br>
	 * 処理終了後、{@link Mapping#INPUT}で定義されたURIに遷移します.
	 * @return 画面遷移先のURI文字列
	 * @throws Exception
	 */
	@Execute(validate = "validate", validator = true, input = "initEdit")
	public String update() throws Exception {
		// サイズエラーチェック
		uploadErrorCheck();

		// 自社情報を更新する
		// 排他制御
		Mine mine = this.mineService.getMine();
		if (mine != null) {

			String nowUpdatetm = "";
			if( mine.updDatetm != null){
				nowUpdatetm = String.valueOf(mine.updDatetm);
			}
			if( companyForm.updDatetm.equals(nowUpdatetm) == false){
				super.messages.add(ActionMessages.GLOBAL_MESSAGE,	new ActionMessage("errors.exclusive.control.updated"));
				ActionMessagesUtil.addErrors(super.httpRequest, super.messages);
				return CompanyAction.Mapping.INPUT;
			}

		}

		this.mineService.updateMine(companyForm.companyName, companyForm.companyAbbr, companyForm.companyKana,
				companyForm.companyCeoName, companyForm.companyCeoTitle, companyForm.logoImgPath, companyForm.logoInit, companyForm.companyZipCode , companyForm.companyAddress1,
				companyForm.companyAddress2, companyForm.companyTel, companyForm.companyFax, companyForm.companyEmail,
				companyForm.companyWebSite, companyForm.cutoffGroup, companyForm.closeMonth, companyForm.iniPostageType, companyForm.targetPostageCharges, companyForm.postage);

		super.messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
				"infos.company.changed"));
		ActionMessagesUtil.addMessages(super.httpRequest, super.messages);

		init();

		return CompanyAction.Mapping.INPUT;

	}

	/**
	 * 初期化処理を行います.
	 * @throws Exception
	 */
	private void init() throws Exception {
		this.companyForm.isUpdate = this.userDto.isMenuUpdate( Constants.MENU_ID.SETTING_COMPANY );

		// 自社マスタの情報を取得する
		Mine mine = this.mineService.getMine();

		// 自社情報をセッション管理DTOに格納する
		Beans.copy(mine, super.mineDto).execute();

		//送料区分プルダウン要素を作成します
		initList();

		if (mine != null) {
			companyForm.companyName = mine.companyName;
			companyForm.companyAbbr = mine.companyAbbr;
			companyForm.companyKana = mine.companyKana;
			companyForm.companyCeoName = mine.companyCeoName;
			companyForm.companyCeoTitle = mine.companyCeoTitle;
			companyForm.logoImgPath = null;
			companyForm.logoInit = false;
			companyForm.companyZipCode = mine.companyZipCode;
			companyForm.companyAddress1 = mine.companyAddress1;
			companyForm.companyAddress2 = mine.companyAddress2;
			companyForm.companyTel = mine.companyTel;
			companyForm.companyFax = mine.companyFax;
			companyForm.companyEmail = mine.companyEmail;
			companyForm.companyWebSite = mine.companyWebSite;
			companyForm.cutoffGroup = mine.cutoffGroup;
			companyForm.closeMonth = mine.closeMonth;
			companyForm.iniPostageType = mine.iniPostageType;
			companyForm.targetPostageCharges = mine.targetPostageCharges;
			companyForm.postage = mine.postage;

			companyForm.updDatetm = "";

			companyForm.priceFractCategory = mineDto.priceFractCategory;

			if( mine.updDatetm != null)	{
				companyForm.updDatetm = String.valueOf(mine.updDatetm);
			}
		}

	}

	/**
	 *
	 * @return {@link Mapping#INPUT}で定義されたURI文字列
	 * @see jp.co.arkinfosys.action.master.AbstractEditAction#getInputURL()
	 */

	protected String getInputURL() {
		return Mapping.INPUT;
	}

	/**
	 * 送料区分プルダウン要素を作成します.
	 * @throws ServiceException
	 * @see jp.co.arkinfosys.action.master.AbstractEditAction#initList()
	 */
	protected void initList() throws ServiceException {

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
		initList();
		return getInputURL();
	}

}
