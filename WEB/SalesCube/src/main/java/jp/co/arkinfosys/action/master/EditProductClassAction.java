/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.master;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.common.Constants.MENU_ID;
import jp.co.arkinfosys.common.ListUtil;
import jp.co.arkinfosys.dto.master.ProductClassDto;
import jp.co.arkinfosys.entity.AuditInfo;
import jp.co.arkinfosys.entity.ProductClass;
import jp.co.arkinfosys.form.master.AbstractEditForm;
import jp.co.arkinfosys.form.master.EditProductClassForm;
import jp.co.arkinfosys.service.AbstractMasterEditService;
import jp.co.arkinfosys.service.ProductClassService;
import jp.co.arkinfosys.service.exception.ServiceException;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.LabelValueBean;
import org.seasar.framework.beans.util.BeanMap;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.util.ActionMessagesUtil;

/**
 * 分類編集画面のアクションクラスです.
 * @author Ark Information Systems
 *
 */
public class EditProductClassAction extends
		AbstractEditAction<ProductClassDto, ProductClass> {

	/**
	 * 画面遷移用のマッピングクラスです.
	 * @author Ark Information Systems
	 *
	 */
	private static class Mapping {
		public static final String INPUT = "editProductClass.jsp";
	}

	@ActionForm
	@Resource
	public EditProductClassForm editProductClassForm;

	@Resource
	public ProductClassService productClassService;

	public List<LabelValueBean> classCode1List = new ArrayList<LabelValueBean>();

	public List<LabelValueBean> classCode2List = new ArrayList<LabelValueBean>();

	/**
	 * 新規登録時の初期化処理を行います.<br>
	 * 処理終了後、{@link EditProductClassAction#getInputURL()}で取得したURIに遷移します.
	 * @return 画面遷移先のURI文字列
	 * @throws Exception
	 */
	@Execute(validator = false)
	public String index() throws Exception {
		super.init(null);
		classCode1List = ListUtil.addEmptyLabelValue(classCode1List);
		classCode2List = ListUtil.addEmptyLabelValue(classCode2List);

		// 分類（大）のコード
		Map<String, Object> conditions = new HashMap<String, Object>();
		conditions.put(ProductClassService.Param.TARGET_COLUMN, ProductClassService.COLUMN_CLASS_CODE_1);
		String nextVal = productClassService.getNextCode(conditions);
		this.editProductClassForm.classCode = nextVal;
		return getInputURL();
	}

	/**
	 * 編集モード時の初期化処理を行います.<br>
	 * 処理終了後、{@link EditProductClassAction#doEdit(String) doEdit()}で取得したURIに遷移します.
	 * @return 画面遷移先のURI文字列
	 * @throws Exception
	 */
	@Execute(validator = false, urlPattern = "edit/{classCode}")
	public String edit() throws Exception {
		return doEdit(this.editProductClassForm.classCode);
	}

	/**
	 * 登録処理を行います.<br>
	 * 処理完了後、{@link EditProductClassAction#doInsert()}で取得したURIに遷移します.
	 * @return 画面遷移先のURI文字列
	 * @throws Exception
	 */
	@Execute(validator = true, validate = "validate", input = "initEdit", stopOnValidationError = false)
	public String insert() throws Exception {
		ProductClassDto tempDto = this.createSearchDto();

		// 値を入れておく
		String classCode1 = this.editProductClassForm.classCode1;
		String classCode2 = this.editProductClassForm.classCode2;
		if (tempDto != null) {
			this.editProductClassForm.classCode1 = tempDto.classCode1;
			this.editProductClassForm.classCode2 = tempDto.classCode2;
		}

		String result = doInsert();

		// 値を戻しておく
		this.editProductClassForm.classCode1 = classCode1;
		this.editProductClassForm.classCode2 = classCode2;
		return result;
	}

	/**
	 * 更新処理を行います.<br>
	 * 処理終了後、{@link EditProductClassAction#doUpdate()}で取得したURIに遷移します.
	 * @return 画面遷移先のURI
	 * @throws Exception
	 */
	@Execute(validator = true, validate = "validate", input = "initEdit", stopOnValidationError = false)
	public String update() throws Exception {
		ProductClassDto tempDto = this.createSearchDto();

		// 値を入れておく
		String classCode1 = this.editProductClassForm.classCode1;
		String classCode2 = this.editProductClassForm.classCode2;
		if (tempDto != null) {
			this.editProductClassForm.classCode1 = tempDto.classCode1;
			this.editProductClassForm.classCode2 = tempDto.classCode2;
		}

		String result = doUpdate();

		// 値を戻しておく
		this.editProductClassForm.classCode1 = classCode1;
		this.editProductClassForm.classCode2 = classCode2;

		return result;
	}

	/**
	 * 削除処理を行います.<br>
	 * 何かしらの問題があった場合、画面にメッセージを表示します.<br>
	 * 正常終了時は{@link EditProductClassAction#doDelete()}で取得した、失敗時は{@link Mapping#INPUT}で定義されたURIに遷移します.
	 * @return 画面遷移先のURI
	 * @throws Exception
	 */
	@Execute(validator = false)
	public String delete() throws Exception {
		// 値を入れておく
		setClassCode();

		// 子分類があるかどうかチェックする
		BeanMap params = Beans.createAndCopy(BeanMap.class,
				this.editProductClassForm).excludesWhitespace().execute();

		params.remove("className");

		// 検索結果件数を取得する
		int count = this.productClassService.countByCondition(params);

		if(count > 1) {
			super.messages.add(ActionMessages.GLOBAL_MESSAGE,
					new ActionMessage("errors.productclass.childexists"));
			ActionMessagesUtil.addErrors(super.httpRequest, super.messages);
			this.editProductClassForm.editMode = true;
			initList();
			return Mapping.INPUT;
		}

		String result = doDelete();

		this.editProductClassForm.initialize();
		this.classCode1List = new ArrayList<LabelValueBean>();
		this.classCode2List = new ArrayList<LabelValueBean>();
		return result;
	}

	/**
	 *
	 * @return {@link EditProductClassForm}
	 * @see jp.co.arkinfosys.action.master.AbstractEditAction#getActionForm()
	 */
	@Override
	protected AbstractEditForm getActionForm() {
		return this.editProductClassForm;
	}

	/**
	 *
	 * @return　プロパティキー文字列
	 * @see jp.co.arkinfosys.action.master.AbstractEditAction#getAlreadyExistsErrorKey()
	 */
	@Override
	protected String getAlreadyExistsErrorKey() {
		return "errors.productclass.already.exists";
	}

	/**
	 *
	 * @return {@link ProductClassDto}
	 * @see jp.co.arkinfosys.action.master.AbstractEditAction#getDtoClass()
	 */
	@Override
	protected Class<ProductClassDto> getDtoClass() {
		return ProductClassDto.class;
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
	 * @return 分類コード
	 * @see jp.co.arkinfosys.action.master.AbstractEditAction#getKey()
	 */
	@Override
	protected String getKey() {
		if (this.editProductClassForm.targetClass != null) {
			if ("1".equals(this.editProductClassForm.targetClass)) {
				// 分類（大）の場合
				return this.editProductClassForm.classCode + "--";
			}
			if ("2".equals(this.editProductClassForm.targetClass)) {
				// 分類（中）の場合
				return this.editProductClassForm.classCode1 + "-"
						+ this.editProductClassForm.classCode + "-";
			}
			if ("3".equals(this.editProductClassForm.targetClass)) {
				// 分類（小）の場合
				return this.editProductClassForm.classCode1 + "-"
						+ this.editProductClassForm.classCode2 + "-"
						+ this.editProductClassForm.classCode;
			}
		} else {
			return this.editProductClassForm.classCode;
		}
		// ありえない・・・
		return null;
	}

	/**
	 *
	 * @return {@link MENU_ID#MASTER_PRODUCT_CLASS}
	 * @see jp.co.arkinfosys.action.master.AbstractEditAction#getMenuId()
	 */
	@Override
	protected String getMenuId() {
		return Constants.MENU_ID.MASTER_PRODUCT_CLASS;
	}

	/**
	 *
	 * @return {@link ProductClassService}
	 * @see jp.co.arkinfosys.action.master.AbstractEditAction#getService()
	 */
	@Override
	protected AbstractMasterEditService<ProductClassDto, ProductClass> getService() {
		return productClassService;
	}

	/**
	 *
	 * @param key
	 * @return {@link ProductClass}
	 * @throws ServiceException
	 * @see jp.co.arkinfosys.action.master.AbstractEditAction#loadData(java.lang.String)
	 */
	@Override
	protected AuditInfo loadData(String key) throws ServiceException {
		if (key == null) {
			return null;
		}
		ProductClassDto dto = createSearchDto();
		ProductClass result = productClassService.findProductClassByKey(dto);
		if (result == null) {
			if (this.editProductClassForm.editMode) {
				this.messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
				"infos.delete"));
				ActionMessagesUtil.addErrors(this.httpRequest, this.messages);
				this.editProductClassForm.targetClass = "1";	// とりあえず分類（大）
				this.editProductClassForm.classCode = "";
			} else {
				// 新規のここでコードをセットしておく
				setClassCode();
			}
			return null;
		}
		if (result.classCode2.length() == 0 && result.classCode3.length() == 0) {
			// 分類（大）
			this.editProductClassForm.targetClass = "1";
			this.editProductClassForm.classCode = result.classCode1;
			this.editProductClassForm.classCode3 = "";
		} else if (result.classCode3.length() == 0) {
			this.editProductClassForm.targetClass = "2";
			this.editProductClassForm.classCode = result.classCode2;
			this.editProductClassForm.classCode3 = "";
		} else {
			this.editProductClassForm.targetClass = "3";
			this.editProductClassForm.classCode = result.classCode3;
			this.editProductClassForm.classCode3 = result.classCode3;
		}

		return result;
	}

	/**
	 * 分類コードを設定します.
	 */
	private void setClassCode() {
		if ("1".equals(this.editProductClassForm.targetClass)) {
			this.editProductClassForm.classCode1 = this.editProductClassForm.classCode;
			this.editProductClassForm.classCode2 = "";
			this.editProductClassForm.classCode3 = "";
		} else if ("2".equals(this.editProductClassForm.targetClass)) {
			this.editProductClassForm.classCode2 = this.editProductClassForm.classCode;
			this.editProductClassForm.classCode3 = "";
		} else if ("3".equals(this.editProductClassForm.targetClass)) {
			this.editProductClassForm.classCode3 = this.editProductClassForm.classCode;
		}
	}

	/**
	 * 検索用の分類DTOを作成します.
	 * @return 検索用分類DTO
	 */
	private ProductClassDto createSearchDto() {
		ProductClassDto dto = new ProductClassDto();
		if (getKey() == null) {
			return dto;
		}
		String[] values = getKey().split("-");
		if (values.length == 1) {
			dto.classCode1 = values[0];
			dto.classCode2 = "";
			dto.classCode3 = "";
		} else if (values.length == 2) {
			dto.classCode1 = values[0];
			dto.classCode2 = values[1];
			dto.classCode3 = "";
		} else if (values.length == 3) {
			dto.classCode1 = values[0];
			dto.classCode2 = values[1];
			dto.classCode3 = values[2];
		} else {
			return null;
		}
		return dto;
	}

	/**
	 * 分類プルダウン要素を作成します.
	 * @throws ServiceException
	 * @see jp.co.arkinfosys.action.master.AbstractEditAction#initList()
	 */
	@Override
	protected void initList() throws ServiceException {
		classCode1List = ListUtil.addEmptyLabelValue(classCode1List);
		classCode2List = ListUtil.addEmptyLabelValue(classCode2List);
		ProductClassDto dto = createSearchDto();
		if ("1".equals(this.editProductClassForm.targetClass)) {
			// 分類（大）
			classCode1List = ListUtil.addEmptyLabelValue(classCode1List);
			classCode2List = ListUtil.addEmptyLabelValue(classCode2List);
		} else if ("2".equals(this.editProductClassForm.targetClass)) {
			// 分類（中）
			classCode1List = new ArrayList<LabelValueBean>();
			// 分類（大）を検索する（classCode2を空欄にして、検索条件を分類１コードだけにする）
			dto.classCode2 = "";
			ProductClass product = productClassService.findProductClassByKey(dto);
			classCode1List.add(new LabelValueBean(product.className, product.classCode1));
			classCode2List = ListUtil.addEmptyLabelValue(classCode2List);
		} else {
			// 分類（小）
			classCode1List = new ArrayList<LabelValueBean>();
			classCode2List = new ArrayList<LabelValueBean>();
			if (this.editProductClassForm.editMode) {
				// 分類（中）を検索する（classCode3を空欄にして、検索条件を分類１コード、分類２コードにする）
				dto.classCode3 = "";
				ProductClass product2 = productClassService.findProductClassByKey(dto);
				// 分類（大）を検索する（さらにclassCode2を空欄にして、検索条件を分類１コードだけにする）
				dto.classCode2 = "";
				ProductClass product1 = productClassService.findProductClassByKey(dto);
				classCode1List.add(new LabelValueBean(product1.className, product1.classCode1));
				classCode2List.add(new LabelValueBean(product2.className, product2.classCode2));
			} else {
				// 分類（大）のリストを取得する
				classCode1List = productClassService.findAllProductClass1LabelValueBeanList();

				// 分類（中）のリストを取得する
				classCode2List = productClassService.findAllProductClass2LabelValueBeanList(this.editProductClassForm.classCode1);
			}

		}
	}

	/**
	 * バリデートでエラーになった際の初期化処理を行います.<br>
	 * 処理実行後、{@link EditProductClassAction#getInputURL()}で取得したURIに遷移します.
	 * @return 画面遷移先のURI文字列
	 * @throws ServiceException
	 */
	@Execute(validator = false)
	public String initEdit() throws ServiceException {
		initList();
		return Mapping.INPUT;
	}
}
