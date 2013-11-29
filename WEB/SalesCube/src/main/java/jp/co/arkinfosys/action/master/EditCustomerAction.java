/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.master;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import jp.co.arkinfosys.common.Categories;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.common.StringUtil;
import jp.co.arkinfosys.dto.master.CustomerDto;
import jp.co.arkinfosys.entity.AuditInfo;
import jp.co.arkinfosys.entity.Customer;
import jp.co.arkinfosys.entity.CustomerRank;
import jp.co.arkinfosys.entity.CustomerRel;
import jp.co.arkinfosys.entity.join.CustomerJoin;
import jp.co.arkinfosys.entity.join.DeliveryAndPre;
import jp.co.arkinfosys.form.master.AbstractEditForm;
import jp.co.arkinfosys.form.master.EditCustomerForm;
import jp.co.arkinfosys.service.AbstractMasterEditService;
import jp.co.arkinfosys.service.CategoryService;
import jp.co.arkinfosys.service.CustomerRankService;
import jp.co.arkinfosys.service.CustomerRelService;
import jp.co.arkinfosys.service.CustomerService;
import jp.co.arkinfosys.service.DeliveryService;
import jp.co.arkinfosys.service.InitMstService;
import jp.co.arkinfosys.service.ZipService;
import jp.co.arkinfosys.service.exception.ServiceException;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.LabelValueBean;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.util.ActionMessagesUtil;
import org.seasar.struts.util.MessageResourcesUtil;

/**
 * 顧客編集画面のアクションクラスです.
 * @author Ark Information Systems
 *
 */
public class EditCustomerAction extends AbstractEditAction<CustomerDto, CustomerJoin> {

	/**
	 * 画面遷移用のマッピングクラスです.
	 * @author Ark Information Systems
	 *
	 */
	protected static class Mapping {
		public static final String INPUT = "editCustomer.jsp";
	}

	/** 関連するアクションフォーム */
	@ActionForm
	@Resource
	public EditCustomerForm editCustomerForm;

	// サービス群
	/** 区分マスタ */
	@Resource
	public CategoryService categoryService;

	/** 得意先マスタ */
	@Resource
	public CustomerService customerService;

	/** 得意先関連マスタ */
	@Resource
	public CustomerRelService customerRelService;

	/** 納入先マスタ */
	@Resource
	public DeliveryService deliveryService;

	/** 顧客ランクマスタ */
	@Resource
	public CustomerRankService customerRankService;

	/** 初期値マスタ */
	@Resource
	public InitMstService initMstService;

	/**
	 * 郵便番号マスタ
	 */
	@Resource
	public ZipService zipService;

	/**
	 * 更新処理時に内容が更新されていたか否か
	 */
	private boolean updateFlag;

	/**
	 * 新規登録時の初期化処理を行います.<br>
	 * 処理終了後、{@link EditCustomerAction#getInputURL()}で定義されたURIに遷移します.
	 * @return 画面遷移先のURI文字列
	 * @throws Exception
	 */
	@Execute(validator = false)
	public String index() throws Exception {
		this.init(null);
		return this.getInputURL();
	}

	/**
	 * 編集モード時の初期化処理を行います.<br>
	 * 処理終了後、{@link EditCustomerAction#getInputURL()}で定義されたURIに遷移します.
	 * @return 画面遷移先のURI文字列
	 * @throws Exception
	 */
	@Execute(validator = false)
	public String edit() throws Exception {
		return super.doEdit(this.getKey());
	}

	/**
	 * 既存データを新規追加モードで開きます.<br>
	 * 処理終了後、{@link EditCustomerAction#getInputURL()}で定義されたURIに遷移します.
	 * @return 画面遷移先のURI文字列
	 * @throws Exception
	 */
	@Execute(validator = false)
	public String load() throws Exception {
		super.doEdit(this.getKey());

		// 新規追加で不要な情報はクリアする
		this.editCustomerForm.customerCode = null;
		this.editCustomerForm.editMode = false;
		this.editCustomerForm.lastCutoffDate = null;
		this.editCustomerForm.creDatetm = null;
		this.editCustomerForm.creDatetmShow = null;
		this.editCustomerForm.updDatetm = null;
		this.editCustomerForm.updDatetmShow = null;
		this.editCustomerForm.lastSalesCutoffDate = null;
		this.editCustomerForm.billTo.deliveryCode = null;
		if(this.editCustomerForm.deliveryList != null) {
			for (DeliveryAndPre dap : this.editCustomerForm.deliveryList) {
				dap.deliveryCode = null;
			}
		}

		return this.getInputURL();
	}

	/**
	 * 登録処理を行います.<br>
	 * 何かしらの問題があった場合および登録完了時に、画面にメッセージを表示します.<br>
	 * 処理終了後、{@link EditCustomerAction#getInputURL()}で定義されたURIに遷移します.
	 * @return 画面遷移先のURI文字列
	 * @throws Exception
	 */
	@Execute(validator = true, validate = "validate", input = Mapping.INPUT, stopOnValidationError = false)
	public String insert() throws Exception {
		return super.doInsert();
	}

	/**
	 * 納入先情報と請求先情報の登録処理を行います.<br>
	 * 何かしらの問題があった場合および登録完了時に、画面にメッセージを表示します.<br>
	 * @param dto 顧客マスタDTO
	 * @throws Exception
	 */
	@Override
	protected void doInsertAfter(CustomerDto dto) throws Exception {
		ActionMessages messages = new ActionMessages();

		// 納入先情報登録
		if (editCustomerForm.deliveryList != null) {
			for (int i = 0; i < editCustomerForm.deliveryList.size(); i++) {
				DeliveryAndPre delivery = editCustomerForm.deliveryList.get(i);
				this.deliveryService.insertDelivery(delivery);
				CustomerRel cr = new CustomerRel();
				cr.customerCode = editCustomerForm.customerCode;
				cr.relCode = String.valueOf(delivery.deliveryCode);
				cr.custRelCategory = Constants.CUSTOMER_REL.DELIVERY;
				this.customerRelService.insertCustomerRel(cr);

				if (!zipService.checkZipCodeAndAddress(
						delivery.deliveryZipCode, delivery.deliveryAddress1)) {
					messages.add(ActionMessages.GLOBAL_MESSAGE,
							new ActionMessage(
									"warns.line.zipcode.address.mismatch",
									i + 1, MessageResourcesUtil
											.getMessage("labels.delivery")));
				}
			}
		}

		// 請求先情報登録
		this.deliveryService.insertDelivery(editCustomerForm.billTo);
		CustomerRel cr = new CustomerRel();
		cr.customerCode = editCustomerForm.customerCode;
		cr.relCode = String.valueOf(editCustomerForm.billTo.deliveryCode);
		cr.custRelCategory = Constants.CUSTOMER_REL.BILLING;
		this.customerRelService.insertCustomerRel(cr);
		if (!zipService.checkZipCodeAndAddress(
				editCustomerForm.billTo.deliveryZipCode,
				editCustomerForm.billTo.deliveryAddress1)) {
			messages.add(ActionMessages.GLOBAL_MESSAGE,
					new ActionMessage("warns.zipcode.address.mismatch",
							MessageResourcesUtil.getMessage("labels.bill")));
		}

		ActionMessagesUtil.addMessages(super.httpRequest, messages);
	}

	/**
	 * 更新処理を行います.<br>
	 * 何かしらの問題があった場合および更新完了時に、画面にメッセージを表示します.<br>
	 * 処理終了後、{@link EditCustomerAction#getInputURL()}で定義されたURIに遷移します.
	 * @return 画面遷移先のURI文字列
	 * @throws Exception
	 */
	@Execute(validator = true, validate = "validate", input = Mapping.INPUT, stopOnValidationError = false)
	public String update() throws Exception {
		return this.doUpdate();
	}

	/**
	 * 基底クラスの更新処理の拡張です.内容が変更されているか否かチェックを行い、変更されていなければ更新を行いません.<br>
	 * 何かしらの問題があった場合および更新完了時に、画面にメッセージを表示します.<br>
	 * @throws Exception
	 */
	@Override
	protected String doUpdate() throws Exception, ServiceException {
		Customer customer = this.customerService
				.findCustomerByCode(this.getKey());
		if (customer == null) {
			super.messages.add(ActionMessages.GLOBAL_MESSAGE,
					new ActionMessage("errors.exclusive.control.deleted"));
			ActionMessagesUtil.addErrors(super.httpRequest, super.messages);
			return this.getInputURL();
		}

		CustomerDto dto = Beans.createAndCopy(CustomerDto.class,
				this.editCustomerForm).execute();

		// 顧客情報更新
		if (!dto.equals(customer)) {
			updateFlag = true;
			super.doUpdate();
		}
		else {
			this.doUpdateAfter(dto);
		}

		return this.getInputURL();
	}

	/**
	 * アクションフォームから顧客DTOクラスへの値コピーを行います.<br>
	 * 支払条件は分割し、それぞれ締グループと回収間隔に設定します.
	 * @return 顧客マスタDTO
	 */
	@Override
	protected CustomerDto createDtoFromActionForm() {
		CustomerDto dto = Beans.createAndCopy(CustomerDto.class,
				this.editCustomerForm).execute();
		dto.cutoffGroup = dto.cutoffGroupCategory.substring(0, 2);
		dto.paybackCycleCategory = dto.cutoffGroupCategory.substring(2, 3);
		return dto;
	}

	/**
	 * 納入先情報と請求先情報の更新処理を行います.<br>
	 * 何かしらの問題があった場合および更新完了時に、画面にメッセージを表示します.<br>
	 * @param dto 顧客マスタDTO
	 * @throws Exception
	 */
	@Override
	protected void doUpdateAfter(CustomerDto dto) throws Exception {
		boolean customerUpdateFlag = updateFlag;

		// 既存納入先情報の取得
		List<DeliveryAndPre> deliveryAndPreList = this.deliveryService.searchDeliveryByCompleteCustomerCodeSortedByCreDate(this.getKey());

		// 納入先情報登録
		if (editCustomerForm.deliveryList != null) {
			for (DeliveryAndPre delivery : editCustomerForm.deliveryList) {
				// 既存データ存在チェック
				boolean exist = false;
				boolean update = false;
				if( delivery.deliveryCode != null ){
					for( DeliveryAndPre dbdap : deliveryAndPreList ){
						if( delivery.deliveryCode.equals(dbdap.deliveryCode)){
							if( !delivery.equals(dbdap) ){
								update = true;
							}
							exist = true;
							deliveryAndPreList.remove(dbdap);
							break;
						}
					}
				}
				if( !exist ){
					// 追加
					this.deliveryService.insertDelivery(delivery);
					CustomerRel cr = new CustomerRel();
					cr.customerCode = editCustomerForm.customerCode;
					cr.relCode = String.valueOf(delivery.deliveryCode);
					cr.custRelCategory = Constants.CUSTOMER_REL.DELIVERY;
					this.customerRelService.insertCustomerRel(cr);
					updateFlag = true;
				}else{
					// 更新
					if( update ){
						this.deliveryService.updateDelivery(delivery);
						updateFlag = true;
					}
				}
			}
			// 削除
			for( DeliveryAndPre dbdap : deliveryAndPreList ){
				this.deliveryService.deleteDelivery(dbdap.deliveryCode);
				this.customerRelService.deleteCustomerRel(
						editCustomerForm.customerCode,
						dbdap.deliveryCode,
						Constants.CUSTOMER_REL.DELIVERY);
				updateFlag = true;
			}
		}

		// 請求先情報登録
		List<DeliveryAndPre> ldapBill = this.deliveryService.searchDeliveryByCompleteCustomerCode( this.editCustomerForm.customerCode);
		if( ldapBill.size() == 1 ){
			if( !editCustomerForm.billTo.equals(ldapBill.get(0)) ){
				this.deliveryService.updateDelivery(editCustomerForm.billTo);
				updateFlag = true;
			}
		}else{
			throw new ServiceException("errors.system");
		}
		this.init(this.editCustomerForm.customerCode);

		// メッセージ設定
		ActionMessages messages = new ActionMessages();
		if( updateFlag ){
			if(!zipService.checkZipCodeAndAddress(dto.customerZipCode,
					dto.customerAddress1)) {
				messages.add(ActionMessages.GLOBAL_MESSAGE,
						new ActionMessage("warns.zipcode.address.mismatch",
								MessageResourcesUtil.getMessage("labels.customer")));
			}

			if (editCustomerForm.deliveryList != null) {
				for(int i = 0; i < editCustomerForm.deliveryList.size(); i++) {
					DeliveryAndPre delivery = editCustomerForm.deliveryList.get(i);
					if(!zipService.checkZipCodeAndAddress(delivery.deliveryZipCode,
							delivery.deliveryAddress1)) {
						messages.add(ActionMessages.GLOBAL_MESSAGE,
								new ActionMessage("warns.line.zipcode.address.mismatch", i + 1,
										MessageResourcesUtil.getMessage("labels.delivery")));
					}
				}
			}

			if(!zipService.checkZipCodeAndAddress(editCustomerForm.billTo.deliveryZipCode,
					editCustomerForm.billTo.deliveryAddress1)) {
				messages.add(ActionMessages.GLOBAL_MESSAGE,
						new ActionMessage("warns.zipcode.address.mismatch",
								MessageResourcesUtil.getMessage("labels.bill")));
			}
		}

		if (!customerUpdateFlag && updateFlag) {
			// 関連テーブルのみ更新時
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"infos.update"));
		} else if (!customerUpdateFlag && !updateFlag) {
			// 更新なし
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"infos.noupdate"));
		}
		ActionMessagesUtil.addMessages(super.httpRequest, messages);
	}

	/**
	 * 削除処理を行います.<br>
	 * 何かしらの問題があった場合および削除完了時に、画面にメッセージを表示します.<br>
	 * 処理実行後、{@link EditCustomerAction#getInputURL()}で定義されたURIに遷移します.
	 * @return 画面遷移先のURI文字列
	 * @throws Exception
	 */
	@Execute(validator = false)
	public String delete() throws Exception {
		return this.doDelete();
	}

	/**
	 * 基底クラスの削除処理の拡張です.顧客が既に伝票等から参照されているかチェックし、参照されていれば削除を行いません.<br>
	 * @throws Exception
	 */
	@Override
	protected String doDelete() throws Exception {
		// 関連データの存在チェック
		Map<String, Object> result = this.customerService
				.countRelations(this.editCustomerForm.customerCode);

		Iterator<Entry<String, Object>> ite = result.entrySet().iterator();
		while (ite.hasNext()) {
			Entry<String, Object> entry = ite.next();
			Number num = (Number) entry.getValue();
			if (num != null && num.longValue() > 0) {
				super.messages.add(ActionMessages.GLOBAL_MESSAGE,
						new ActionMessage("errors.db.delete.relation",
								MessageResourcesUtil
										.getMessage("erroes.db."
												+ entry.getKey())));
			}
		}
		if(super.messages.size() > 0) {
			ActionMessagesUtil.addErrors(super.httpRequest,
					super.messages);
			return this.getInputURL();
		}

		return super.doDelete();
	}
	/**
	 * 基底クラスの初期化処理の拡張です.<br>
	 * 基底クラスの初期化処理後、新規追加の場合は敬称を設定します.<br>
	 * @throws Exception
	 */
	@Override
	protected void init(String key) throws ServiceException {
		super.init(key);

		if (!StringUtil.hasLength(key)) {
			// 新規追加の場合、初期値マスタから値を取る。
			this.initMstService.initBean(Customer.TABLE_NAME, this.editCustomerForm);

			// 敬称
			if (StringUtil.hasLength(this.editCustomerForm.customerPcPreCategory)) {
				editCustomerForm.customerPcPreCategory = this.editCustomerForm.customerPcPreCategory;
				editCustomerForm.newDeliveryPcPreCategory = this.editCustomerForm.customerPcPreCategory;
				editCustomerForm.billTo.deliveryPcPreCategory = this.editCustomerForm.customerPcPreCategory;
			} else {
				editCustomerForm.customerPcPreCategory = Constants.DEFAULT_INIT_VALUE.PC_PRE_CATEGORY;
				editCustomerForm.newDeliveryPcPreCategory = Constants.DEFAULT_INIT_VALUE.PC_PRE_CATEGORY;
				editCustomerForm.billTo.deliveryPcPreCategory = Constants.DEFAULT_INIT_VALUE.PC_PRE_CATEGORY;
			}

			return;
		}
	}

	/**
	 *
	 * @param key 顧客コード
	 * @return {@link CustomerJoin}
	 * @throws ServiceException
	 * @see jp.co.arkinfosys.action.master.AbstractEditAction#loadData(java.lang.String)
	 */
	@Override
	protected AuditInfo loadData(String key) throws ServiceException {
		// 顧客情報を取得する
		return this.customerService
				.findCustomerByCode(key);
	}

	/**
	 *
	 * @param record {@link Customer}
	 * @throws ServiceException
	 * @see jp.co.arkinfosys.action.master.AbstractEditAction#setForm(jp.co.arkinfosys.entity.AuditInfo)
	 */
	@Override
	protected void setForm(AuditInfo record) throws ServiceException {
		super.setForm(record);

		// 納入先リストを作る
		editCustomerForm.deliveryList = deliveryService
				.searchDeliveryListByCompleteCustomerCode(this.getKey());

		// 請求先を設定
		List<DeliveryAndPre> billing = deliveryService
				.searchDeliveryByCompleteCustomerCode(this.getKey());
		if (billing != null && billing.size() > 0) {
			editCustomerForm.billTo = billing.get(0);
		}

		// 支払条件の調整
		editCustomerForm.cutoffGroupCategory = ((Customer)record).cutoffGroup
				+ ((Customer)record).paybackCycleCategory;
	}

	/**
	 * プルダウン要素を作成します.
	 * @throws ServiceException
	 */
	protected void initList() throws ServiceException {
		// 敬称
		this.editCustomerForm.preTypeCategoryList = categoryService
				.findCategoryLabelValueBeanListById(Categories.PRE_TYPE);
		// 先頭に空白を入れる
		this.editCustomerForm.preTypeCategoryList.add(0, new LabelValueBean());

		// 顧客ランク
		List<CustomerRank> list = customerRankService.findAllCustomerRank();
		this.editCustomerForm.customerRankList.clear();
		for (CustomerRank rank : list) {
			this.editCustomerForm.customerRankList.add(new LabelValueBean(rank.rankName,
					rank.rankCode));
		}
		// 先頭に空白を入れる
		this.editCustomerForm.customerRankList.add(0, new LabelValueBean());

		// 受注停止
		this.editCustomerForm.customerRoCategoryList = categoryService
				.findCategoryLabelValueBeanListById(Categories.CUSTOMER_RO_CATEGORY);
		// 先頭に空白を入れる
		this.editCustomerForm.customerRoCategoryList.add(0, new LabelValueBean());

		// 業種
		this.editCustomerForm.customerBusinessCategoryList = categoryService
				.findCategoryLabelValueBeanListById(Categories.CUSTOMER_BUSINESS_CATEGORY);
		// 先頭に空白を入れる
		this.editCustomerForm.customerBusinessCategoryList.add(0, new LabelValueBean());

		// 職種
		this.editCustomerForm.customerJobCategoryList = categoryService
				.findCategoryLabelValueBeanListById(Categories.CUSTOMER_JOB_CATEGORY);
		// 先頭に空白を入れる
		this.editCustomerForm.customerJobCategoryList.add(0, new LabelValueBean());

		// 取引区分
		this.editCustomerForm.salesCmCategoryList = categoryService
				.findCategoryLabelValueBeanListById(Categories.SALES_CM_CATEGORY);

		// 税端数処理
		this.editCustomerForm.taxFractCategoryList = categoryService
				.findCategoryLabelValueBeanListById(Categories.TAX_FRACT_CATEGORY);

		// 税転嫁
		this.editCustomerForm.taxShiftCategoryList = categoryService
				.findCategoryLabelValueBeanListById(Categories.ART_TAX_SHIFT_CATEGORY);

		// 支払方法
		this.editCustomerForm.cutoffGroupList = categoryService
				.findCategoryLabelValueBeanListById(Categories.CUTOFF_GROUP);

		// 回収方法
		this.editCustomerForm.paybackTypeCategoryList = categoryService
				.findCategoryLabelValueBeanListById(Categories.PAYBACK_TYPE_CATEGORY);

		// 請求書発行単位
		this.editCustomerForm.billPrintUnitList = categoryService
				.findCategoryLabelValueBeanListById(Categories.BILL_PRINT_UNIT);

		// 請求書日付有無
		this.editCustomerForm.billDatePrintList = categoryService
				.findCategoryLabelValueBeanListById(Categories.BILL_DATE_PRINT);
	}

	/**
	 *
	 * @return {@link EditCustomerForm}
	 * @see jp.co.arkinfosys.action.master.AbstractEditAction#getActionForm()
	 */
	@Override
	protected AbstractEditForm getActionForm() {
		return this.editCustomerForm;
	}

	/**
	 *
	 * @return {@link CustomerDto}
	 * @see jp.co.arkinfosys.action.master.AbstractEditAction#getDtoClass()
	 */
	@Override
	protected Class<CustomerDto> getDtoClass() {
		return CustomerDto.class;
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
	 * @return 顧客コード
	 * @see jp.co.arkinfosys.action.master.AbstractEditAction#getKey()
	 */
	@Override
	protected String getKey() {
		return this.editCustomerForm.customerCode;
	}

	/**
	 *
	 * @return {@link MENU_ID#MASTER_CUSTOMER}
	 * @see jp.co.arkinfosys.action.master.AbstractEditAction#getMenuId()
	 */
	@Override
	protected String getMenuId() {
		return Constants.MENU_ID.MASTER_CUSTOMER;
	}

	/**
	 *
	 * @return {@link CustomerService}
	 * @see jp.co.arkinfosys.action.master.AbstractEditAction#getService()
	 */
	@Override
	protected AbstractMasterEditService<CustomerDto, CustomerJoin> getService() {
		return this.customerService;
	}

	/**
	 *
	 * @return　プロパティキー文字列
	 * @see jp.co.arkinfosys.action.master.AbstractEditAction#getAlreadyExistsErrorKey()
	 */
	@Override
	protected String getAlreadyExistsErrorKey() {
		return "errors.customer.already.exists";
	}
}
