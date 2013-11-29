/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.master;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.common.Constants.MENU_ID;
import jp.co.arkinfosys.common.StringUtil;
import jp.co.arkinfosys.dto.master.RateDto;
import jp.co.arkinfosys.dto.master.RateTrnDto;
import jp.co.arkinfosys.entity.AuditInfo;
import jp.co.arkinfosys.entity.Rate;
import jp.co.arkinfosys.entity.RateTrn;
import jp.co.arkinfosys.entity.join.RateJoin;
import jp.co.arkinfosys.form.master.AbstractEditForm;
import jp.co.arkinfosys.form.master.EditRateForm;
import jp.co.arkinfosys.service.AbstractMasterEditService;
import jp.co.arkinfosys.service.RateService;
import jp.co.arkinfosys.service.exception.ServiceException;

import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;

/**
 * レートタイプ編集画面のアクションクラスです.
 * @author Ark Information Systems
 *
 */
public class EditRateAction extends AbstractEditAction<RateDto, RateJoin> {

	@ActionForm
	@Resource
	public EditRateForm editRateForm;

	@Resource
	public RateService rateService;

	/**
	 * 画面遷移用のマッピングクラスです.
	 * @author Ark Information Systems
	 *
	 */
	private static class Mapping {
		public static final String INPUT = "editRate.jsp";
	}

	/**
	 * 新規登録時の初期化処理を行います.<br>
	 * 処理終了後、{@link EditRateAction#getInputURL()}で取得したURIに遷移します.
	 * @return 画面遷移先のURI文字列
	 * @throws Exception
	 */
	@Execute(validator = false)
	public String index() throws Exception {
		super.init(null);
		
		// 新規のときは１つだけRateTrnを追加しておく
		this.editRateForm.rateTrnList.add(new RateTrnDto());
		
		return getInputURL();
	}

	/**
	 * 編集モード時の初期化処理を行います.<br>
	 * 処理終了後、{@link EditRateAction#doEdit(String) doEdit()}で取得したURIに遷移します.
	 * @return 画面遷移先のURI文字列
	 * @throws Exception
	 */
	@Execute(validator = false, urlPattern = "edit/{rateId}")
	public String edit() throws Exception {
		
		return doEdit(getKey());
	}

	/**
	 * 登録処理を行います.<br>
	 * 処理終了後、{@link EditRateAction#doInsert()}で取得したURIに遷移します.
	 * @return 画面遷移先のURI文字列
	 * @throws Exception
	 */
	@Execute(validator = true, validate = "validate", input = "index", stopOnValidationError = false)
	public String insert() throws Exception {
		return doInsert();
	}

	/**
	 * 挿入後の処理
	 * 
	 * @param dto マスタのDTO
	 * @throws Exception
	 */
	@Override
	public void doInsertAfter(RateDto dto) throws Exception {
		// IDを引き取る
		editRateForm.rateId = dto.rateId;
	}

	

	/**
	 * 更新処理を行います.<br>
	 * 処理終了後、{@link EditRateAction#doUpdate()}で取得したURIに遷移します.
	 * @return 画面遷移先のURI文字列
	 * @throws Exception
	 */
	@Execute(validator = true, validate = "validate", input = "initEdit", stopOnValidationError = false)
	public String update() throws Exception {
		String result = doUpdate();
		// 削除済みデータをクリア
		this.editRateForm.deletedRateId = "";
		return result;
	}

	/**
	 * 削除処理を行います.<br>
	 * 処理終了後、{@link EditRateAction#doDelete()}で取得したURIに遷移します.
	 * @return 画面遷移先のURI文字列
	 * @throws Exception
	 */
	@Execute(validator = false)
	public String delete() throws Exception {
		String result = doDelete();
		this.editRateForm.initialize();
		this.editRateForm.editMode = false;
		// 新規のときは１つだけRateTrnを追加しておく
		this.editRateForm.rateTrnList.add(new RateTrnDto());
		return result;

	}

	/**
	 *
	 * @return {@link EditRateForm}
	 * @see jp.co.arkinfosys.action.master.AbstractEditAction#getActionForm()
	 */
	@Override
	protected AbstractEditForm getActionForm() {
		return this.editRateForm;
	}

	/**
	 *
	 * @return　プロパティキー文字列
	 * @see jp.co.arkinfosys.action.master.AbstractEditAction#getAlreadyExistsErrorKey()
	 */
	@Override
	protected String getAlreadyExistsErrorKey() {
		return "errors.rack.already.exists";
	}

	/**
	 *
	 * @return {@link RateDto}
	 * @see jp.co.arkinfosys.action.master.AbstractEditAction#getDtoClass()
	 */
	@Override
	protected Class<RateDto> getDtoClass() {
		return RateDto.class;
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
	 * @return レートID
	 * @see jp.co.arkinfosys.action.master.AbstractEditAction#getKey()
	 */
	@Override
	protected String getKey() {
		return this.editRateForm.rateId;
	}

	/**
	 *
	 * @return {@link MENU_ID#MASTER_RATE}
	 * @see jp.co.arkinfosys.action.master.AbstractEditAction#getMenuId()
	 */
	@Override
	protected String getMenuId() {
		return Constants.MENU_ID.MASTER_RATE;
	}

	/**
	 *
	 * @return {@link RateService}
	 * @see jp.co.arkinfosys.action.master.AbstractEditAction#getService()
	 */
	@Override
	protected AbstractMasterEditService<RateDto, RateJoin> getService() {
		return this.rateService;
	}

	/**
	 *
	 * @param key レートID
	 * @return {@link Rate}
	 * @throws ServiceException
	 * @see jp.co.arkinfosys.action.master.AbstractEditAction#loadData(java.lang.String)
	 */
	@Override
	protected AuditInfo loadData(String key) throws ServiceException {
		Rate rate = rateService.findByIdWithoutDate(this.editRateForm.rateId);
		return rate;
	}

	/**
	 * バリデートでエラーになった際の初期化処理を行います.<br>
	 * 処理終了後、{@link EditRateAction#getInputURL()}で取得したURIに遷移します.
	 * @return 画面遷移先のURI文字列
	 */
	@Execute(validator = false)
	public String initEdit() {
		this.editRateForm.editMode = true;
		return getInputURL();
	}

	/**
	 * アクションフォームをクリア後、レート情報を取得しアクションフォームに設定します.
	 * @param record {@link Rate}
	 * @throws ServiceException
	 * @see jp.co.arkinfosys.action.master.AbstractEditAction#setForm(jp.co.arkinfosys.entity.AuditInfo)
	 */
	@Override
	protected void setForm(AuditInfo record) throws ServiceException {

		try {
			super.setForm(record);

			// 一旦クリア
			this.editRateForm.rateTrnList = new ArrayList<RateTrnDto>();

			// レートデータを取得
			Rate rate = (Rate) record;
			List<RateTrn> resultList = rateService.findRateTrnsByRateId(rate.rateId.toString());
			for (RateTrn entity : resultList) {
				RateTrnDto dto = Beans.createAndCopy(RateTrnDto.class, entity)
						.timestampConverter(Constants.FORMAT.TIMESTAMP)
						.timestampConverter(Constants.FORMAT.DATE, "startDate")
						.dateConverter(Constants.FORMAT.DATE).execute();
				this.editRateForm.rateTrnList.add(dto);
			}

			this.editRateForm.rateTrnListSize = this.editRateForm.rateTrnList.size();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

}
