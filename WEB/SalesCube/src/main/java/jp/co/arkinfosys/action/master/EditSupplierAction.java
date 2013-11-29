/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.master;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import jp.co.arkinfosys.common.Categories;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.common.Constants.MENU_ID;
import jp.co.arkinfosys.common.StringUtil;
import jp.co.arkinfosys.dto.master.SupplierDto;
import jp.co.arkinfosys.entity.AuditInfo;
import jp.co.arkinfosys.entity.Rate;
import jp.co.arkinfosys.entity.Supplier;
import jp.co.arkinfosys.entity.join.SupplierJoin;
import jp.co.arkinfosys.form.master.AbstractEditForm;
import jp.co.arkinfosys.form.master.EditSupplierForm;
import jp.co.arkinfosys.service.CategoryService;
import jp.co.arkinfosys.service.InitMstService;
import jp.co.arkinfosys.service.AbstractMasterEditService;
import jp.co.arkinfosys.service.RateService;
import jp.co.arkinfosys.service.SupplierService;
import jp.co.arkinfosys.service.exception.ServiceException;

import org.apache.struts.util.LabelValueBean;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;

/**
 * 仕入先編集画面のアクションクラスです.
 * @author Ark Information Systems
 *
 */
public class EditSupplierAction extends AbstractEditAction<SupplierDto, SupplierJoin> {

	/**
	 * 画面遷移用のマッピングクラスです.
	 * @author Ark Information Systems
	 *
	 */
	public static class Mapping {
		public static final String INPUT = "editSupplier.jsp";
	}

	/**
	 * 初期設定を行うカラム定義クラスです.
	 * @author Ark Information Systems
	 *
	 */
	private static class InitColumn {
		/** 敬称 */
		public static final String SUPPLIER_PC_PRE_CATEGORY = "SUPPLIER_PC_PRE_CATEGORY";
		/** 取引区分 */
		public static final String SUPPLIER_CM_CATEGORY = "SUPPLIER_CM_CATEGORY";
		/** 税転嫁 */
		public static final String TAX_SHIFT_CATEGORY = "TAX_SHIFT_CATEGORY";
		/** 税端数処理 */
		public static final String TAX_FRACT_CATEGORY = "TAX_FRACT_CATEGORY";
		/** 単価端数処理 */
		public static final String PRICE_FRACT_CATEGORY = "PRICE_FRACT_CATEGORY";
	}

	/** 関連するアクションフォーム */
	@ActionForm
	@Resource
	public EditSupplierForm editSupplierForm;

	// サービス群
	/** 区分マスタ */
	@Resource
	public CategoryService categoryService;

	/** 初期値マスタ */
	@Resource
	public InitMstService initMstService;

	/** レートマスタ */
	@Resource
	public RateService rateService;

	/** 仕入先マスタ */
	@Resource
	public SupplierService supplierService;

	// リスト
	/** 敬称 */
	public List<LabelValueBean> supplierPcPreTypeCategoryList = new ArrayList<LabelValueBean>();

	/** 取引区分 */
	public List<LabelValueBean> supplierCmCategoryList = new ArrayList<LabelValueBean>();

	/** 税転嫁 */
	public List<LabelValueBean> taxShiftCategoryList = new ArrayList<LabelValueBean>();

	/** 支払方法 */
	public List<LabelValueBean> paymentTypeCategoryList = new ArrayList<LabelValueBean>();

	/** 回収間隔 */
	public List<LabelValueBean> paymentCycleCategoryList = new ArrayList<LabelValueBean>();

	/** レート */
	public List<LabelValueBean> rateList = new ArrayList<LabelValueBean>();

	/** 振込方法 */
	public List<LabelValueBean> transferTypeCategoryList = new ArrayList<LabelValueBean>();

	/** 税端数処理 */
	public List<LabelValueBean> taxFractCategoryList = new ArrayList<LabelValueBean>();

	/** 単価端数処理 */
	public List<LabelValueBean> priceFractCategoryList = new ArrayList<LabelValueBean>();

	/** 手数料負担 */
	public List<LabelValueBean> serviceChargeCategoryList = new ArrayList<LabelValueBean>();

	/**
	 * 新規登録時の初期化処理を行います.<br>
	 * 処理終了後、{@link Mapping#INPUT}で定義されたURIに遷移します.
	 * @return 画面遷移先のURI文字列
	 * @throws Exception
	 */
	@Execute(validator = false)
	public String index() throws Exception {
		try {
			this.init(null);
			this.editSupplierForm.editMode = false;
		} catch (ServiceException e) {
			super.errorLog(e);
			throw new ServiceException(e);
		}

		return Mapping.INPUT;
	}

	/**
	 * 編集モード時の初期化処理を行います.<br>
	 * 処理終了後、{@link EditSupplierAction#doEdit(String) doEdit()}で取得したURIに遷移します.
	 * @return 画面遷移先のURI文字列
	 * @throws Exception
	 */
	@Execute(validator = false, urlPattern = "edit/{supplierCode}")
	public String edit() throws Exception {
		this.editSupplierForm.supplierCode = StringUtil.decodeSL(this.editSupplierForm.supplierCode);
		return doEdit(this.editSupplierForm.supplierCode);
	}

	/**
	 * 登録処理を行います.<br>
	 * 処理終了後、{@link EditSupplierAction#doInsert()}}で取得したURIに遷移します.
	 * @return 画面遷移先のURI文字列
	 * @throws Exception
	 */
	@Execute(validator = true, validate = "validate", input = "init", stopOnValidationError = false)
	public String insert() throws Exception {
		return doInsert();
	}

	/**
	 * 更新処理を行います.<br>
	 * 処理終了後、{@link EditSupplierAction#doUpdate()}で取得したURIに遷移します.
	 * @return 画面遷移先のURI文字列
	 * @throws Exception
	 */
	@Execute(validator = true, validate = "validate", input = "initEdit", stopOnValidationError = false)
	public String update() throws Exception {
		return doUpdate();
	}

	/**
	 * 削除処理を行います.<br>
	 * 処理終了後、{@link EditSupplierAction#doDelete()}で取得したURIに遷移します.
	 * @return 画面遷移先のURI文字列
	 * @throws Exception
	 */
	@Execute(validator = false)
	public String delete() throws Exception {
		return doDelete();
	}

	/**
	 * {@link EditSupplierAction#insert()}のバリデートでエラーになった際の初期化処理を行います.<br>
	 * 処理終了後、{@link Mapping#INPUT}で定義されたURIに遷移します.
	 * @return 画面遷移先のURI文字列
	 * @throws Exception
	 */
	@Execute(validator = false)
	public String init() throws Exception {
		this.init(null);
		return Mapping.INPUT;
	}

	/**
	 * {@link EditSupplierAction#update()}のバリデートでエラーになった際の初期化処理を行います.<br>
	 * 処理終了後、{@link Mapping#INPUT}で定義されたURIに遷移します.
	 * @return 画面遷移先のURI文字列
	 * @throws Exception
	 */
	@Execute(validator = false)
	public String initEdit() throws Exception {
		this.editSupplierForm.editMode = true;
		this.initList();
		return Mapping.INPUT;
	}

	/**
	 *
	 * @return {@link EditSupplierForm}
	 * @see jp.co.arkinfosys.action.master.AbstractEditAction#getActionForm()
	 */
	@Override
	protected AbstractEditForm getActionForm() {
		return this.editSupplierForm;
	}

	/**
	 *
	 * @return　プロパティキー文字列
	 * @see jp.co.arkinfosys.action.master.AbstractEditAction#getAlreadyExistsErrorKey()
	 */
	@Override
	protected String getAlreadyExistsErrorKey() {
		return "errors.supplier.already.exists";
	}

	/**
	 *
	 * @return {@link SupplierDto}
	 * @see jp.co.arkinfosys.action.master.AbstractEditAction#getDtoClass()
	 */
	@Override
	protected Class<SupplierDto> getDtoClass() {
		return SupplierDto.class;
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
	 * @return 仕入先コード
	 * @see jp.co.arkinfosys.action.master.AbstractEditAction#getKey()
	 */
	@Override
	protected String getKey() {
		return this.editSupplierForm.supplierCode;
	}

	/**
	 *
	 * @return {@link MENU_ID#MASTER_SUPPLIER}
	 * @see jp.co.arkinfosys.action.master.AbstractEditAction#getMenuId()
	 */
	@Override
	protected String getMenuId() {
		return Constants.MENU_ID.MASTER_SUPPLIER;
	}

	/**
	 *
	 * @return {@link SupplierService}
	 * @see jp.co.arkinfosys.action.master.AbstractEditAction#getService()
	 */
	@Override
	protected AbstractMasterEditService<SupplierDto, SupplierJoin> getService() {
		return this.supplierService;
	}

	/**
	 *
	 * @param key 仕入先コード
	 * @return {@link SupplierJoin}
	 * @throws ServiceException
	 * @see jp.co.arkinfosys.action.master.AbstractEditAction#loadData(java.lang.String)
	 */
	@Override
	protected AuditInfo loadData(String key) throws ServiceException {
		if (key == null) {
			return null;
		}
		SupplierJoin supplier = this.supplierService.findById(key);

		if (supplier == null) {
			return null;
		}
		return supplier;
	}

	/**
	 *
	 * @param record {@link SupplierJoin}
	 * @throws ServiceException
	 * @see jp.co.arkinfosys.action.master.AbstractEditAction#setForm(jp.co.arkinfosys.entity.AuditInfo)
	 */
	@Override
	protected void setForm(AuditInfo record) throws ServiceException {
		if (record != null) {
			SupplierJoin supplier = (SupplierJoin) record;
			Beans.copy(supplier, this.editSupplierForm).timestampConverter(
					Constants.FORMAT.TIMESTAMP)
					.dateConverter(Constants.FORMAT.DATE).execute();
		}

	}

	/**
	 * プルダウン要素を作成します.
	 * @throws ServiceException
	 * @see jp.co.arkinfosys.action.master.AbstractEditAction#initList()
	 */
	protected void initList() throws ServiceException {
		//
		// その他初期化
		//

		// 敬称
		supplierPcPreTypeCategoryList = categoryService
				.findCategoryLabelValueBeanListById(Categories.PRE_TYPE);
		// 先頭に空白を入れる
		supplierPcPreTypeCategoryList.add(0, new LabelValueBean());

		// 取引区分
		supplierCmCategoryList = categoryService
				.findCategoryLabelValueBeanListById(Categories.SUPPLIER_CM_CATEGORY);
		// 先頭に空白を入れる
		supplierCmCategoryList.add(0, new LabelValueBean());

		// 税転嫁
		taxShiftCategoryList = categoryService
				.findCategoryLabelValueBeanListById(Categories.APT_TAX_SHIFT_CATEGORY);
		// 先頭に空白を入れる
		taxShiftCategoryList.add(0, new LabelValueBean());

		// 支払方法
		paymentTypeCategoryList = categoryService
				.findCategoryLabelValueBeanListById(Categories.PAYMENT_TYPE_CATEGORY);

		// 先頭に空白を入れる
		paymentTypeCategoryList.add(0, new LabelValueBean());

		// 回収間隔
		paymentCycleCategoryList = categoryService
				.findCategoryLabelValueBeanListById(Categories.PAYMENT_CYCLE_CATEGORY);
		// 先頭に空白を入れる
		paymentCycleCategoryList.add(0, new LabelValueBean());

		// レート
		List<Rate> list = rateService.findAllRate();
		for (Rate rate : list) {
			rateList.add(new LabelValueBean(rate.name, rate.rateId.toString()));
		}
		// 先頭に空白を入れる
		rateList.add(0, new LabelValueBean());

		// 振込方法
		transferTypeCategoryList = categoryService
				.findCategoryLabelValueBeanListById(Categories.TRANSFER_TYPE_CATEGORY);
		// 先頭に空白を入れる
		transferTypeCategoryList.add(0, new LabelValueBean());

		// 税端数処理
		taxFractCategoryList = categoryService
				.findCategoryLabelValueBeanListById(Categories.TAX_FRACT_CATEGORY);
		// 先頭に空白を入れる
		taxFractCategoryList.add(0, new LabelValueBean());

		// 単価端数処理
		priceFractCategoryList = categoryService
				.findCategoryLabelValueBeanListById(Categories.PRICE_FRACT_CATEGORY);
		// 先頭に空白を入れる
		priceFractCategoryList.add(0, new LabelValueBean());

		// 手数料負担
		serviceChargeCategoryList = categoryService
				.findCategoryLabelValueBeanListById(Categories.SERVICE_CHARGE_CATEGORY);
		// 先頭に空白を入れる
		serviceChargeCategoryList.add(0, new LabelValueBean());

		if (!StringUtil.hasLength(getKey())) {
			// 新規追加の場合、初期値マスタから値を取る。
			Map<String, Object> initMap = initMstService
					.findInitDataByTableName(Supplier.TABLE_NAME);

			// ※以下、ダウンキャストはテーブル定義に則っているので安全

			// 敬称
			String initVal = (String) initMap.get(InitColumn.SUPPLIER_PC_PRE_CATEGORY);
			if (initVal != null) {
				editSupplierForm.supplierPcPreCategory = initVal;
			} else {
				editSupplierForm.supplierPcPreCategory = Constants.DEFAULT_INIT_VALUE.PC_PRE_CATEGORY;
			}

			// 取引区分の初期値
			initVal = (String) initMap
					.get(InitColumn.SUPPLIER_CM_CATEGORY);
			if (initVal != null) {
				editSupplierForm.supplierCmCategory = initVal;
			} else {
				editSupplierForm.supplierCmCategory = Constants.DEFAULT_INIT_VALUE.SUPPLIER_CM_CATEGORY;
			}

			// 税転嫁
			initVal = (String) initMap
					.get(InitColumn.TAX_SHIFT_CATEGORY);
			if (initVal != null) {
				editSupplierForm.taxShiftCategory = initVal;
			} else {
				editSupplierForm.taxShiftCategory = Constants.DEFAULT_INIT_VALUE.TAX_SHIFT_CATEGORY;
			}

			// 税端数処理
			initVal = (String) initMap
					.get(InitColumn.TAX_FRACT_CATEGORY);
			if (initVal != null) {
				editSupplierForm.taxFractCategory = initVal;
			} else {
				editSupplierForm.taxFractCategory = Constants.DEFAULT_INIT_VALUE.TAX_FRACT_CATEGORY;
			}

			// 単価端数処理
			initVal = (String) initMap
					.get(InitColumn.PRICE_FRACT_CATEGORY);
			if (initVal != null) {
				editSupplierForm.priceFractCategory = initVal;
			} else {
				editSupplierForm.priceFractCategory = Constants.DEFAULT_INIT_VALUE.PRICE_FRACT_CATEGORY;
			}

			return;
		}
	}



}
