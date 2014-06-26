/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.master;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.dto.master.CategoryDto;
import jp.co.arkinfosys.entity.AuditInfo;
import jp.co.arkinfosys.entity.Category;
import jp.co.arkinfosys.entity.join.CategoryJoin;
import jp.co.arkinfosys.form.master.AbstractEditForm;
import jp.co.arkinfosys.form.master.EditCategoryForm;
import jp.co.arkinfosys.service.AbstractMasterEditService;
import jp.co.arkinfosys.service.CategoryService;
import jp.co.arkinfosys.service.exception.ServiceException;
import jp.co.arkinfosys.service.exception.UnabledLockException;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.util.ActionMessagesUtil;

/**
 * 区分マスタ編集画面のアクションクラスです.
 * @author Ark Information Systems
 *
 */
public class EditCategoryAction extends
		AbstractEditAction<CategoryDto, Category> {

	protected static class Mapping {
		public static final String INPUT = "editCategory.jsp";
	}

	@ActionForm
	@Resource
	public EditCategoryForm editCategoryForm;

	@Resource
	public CategoryService categoryService;

	/**
	 * 編集モード時の初期化処理を行います.<br>
	 * 処理実行後、{@link EditCategoryAction#getSearchURL()}で取得したURIに遷移します.
	 * @return 画面遷移先のURI
	 * @throws Exception
	 */
	@Execute(validator = false)
	public String edit() throws Exception {
		return super.doEdit(this.getKey());
	}

	/**
	 * 区分マスタを読み込みます.
	 * @return {@link EditCategoryAction#getInputURL()}で定義されたURI文字列
	 * @throws ServiceException
	 */
	@Override
	protected AuditInfo loadData(String key) throws ServiceException {
		// ベースとなる区分マスタレコードを取得する
		Category category = categoryService.findCategoryById(key);
		if (category == null) {
			this.messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"infos.delete"));
			ActionMessagesUtil.addErrors(this.httpRequest, this.messages);
			return null;
		}

		// フォーム設定
		this.editCategoryForm.category = category;

		List<CategoryJoin> categoryTrns = categoryService
				.findCategoryJoinById(Integer.parseInt(this.getKey()));
		this.editCategoryForm.categoryTrnList = new ArrayList<CategoryDto>();
		for (CategoryJoin categoryJoin : categoryTrns) {
			CategoryDto dto = Beans.createAndCopy(CategoryDto.class,
					categoryJoin).execute();
			dto.categoryDsp = categoryJoin.categoryTrnDsp;
			this.editCategoryForm.categoryTrnList.add(dto);
		}

		return category;
	}

	/**
	 * 更新処理を行います.<br>
	 * 何かしらの問題があった場合および更新完了時に、画面にメッセージを表示します.<br>
	 * 元となる区分が存在しない場合は{@link EditCategoryAction#getSearchURL()}、その他の場合は{@link EditCategoryAction#getInputURL()}で定義されたURIに遷移します.
	 * @return 画面遷移先のURI文字列
	 * @throws Exception
	 */
	@Execute(validator = true, validate = "validate", input = Mapping.INPUT, stopOnValidationError = false)
	public String update() throws Exception {
		// ベースとなる区分マスタレコードを取得する
		Category category = categoryService.findCategoryById(this.getKey());
		if (category == null) {
			this.messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"infos.delete"));
			ActionMessagesUtil.addErrors(this.httpRequest, this.messages);
			return this.getSearchURL();
		}

		// 区分マスタを更新する

		try {
			categoryService.updateMasterRecordUpdDatetm(Integer.parseInt(this
					.getKey()), this.editCategoryForm.updDatetm);
		} catch (UnabledLockException e) {
			super.errorLog(e);

			// ロックエラー
			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(e
					.getKey()));
			ActionMessagesUtil.addErrors(super.httpRequest, errors);

			return this.getInputURL();
		}


		// 既存レコードを削除する
		List<CategoryJoin> categoryTrns = categoryService
				.findCategoryJoinById(Integer.parseInt(this.getKey()));

		for (CategoryJoin categoryJoin : categoryTrns) {
			CategoryDto dto = Beans.createAndCopy(CategoryDto.class,
					categoryJoin).execute();

			//更新履歴を残すためのUPDATE
			categoryService.updateAudit(new String[] { this.getKey(),
					dto.categoryCode });

			//IDが可変なので一度Deleteする
			categoryService.deleteRecord(dto);
		}

		int noInsertCount = 0;
		int doubleCount = 0;


		// 画面上のレコードを登録する
		for (CategoryDto dto : this.editCategoryForm.categoryTrnList) {
			dto.categoryId = this.getKey();

			//対象のIDがDBに存在しないかチェックする
			if(categoryService.findCategoryTrnByIdAndCode(Integer.valueOf(dto.categoryId),dto.categoryCode) == null){
					//INSERT
					categoryService.insertRecord(dto);
			}else{
				doubleCount += 1;
				//DBに存在している場合、対象のID・コードがが区分データテーブル上に非表示項目かどうかかチェックする
				if(categoryService.findCategoryTrnNoDspByIdAndCode(dto) != null){
					//非表示項目として存在するためINSERTはしない。
					//カウントのみ
					noInsertCount += 1;
				}
			}

		}

		super.init(this.getKey());

		 if(doubleCount != 0){
				// メッセージ設定
				this.messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"infos.update" ));

			 //重複している値が非表示項目の場合
			if (noInsertCount != 0){
				// メッセージ設定
				this.messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.already.exists.CategoryCode" ));
			}

			//重複している値が大文字小文字の重複の場合
			if (doubleCount > noInsertCount){
				this.messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.already.exists.doubleCategoryCode" ));
			}

		}
		else{
			// メッセージ設定
			this.messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"infos.update"));
		}


		ActionMessagesUtil.addMessages(this.httpRequest, this.messages);
		return getInputURL();
	}

	/**
	 *
	 * @return {@link EditCategoryForm}
	 * @see jp.co.arkinfosys.action.master.AbstractEditAction#getActionForm()
	 */
	@Override
	protected AbstractEditForm getActionForm() {
		return this.editCategoryForm;
	}

	/**
	 *
	 * @return URI文字列
	 * @see jp.co.arkinfosys.action.master.AbstractEditAction#getInputURL()
	 */
	@Override
	protected String getInputURL() {
		return "editCategory.jsp";
	}

	/**
	 * 区分マスタ検索画面のURI文字列を返します.
	 * @return URI文字列
	 */
	protected String getSearchURL() {
		return "searchCategory.jsp";
	}

	/**
	 *
	 * @return {@link MENU_ID#MASTER_CATEGORY}
	 * @see jp.co.arkinfosys.action.master.AbstractEditAction#getMenuId()
	 */
	@Override
	protected String getMenuId() {
		return Constants.MENU_ID.MASTER_CATEGORY;
	}

	/**
	 *
	 * @return 区分ID
	 * @see jp.co.arkinfosys.action.master.AbstractEditAction#getKey()
	 */
	@Override
	protected String getKey() {
		return this.editCategoryForm.categoryId;
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
	 *
	 * @return {@link CategoryService}
	 * @see jp.co.arkinfosys.action.master.AbstractEditAction#getService()
	 */
	@Override
	protected AbstractMasterEditService<CategoryDto, Category> getService() {
		return null;
	}

	/**
	 *
	 * @return　プロパティキー文字列
	 * @see jp.co.arkinfosys.action.master.AbstractEditAction#getAlreadyExistsErrorKey()
	 */
	@Override
	protected String getAlreadyExistsErrorKey() {
		return null;
	}
}
