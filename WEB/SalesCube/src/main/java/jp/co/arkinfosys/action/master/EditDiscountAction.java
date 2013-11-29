/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.master;

import java.util.List;

import javax.annotation.Resource;

import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.common.Constants.MENU_ID;
import jp.co.arkinfosys.common.StringUtil;
import jp.co.arkinfosys.dto.master.DiscountDto;
import jp.co.arkinfosys.dto.master.DiscountTrnDto;
import jp.co.arkinfosys.entity.AuditInfo;
import jp.co.arkinfosys.entity.Discount;
import jp.co.arkinfosys.entity.join.DiscountJoin;
import jp.co.arkinfosys.form.master.AbstractEditForm;
import jp.co.arkinfosys.form.master.EditDiscountForm;
import jp.co.arkinfosys.service.DiscountService;
import jp.co.arkinfosys.service.DiscountTrnService;
import jp.co.arkinfosys.service.AbstractMasterEditService;
import jp.co.arkinfosys.service.exception.ServiceException;

import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;

/**
 * 数量割引編集画面のアクションクラスです.
 * @author Ark Information Systems
 *
 */
public class EditDiscountAction extends AbstractEditAction<DiscountDto, DiscountJoin> {

	@ActionForm
	@Resource
	public EditDiscountForm editDiscountForm;

	@Resource
	public DiscountService discountService;

	@Resource
	public DiscountTrnService discountTrnService;

	/**
	 * 画面遷移用のマッピングクラスです.
	 * @author Ark Information Systems
	 *
	 */
	private static class Mapping {
		public static final String INPUT = "editDiscount.jsp";
	}

	/**
	 * 新規登録時の初期化処理を行います.<br>
	 * 処理終了後、{@link EditDiscountAction#getInputURL()}で取得したURIに遷移します.
	 * @return 画面遷移先のURI文字列
	 * @throws Exception
	 */
	@Execute(validator = false)
	public String index() throws Exception {
		super.init(null);
		return getInputURL();
	}

	/**
	 * 編集モード時の初期化処理を行います.<br>
	 * 処理終了後、{@link EditDiscountAction#doEdit(String) doEdit()}で取得したURIに遷移します.
	 * @return 画面遷移先のURI文字列
	 * @throws Exception
	 */
	@Execute(validator = false, urlPattern = "edit/{discountId}")
	public String edit() throws Exception {
		this.editDiscountForm.discountId = StringUtil.decodeSL(this.editDiscountForm.discountId);
		return doEdit(getKey());
	}

	/**
	 * 登録処理を行います.<br>
	 * 処理終了後、{@link EditDiscountAction#doInsert()}で取得したURIに遷移します.
	 * @return 画面遷移先のURI文字列
	 * @throws Exception
	 */
	@Execute(validator = true, validate = "validate", input = Mapping.INPUT, stopOnValidationError = false)
	public String insert() throws Exception {
		return doInsert();
	}

	/**
	 * 更新処理を行います.<br>
	 * 処理終了後、{@link EditDiscountAction#doUpdate()}で取得したURIに遷移します.
	 * @return 画面遷移先のURI文字列
	 * @throws Exception
	 */
	@Execute(validator = true, validate = "validate", input = "initEdit", stopOnValidationError = false)
	public String update() throws Exception {
		return doUpdate();
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
	 * 削除処理を行います.<br>
	 * 処理実行後、{@link EditDiscountAction#doDelete()}で定義されたURIに遷移します.
	 * @return 画面遷移先のURI文字列
	 * @throws Exception
	 */
	@Execute(validator = false)
	public String delete() throws Exception {
		return doDelete();

	}

	/**
	 *
	 * @return {@link EditDiscountForm}
	 * @see jp.co.arkinfosys.action.master.AbstractEditAction#getActionForm()
	 */
	protected AbstractEditForm getActionForm() {
		return this.editDiscountForm;
	}

	/**
	 *
	 * @return {@link MENU_ID#MASTER_DISCOUNT}
	 * @see jp.co.arkinfosys.action.master.AbstractEditAction#getMenuId()
	 */
	protected String getMenuId() {
		return Constants.MENU_ID.MASTER_DISCOUNT;
	}

	/**
	 *
	 * @param key 割引ID
	 * @return {@link Discount}
	 * @throws ServiceException
	 * @see jp.co.arkinfosys.action.master.AbstractEditAction#loadData(java.lang.String)
	 */
	protected AuditInfo loadData(String key) throws ServiceException {
		// 割引情報を取得
		Discount discount = discountService
				.findById(key);
		return discount;
	}

	/**
	 * 割引情報とデフォルト行数をフォームに設定します.
	 * @param record {@link Discount}
	 * @throws ServiceException
	 * @see jp.co.arkinfosys.action.master.AbstractEditAction#setForm(jp.co.arkinfosys.entity.AuditInfo)
	 */
	protected void setForm(AuditInfo record) throws ServiceException {
		if (record == null) {
			return;
		}
		// フォーム入れる
		Discount discount = (Discount) record;
		Beans.copy(discount, this.editDiscountForm).timestampConverter(
				Constants.FORMAT.TIMESTAMP)
				.dateConverter(Constants.FORMAT.DATE).execute();

		// 割引データを取得してフォームに入れる
		List<DiscountTrnDto> list = discountTrnService.findDiscountTrnByDiscountId(discount.discountId);
		this.editDiscountForm.discountTrnList = list;

		// 行数を記録
		this.editDiscountForm.defaultRowCount = list.size();
	}

	/**
	 *
	 * @return 割引ID
	 * @see jp.co.arkinfosys.action.master.AbstractEditAction#getKey()
	 */
	public String getKey() {
		return this.editDiscountForm.discountId;
	}

	/**
	 *
	 * @return　　プロパティキー文字列
	 * @see jp.co.arkinfosys.action.master.AbstractEditAction#getAlreadyExistsErrorKey()
	 */
	protected String getAlreadyExistsErrorKey() {
		return "errors.discount.already.exists";
	}

	/**
	 *
	 * @return {@link DiscountDto}
	 * @see jp.co.arkinfosys.action.master.AbstractEditAction#getDtoClass()
	 */
	protected Class<DiscountDto> getDtoClass() {
		return DiscountDto.class;
	}

	/**
	 *
	 * @return {@link DiscountService}
	 * @see jp.co.arkinfosys.action.master.AbstractEditAction#getService()
	 */
	protected AbstractMasterEditService<DiscountDto, DiscountJoin> getService() {
		return this.discountService;
	}

	/**
	 * 割引情報を登録します.
	 * @param dto {@link DiscountDto}
	 * @throws Exception
	 * @see jp.co.arkinfosys.action.master.AbstractEditAction#doInsertAfter(jp.co.arkinfosys.dto.master.MasterEditDto)
	 */
	protected void doInsertAfter(DiscountDto dto) throws Exception {
		// 割引データ
		for (DiscountTrnDto trnDto : dto.discountTrnList) {
			trnDto.discountId = getKey();

			trnDto.discountDataId = Integer
					.valueOf((int) this.discountTrnService
							.insertDiscountTrn(trnDto));
		}
	}

	/**
	 * 割引情報を更新します.
	 * @param dto {@link DiscountDto}
	 * @throws Exception
	 * @see jp.co.arkinfosys.action.master.AbstractEditAction#doUpdateAfter(jp.co.arkinfosys.dto.master.MasterEditDto)
	 */
	protected void doUpdateAfter(DiscountDto dto) throws Exception {
		// 割引データ更新
		for (DiscountTrnDto trnDto : dto.discountTrnList) {
			trnDto.discountId = dto.discountId;
			if (trnDto.lineNo == null) {
				// 削除されたデータ（後でDELETEされる）
				continue;
			}
			if (trnDto.discountDataId == null) {
				trnDto.discountDataId = Integer
						.valueOf((int) this.discountTrnService
								.insertDiscountTrn(trnDto));
			} else {
				this.discountTrnService.updateDiscountTrn(trnDto);
			}
		}

		// 画面上で削除された割引データ削除
		String deletedDataIdCSV = this.editDiscountForm.deletedDataId;
		if (deletedDataIdCSV != null) {
			String[] deletedArray = deletedDataIdCSV.split(",");
			for (String deletedDataId : deletedArray) {
				if (deletedDataId.length() == 0) {
					continue;
				}
				this.discountTrnService
						.deleteDiscountTrnByDiscountDataId(deletedDataId);
			}
		}
	}

	/**
	 * 割引情報を削除します.
	 * @param dto {@link DiscountDto}
	 * @throws Exception
	 * @see jp.co.arkinfosys.action.master.AbstractEditAction#doDeleteAfter(jp.co.arkinfosys.dto.master.MasterEditDto)
	 */
	protected void doDeleteAfter(DiscountDto dto) throws Exception {
		// 割引データを削除
		for (DiscountTrnDto trnDto : dto.discountTrnList) {
			if (trnDto.discountDataId == null) {
				continue;
			}
			this.discountTrnService
					.deleteDiscountTrnByDiscountDataId(trnDto.discountDataId
							.toString());
		}

		// 画面上で削除された割引データ削除
		String deletedDataIdCSV = this.editDiscountForm.deletedDataId;
		if (deletedDataIdCSV != null) {
			String[] deletedArray = deletedDataIdCSV.split(",");
			for (String deletedDataId : deletedArray) {
				if (deletedDataId.length() == 0) {
					continue;
				}
				this.discountTrnService
						.deleteDiscountTrnByDiscountDataId(deletedDataId);
			}
		}
	}

	/**
	 * バリデートでエラーになった際の初期化処理を行います.<br>
	 * 処理実行後、{@link EditDiscountAction#getInputURL()}で取得したURIに遷移します.
	 * @return 画面遷移先のURI文字列
	 */
	@Execute(validator = false)
	public String initEdit() {
		this.editDiscountForm.editMode = true;
		return getInputURL();
	}


}
