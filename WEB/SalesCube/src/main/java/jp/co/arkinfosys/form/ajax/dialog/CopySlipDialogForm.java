/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.form.ajax.dialog;

import java.util.List;

import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.LabelValueBean;
import org.seasar.struts.annotation.Required;

/**
 * 伝票呼出ダイアログのアクションフォームクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class CopySlipDialogForm {

	/**
	 * ダイアログID
	 */
	@Required
	public String dialogId;

	/**
	 * 呼び出し画面ID
	 */
	public String menuId;

	/**
	 * 伝票ID
	 */
	public String slipName;

	/**
	 * 検索条件初期値(DateFrom)
	 */
	public String dateFrom;

	/**
	 * 見積伝票検索条件
	 */
	public CopySlipEstimateConditionForm estimateCondition;

	/**
	 * 受注伝票検索条件
	 */
	public CopySlipROrderConditionForm rorderCondition;

	/**
	 * 売上伝票検索条件
	 */
	public CopySlipSalesConditionForm salesCondition;

	/**
	 * 入金伝票検索条件
	 */
	public CopySlipDepositConditionForm depositCondition;

	/**
	 * 発注伝票検索条件
	 */
	public CopySlipPOrderConditionForm porderCondition;

	/**
	 * 委託発注伝票検索条件
	 */
	public CopySlipEntrustPOrderConditionForm entrustPorderCondition;

	/**
	 * 仕入伝票検索条件
	 */
	public CopySlipSupplierConditionForm supplierCondition;

	/**
	 * 委託入出庫区分
	 */
	public String copySlipEntrustEadCategory;

	/**
	 * 委託入出庫区分の選択値リスト
	 */
	public List<LabelValueBean> entrustCategoryList;

	/**
	 * 検索結果件数
	 */
	public int searchResultCount;

	/**
	 * 検索結果リスト
	 */
	public List<?> searchResultList;

	/**
	 * 入力をチェックします.
	 *
	 * @return　表示するメッセージ
	 */
	public ActionMessages validate() {
		ActionMessages errors = new ActionMessages();

		if (CopySlipEstimateConditionForm.SLIP_NAME.equals(slipName)) {
			errors.add(this.estimateCondition.validate());
		}
		if (CopySlipROrderConditionForm.SLIP_NAME.equals(slipName)) {
			errors.add(this.rorderCondition.validate());
		}
		if (CopySlipSalesConditionForm.SLIP_NAME.equals(slipName)) {
			errors.add(this.salesCondition.validate());
		}
		if (CopySlipDepositConditionForm.SLIP_NAME.equals(slipName)) {
			errors.add(this.depositCondition.validate());
		}
		if (CopySlipPOrderConditionForm.SLIP_NAME.equals(slipName)) {
			errors.add(this.porderCondition.validate());
		}
		if (CopySlipEntrustPOrderConditionForm.SLIP_NAME.equals(slipName)) {
			errors.add(this.entrustPorderCondition.validate());
		}
		if (CopySlipSupplierConditionForm.SLIP_NAME.equals(slipName)) {
			errors.add(this.supplierCondition.validate());
		}
		return errors;
	}
}
