/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.estimate;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.List;

import javax.annotation.Resource;

import jp.co.arkinfosys.action.AbstractSlipEditAction;
import jp.co.arkinfosys.common.Categories;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.common.DiscountUtil;
import jp.co.arkinfosys.common.ListUtil;
import jp.co.arkinfosys.common.StringUtil;
import jp.co.arkinfosys.dto.AbstractSlipDto;
import jp.co.arkinfosys.dto.estimate.InputEstimateDto;
import jp.co.arkinfosys.dto.estimate.InputEstimateLineDto;
import jp.co.arkinfosys.entity.EstimateLineTrn;
import jp.co.arkinfosys.entity.EstimateSheetTrn;
import jp.co.arkinfosys.form.AbstractSlipEditForm;
import jp.co.arkinfosys.form.estimate.InputEstimateForm;
import jp.co.arkinfosys.service.AbstractLineService;
import jp.co.arkinfosys.service.AbstractService;
import jp.co.arkinfosys.service.AbstractSlipService;
import jp.co.arkinfosys.service.CategoryService;
import jp.co.arkinfosys.service.CustomerService;
import jp.co.arkinfosys.service.EstimateLineService;
import jp.co.arkinfosys.service.EstimateSheetService;
import jp.co.arkinfosys.service.exception.ServiceException;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.LabelValueBean;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.util.MessageResourcesUtil;

/**
 * 見積入力画面のアクションクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class InputEstimateAction extends
		AbstractSlipEditAction<InputEstimateDto, InputEstimateLineDto> {

	/**
	 * 画面遷移用のマッピングクラスです.
	 */
	public static class Mapping {
		public static final String INPUT = "inputEstimate.jsp";

	}

	@ActionForm
	@Resource
	public InputEstimateForm inputEstimateForm;

	@Resource
	private EstimateSheetService estimateSheetService;
	@Resource
	private EstimateLineService estimateLineService;

	@Resource
	private CategoryService categoryService;

	@Resource
	private CustomerService customerService;

	/**
	 * 提出先　敬称プルダウン
	 */
	public List<LabelValueBean> submitPreList;

	/**
	 *  消費税率プルダウン
	 */
	public List<LabelValueBean> ctaxRateList;

	/**
	 * 画面表示に使用しているプルダウン等の情報を作成します.
	 */
	protected void createList() throws Exception {

		try {

			// 敬称プルダウンリスト
			submitPreList = categoryService
					.findCategoryLabelValueBeanListById(Categories.PRE_TYPE);

			// 消費税率プルダウンリスト
			this.ctaxRateList =  ListUtil.getRateTaxNoBlankList(super.taxRateService);

		} catch (ServiceException e) {
			super.errorLog(e);
			throw e;
		}
	}

	/**
	 * 伝票データを取得します.
	 * @return 読み込みできたか否か
	 * @throws Exception
	 * @throws ServiceException
	 */
	@Override
	protected boolean loadData() throws Exception, ServiceException {
		try {
			// 見積情報をロードする
			InputEstimateDto dto = (InputEstimateDto) estimateSheetService
					.loadBySlipId(inputEstimateForm.estimateSheetId);
			if (dto == null) {
				return false;
			}

			Beans.copy(dto, inputEstimateForm).execute();

			// 見積明細情報をロードする
			List<InputEstimateLineDto> lineDtoList = estimateLineService
					.loadBySlip(dto);
			dto.setLineDtoList(lineDtoList);
			dto.fillList();
			inputEstimateForm.setLineList(lineDtoList);

			inputEstimateForm.initLoad();

			createList();
			return true;

		} catch (ServiceException e) {
			super.errorLog(e);
			throw e;
		}
	}

	/**
	 * アクションフォームを返します.
	 * @return {@link InputEstimateForm}
	 */
	@Override
	protected AbstractSlipEditForm<InputEstimateLineDto> getActionForm() {
		return this.inputEstimateForm;
	}

	/**
	 * 新規見積入力データのDTOを作成します.
	 * @return {@link InputEstimateDto}
	 */
	@Override
	protected AbstractSlipDto<InputEstimateLineDto> createDTO() {
		return new InputEstimateDto();
	}

	/**
	 * 入力画面jspのURIを返します.
	 * @return 見積入力画面jspのURI
	 */
	@Override
	protected String getInputURIString() {
		return InputEstimateAction.Mapping.INPUT;
	}

	/**
	 * 伝票サービスを返します.
	 * @return {@link EstimateSheetService}
	 */
	@Override
	protected AbstractSlipService<EstimateSheetTrn, InputEstimateDto> getSlipService() {
		return this.estimateSheetService;
	}

	/**
	 * 伝票登録時に使用されるサービスを返します.
	 * @return サービスリスト
	 */
	@Override
	protected AbstractService<?>[] getAdditionalServiceOnSaveSlip() {
		return new AbstractService[] { this.customerService };
	}

	/**
	 * 明細行サービスを返します.
	 * @return {@link EstimateLineService}
	 */
	@Override
	protected AbstractLineService<EstimateLineTrn,InputEstimateLineDto,InputEstimateDto> getLineService() {
		return this.estimateLineService;
	}

	/**
	 * 例外発生時のラベルを返します.
	 * @return 例外発生時のラベル
	 */
	@Override
	public String getSlipKeyLabel() {
		return "labels.report.hist.estimateSheet.estimateSheetId";
	}

	/**
	 * 伝票登録時のvalidateを行います.
	 * @return エラーリスト
	 * @throws ServiceException
	 */
	@Override
	public ActionMessages validateAtCreateSlip() throws ServiceException {
		ActionMessages errors = new ActionMessages();
		String labelCustomerCode = MessageResourcesUtil
				.getMessage("labels.customerCode");
		String labelProductCode = MessageResourcesUtil
				.getMessage("labels.productCode");
		String labelProductName = MessageResourcesUtil
				.getMessage("labels.productName");

		String labelQuantity = MessageResourcesUtil
				.getMessage("labels.quantity");
		String labelUnitCost = MessageResourcesUtil
				.getMessage("labels.unitCost");
		String labelCost = MessageResourcesUtil.getMessage("labels.cost");
		String labelUnitRetailPrice = MessageResourcesUtil
				.getMessage("labels.unitRetailPrice");
		String labelRetailPrice = MessageResourcesUtil
				.getMessage("labels.retailPrice");

		String labelRemarks = MessageResourcesUtil.getMessage("labels.remarks");

		// 見積日＜＝有効期限
		if (StringUtil.hasLength(inputEstimateForm.estimateDate)
				&& StringUtil.hasLength(inputEstimateForm.validDate)) {
			try {
				if (DateFormat.getDateInstance().parse(inputEstimateForm.estimateDate)
						.after(DateFormat.getDateInstance().parse(inputEstimateForm.validDate))) {
					errors.add(ActionMessages.GLOBAL_MESSAGE,
							new ActionMessage("errors.date.estimate"));

				}
			} catch (ParseException e) {
				// 型チェックは済んでいる
			}
		}
		// 存在する顧客か確認
		if (StringUtil.hasLength(inputEstimateForm.customerCode)) {
			if (customerService.isExistCustomerCode(inputEstimateForm.customerCode) == false) {
				// 顧客コードがXXのデータは存在しません
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.dataNotExist", labelCustomerCode, inputEstimateForm.customerCode));
			}
		}

		// 明細行
		boolean inputLine = false;
		for (InputEstimateLineDto line : inputEstimateForm.estimateLineTrnDtoList) {
			// 商品コードに入力がない場合は無視
			if (!StringUtil.hasLength(line.productCode)) {
				continue;
			}
			// 明細行が1件以上、存在する
			inputLine = true;

			// 必須チェック
			// 数量
			if (!StringUtil.hasLength(line.quantity)) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.line.required", line.lineNo, labelQuantity));
			}

			// 売上単価
			if (!StringUtil.hasLength(line.unitRetailPrice)) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.line.required", line.lineNo,
						labelUnitRetailPrice));
			}

			// 売上金額
			if (!StringUtil.hasLength(line.retailPrice)) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.line.required", line.lineNo, labelRetailPrice));
			}

			// 長さチェック
			// 商品コード
			if (StringUtil.hasLength(line.productCode)) {
				if (line.productCode.length() > 20) {
					errors.add(ActionMessages.GLOBAL_MESSAGE,
							new ActionMessage("errors.line.maxlength",
									line.lineNo, labelProductCode, "20"));
				}
			}
			// 商品名
			if (StringUtil.hasLength(line.productAbstract)
					&& line.productAbstract.length() > 60) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.line.maxlength", line.lineNo, labelProductName,
						"60"));
			}
			// 備考
			if (StringUtil.hasLength(line.remarks)
					&& line.remarks.length() > 120) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.line.maxlength", line.lineNo, labelRemarks,
						"120"));
			}

			// 型チェック
			// Float型
			// 数量
			if (StringUtil.hasLength(line.quantity)) {
				try {

					// 数値０チェック
					int iQuantity = ((Double) Double.parseDouble(line.quantity))
							.intValue();
					if (iQuantity == 0) {
						errors.add(ActionMessages.GLOBAL_MESSAGE,
								new ActionMessage("errors.line.num0",
										line.lineNo, labelQuantity));
					}
				} catch (NumberFormatException e) {
					errors.add(ActionMessages.GLOBAL_MESSAGE,
							new ActionMessage("errors.line.float", line.lineNo,
									labelQuantity));
				}
			}

			// 仕入単価
			if (StringUtil.hasLength(line.unitCost)) {
				try {

					// 数値０チェック
					// 特殊コード商品の場合は0チェックを行わない
					if (!DiscountUtil.isExceptianalProduct(line.productCode)) {
						float funitCost = Float.parseFloat(line.unitCost);
						if (Double.compare(funitCost, 0) == 0) {
							errors.add(ActionMessages.GLOBAL_MESSAGE,
									new ActionMessage("errors.line.num0",
											line.lineNo, labelUnitCost));
						}
					}
				} catch (NumberFormatException e) {
					errors.add(ActionMessages.GLOBAL_MESSAGE,
							new ActionMessage("errors.line.float", line.lineNo,
									labelUnitCost));
				}
			}
			// 仕入金額
			if (StringUtil.hasLength(line.cost)) {
				try {
					// 数値０チェック
					// 特殊コード商品の場合は0チェックを行わない
					if (!DiscountUtil.isExceptianalProduct(line.productCode)) {
						float fCost = Float.parseFloat(line.cost);
						if (Double.compare(fCost, 0) == 0) {
							errors.add(ActionMessages.GLOBAL_MESSAGE,
									new ActionMessage("errors.line.num0",
											line.lineNo, labelCost));
						}
					}

					// 計算によって生成される項目の最大最小値チェック
					if (StringUtil.hasLength(line.cost)) {
						Long value = new Long(line.cost);
						if ((value < Constants.LIMIT_VALUE.PRICE_MIN)
								|| (value > Constants.LIMIT_VALUE.PRICE_MAX)) {

							errors.add(
									ActionMessages.GLOBAL_MESSAGE,
									new ActionMessage("errors.line.range",
											line.lineNo, labelCost,
											Constants.LIMIT_VALUE.PRICE_MIN
													.toString(),
											Constants.LIMIT_VALUE.PRICE_MAX
													.toString()));
						}
					}

				} catch (NumberFormatException e) {
					errors.add(ActionMessages.GLOBAL_MESSAGE,
							new ActionMessage("errors.line.float", line.lineNo,
									labelCost));
				}
			}
			// 売上単価
			if (StringUtil.hasLength(line.unitRetailPrice)) {
				try {
					// 数値０チェック
					float funitRetailPrice = Float
							.parseFloat(line.unitRetailPrice);
					if (Double.compare(funitRetailPrice, 0) == 0) {
						errors.add(ActionMessages.GLOBAL_MESSAGE,
								new ActionMessage("errors.line.num0",
										line.lineNo, labelUnitRetailPrice));
					}
					//見積もりでは値引きがあるので、マイナスも入力可とする
					/*if (StringUtil.hasLength(line.unitRetailPrice)
							&& Float.parseFloat(line.unitRetailPrice) < 0) {
						errors.add(ActionMessages.GLOBAL_MESSAGE,
								new ActionMessage("errors.line.integer.plus",
										line.lineNo, labelUnitRetailPrice, "0"));
					}*/
				} catch (NumberFormatException e) {
					errors.add(ActionMessages.GLOBAL_MESSAGE,
							new ActionMessage("errors.line.float", line.lineNo,
									labelUnitRetailPrice));
				}
			}
			// 売価金額
			if (StringUtil.hasLength(line.retailPrice)) {
				try {
					// 数値０チェック
					float fretailPrice = Float.parseFloat(line.retailPrice);
					if (Double.compare(fretailPrice, 0) == 0) {
						errors.add(ActionMessages.GLOBAL_MESSAGE,
								new ActionMessage("errors.line.num0",
										line.lineNo, labelRetailPrice));
					}

					// 計算によって生成される項目の最大最小値チェック
					if (StringUtil.hasLength(line.retailPrice)) {
						Long value = new Long(line.retailPrice);
						if ((value < Constants.LIMIT_VALUE.PRICE_MIN)
								|| (value > Constants.LIMIT_VALUE.PRICE_MAX)) {

							errors.add(
									ActionMessages.GLOBAL_MESSAGE,
									new ActionMessage("errors.line.range",
											line.lineNo, labelRetailPrice,
											Constants.LIMIT_VALUE.PRICE_MIN
													.toString(),
											Constants.LIMIT_VALUE.PRICE_MAX
													.toString()));
						}
					}
				} catch (NumberFormatException e) {
					errors.add(ActionMessages.GLOBAL_MESSAGE,
							new ActionMessage("errors.line.float", line.lineNo,
									labelRetailPrice));
				}
			}

			// 値チェック　見積は商品が商品マスタに登録されていない可能性があるので存在チェックはしない

		}
		// 明細行が1行以上、存在するかどうか
		if (!inputLine) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.noline"));
		}

		return errors;

	}

}
