/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.stock;

import java.util.List;

import javax.annotation.Resource;

import jp.co.arkinfosys.action.AbstractSlipEditAction;
import jp.co.arkinfosys.common.CategoryTrns;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.common.StringUtil;
import jp.co.arkinfosys.common.ValidateUtil;
import jp.co.arkinfosys.common.Constants.CODE_SIZE;
import jp.co.arkinfosys.dto.AbstractSlipDto;
import jp.co.arkinfosys.dto.stock.EadLineTrnDto;
import jp.co.arkinfosys.dto.stock.EadSlipTrnDto;
import jp.co.arkinfosys.entity.EadLineTrn;
import jp.co.arkinfosys.entity.EadSlipTrn;
import jp.co.arkinfosys.entity.Product;
import jp.co.arkinfosys.entity.Rack;
import jp.co.arkinfosys.form.AbstractSlipEditForm;
import jp.co.arkinfosys.form.stock.InputStockForm;
import jp.co.arkinfosys.service.AbstractLineService;
import jp.co.arkinfosys.service.AbstractService;
import jp.co.arkinfosys.service.AbstractSlipService;
import jp.co.arkinfosys.service.exception.ServiceException;
import jp.co.arkinfosys.service.stock.InputStockLineService;
import jp.co.arkinfosys.service.stock.InputStockService;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.LabelValueBean;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.util.MessageResourcesUtil;

/**
 * 入出庫入力画面のアクションクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class InputStockAction extends
		AbstractSlipEditAction<EadSlipTrnDto, EadLineTrnDto> {

	/**
	 * 画面遷移用のマッピングクラスです.
	 */
	public static class Mapping {
		public static final String INPUT = "inputStock.jsp";
		public static final String SEARCH = "/stock/searchStock";
	}

	@ActionForm
	@Resource
	private InputStockForm inputStockForm;

	@Resource
	private InputStockService inputStockService;

	@Resource
	private InputStockLineService inputStockLineService;

	/**
	 * 入出庫伝票区分の選択値リスト
	 */
	public List<LabelValueBean> slipCategoryList;

	/**
	 * 入出庫区分の選択値リスト
	 */
	public List<LabelValueBean> categoryList;

	/** セット分類：単品（JSPで使用） */
	public String productSetSingle = CategoryTrns.PRODUCT_SET_TYPE_SINGLE;

	/**
	 * 初期表示処理を行います.
	 *
	 * @throws Exception
	 */
	@Execute(validator = false)
	public String init() throws Exception {
		try {
			initList();
			inputStockForm.menuUpdate = userDto
					.isMenuUpdate(Constants.MENU_ID.INPUT_STOCK);
		} catch (ServiceException e) {
			super.errorLog(e);

			// 続行可能？
			if (e.isStopOnError()) {
				// システム例外として処理する
				throw e;
			}
		}

		return Mapping.INPUT;
	}

	/**
	 * プルダウンの初期化を行います.
	 * @throws Exception
	 */
	protected void initList() throws ServiceException {
		slipCategoryList = inputStockService.getSlipCategoryList();
		categoryList = inputStockService.getCategoryList();
	}

	/**
	* エラーを追加します.
	* @param errors 被追加対象のエラーメッセージ
	* @param err 追加対象エラーメッセージ
	*/
	private void addError(ActionMessages errors, ActionMessage err) {
		if (errors == null || err == null) {
			return;
		}
		errors.add(ActionMessages.GLOBAL_MESSAGE, err);
	}

	/**
	 * 新規入出庫伝票DTOを作成します.
	 * @return {@link EadSlipTrnDto}
	 */
	@Override
	protected AbstractSlipDto<EadLineTrnDto> createDTO() {
		return new EadSlipTrnDto();
	}

	/**
	 * 画面リストを初期化します.
	 * @throws Exception
	 */
	@Override
	protected void createList() throws Exception {
		slipCategoryList = inputStockService.getSlipCategoryList();
		categoryList = inputStockService.getCategoryList();
	}

	/**
	 * アクションフォームを返します.
	 * @return {@link InputStockForm}
	 */
	@Override
	protected AbstractSlipEditForm<EadLineTrnDto> getActionForm() {
		return inputStockForm;
	}

	/**
	 * 登録時に使用するサービスを返します.
	 * @return サービスリスト
	 */
	@Override
	protected AbstractService<?>[] getAdditionalServiceOnSaveSlip() {
		return new AbstractService[] { inputStockService };
	}

	/**
	 * 入力画面のURIを返します.
	 * @return 入出庫入力画面のURI
	 */
	@Override
	protected String getInputURIString() {
		return Mapping.INPUT;
	}

	/**
	 * 入出庫伝票のラベルのキーを返します.
	 * @return 入出庫伝票のラベルのキー
	 */
	@Override
	public String getSlipKeyLabel() {
		return "erroes.db.eadSlip";
	}

	/**
	 * 伝票サービスを返します.
	 * @return {@link InputStockService}
	 */
	@Override
	protected AbstractSlipService<EadSlipTrn, EadSlipTrnDto> getSlipService() {
		return inputStockService;
	}

	/**
	 * 明細行サービスを返します.
	 * @return {@link InputStockLineService}
	 */
	@Override
	protected AbstractLineService<EadLineTrn, EadLineTrnDto, EadSlipTrnDto> getLineService() {
		return inputStockLineService;
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
			// 権限の取得
			inputStockForm.menuUpdate = userDto
					.isMenuUpdate(Constants.MENU_ID.INPUT_STOCK);

			// プルダウンの初期化
			initList();

			// DTOの取得
			EadSlipTrnDto eadSlipTrnDto = inputStockService
					.loadBySlipId(inputStockForm.eadSlipId);
			// 入出庫入力画面にて作成された伝票のみ参照
			if ((eadSlipTrnDto == null) || !"2".equals(eadSlipTrnDto.srcFunc)) {
				return false;
			}

			// DTOの値をFormへコピー
			Beans.copy(eadSlipTrnDto, inputStockForm).execute();

			// Formの明細行を初期化する
			inputStockForm.eadLineTrnDtoList = eadSlipTrnDto.getLineDtoList();

			inputStockForm.initLoad();

			return true;

		} catch (ServiceException e) {
			super.errorLog(e);
			throw e;
		}
	}

	/**
	 * 登録時の入出庫伝票と入出庫伝票明細行のチェックを行います.
	 * @return アクションメッセージ
	 * @throws ServiceException
	 */
	@Override
	public ActionMessages validateAtCreateSlip() throws ServiceException {
		ActionMessages errors = new ActionMessages();
		ActionMessage err;

		String labelProductCode = MessageResourcesUtil
				.getMessage("labels.productCode");
		String labelQuantity = MessageResourcesUtil
				.getMessage("labels.quantity");
		String labelRemarks = MessageResourcesUtil.getMessage("labels.remarks");
		String labelRackCode = MessageResourcesUtil
				.getMessage("labels.rackCode");

		boolean inputLine = false;

		if (inputStockForm.eadLineTrnDtoList != null) {
			for (EadLineTrnDto eadLineTrnDto : inputStockForm.eadLineTrnDtoList) {
				// 商品コードに入力がない場合は無視
				if (!StringUtil.hasLength(eadLineTrnDto.productCode)) {
					continue;
				}

				// 明細行が1件以上、存在する
				inputLine = true;

				// 必須チェック

				// 数量
				err = ValidateUtil.required(eadLineTrnDto.quantity,
						"errors.line.required", new Object[] {
								eadLineTrnDto.lineNo, labelQuantity });
				addError(errors, err);
				// 棚番
				err = ValidateUtil.required(eadLineTrnDto.rackCode,
						"errors.line.required", new Object[] {
								eadLineTrnDto.lineNo, labelRackCode });
				addError(errors, err);

				// 長さチェック

				// 商品コード
				if (StringUtil.hasLength(eadLineTrnDto.productCode)) {
					err = ValidateUtil.maxlength(eadLineTrnDto.productCode,
							CODE_SIZE.PRODUCT, "errors.line.maxlength",
							new Object[] { eadLineTrnDto.lineNo,
									labelProductCode,
									Integer.toString(CODE_SIZE.PRODUCT) });
					addError(errors, err);
				}
				// 備考
				if (StringUtil.hasLength(eadLineTrnDto.remarks)) {
					err = ValidateUtil
							.maxlength(eadLineTrnDto.remarks, 120,
									"errors.line.maxlength", new Object[] {
											eadLineTrnDto.lineNo, labelRemarks,
											"120" });
					addError(errors, err);
				}
				// 棚番コード
				if (StringUtil.hasLength(eadLineTrnDto.rackCode)) {
					err = ValidateUtil.maxlength(eadLineTrnDto.rackCode,
							CODE_SIZE.RACK, "errors.line.maxlength",
							new Object[] { eadLineTrnDto.lineNo, labelRackCode,
									Integer.toString(CODE_SIZE.RACK) });
					addError(errors, err);
				}

				// 型チェック

				// 数量
				boolean quantityType = false;
				if (StringUtil.hasLength(eadLineTrnDto.quantity)) {
					err = ValidateUtil.intRange(eadLineTrnDto.quantity, 1,
							null, labelQuantity);
					if (err == null) {
						quantityType = true;
					}
					if (!quantityType) {
						err = new ActionMessage("errors.line.integer.plus",
								eadLineTrnDto.lineNo, labelQuantity);
						addError(errors, err);
					}
				}

				// 値チェック（マスタ存在確認）

				// 商品コード
				if (StringUtil.hasLength(eadLineTrnDto.productCode)) {
					Product product = inputStockService
							.findProductByCode(eadLineTrnDto.productCode);
					if (product == null) {
						err = new ActionMessage("errors.line.dataNotExist",
								eadLineTrnDto.lineNo, labelProductCode,
								eadLineTrnDto.productCode);
						addError(errors, err);
					}else if (CategoryTrns.PRODUCT_STOCK_CTL_NO
							.equals(product.stockCtlCategory)
							|| CategoryTrns.PRODUCT_SET_TYPE_SET
									.equals(product.setTypeCategory)) {
						// 在庫管理しない商品は入出庫できない
						err = new ActionMessage(
								"errors.line.product.stockctl.off",
								eadLineTrnDto.lineNo);
						addError(errors, err);
					}
				}
				// 棚番
				if (StringUtil.hasLength(eadLineTrnDto.rackCode)) {
					Rack rack = inputStockService
							.findRackByCode(eadLineTrnDto.rackCode);
					if (rack == null) {
						err = new ActionMessage("errors.line.dataNotExist",
								eadLineTrnDto.lineNo, labelRackCode,
								eadLineTrnDto.rackCode);
						addError(errors, err);
					}
				}

				// 値チェック（相関）
				// 同商品、同棚番はエラー
				if (StringUtil.hasLength(eadLineTrnDto.productCode)
						&& StringUtil.hasLength(eadLineTrnDto.rackCode)) {
					for (EadLineTrnDto eadLineTrnDto2 : inputStockForm.eadLineTrnDtoList) {
						if (eadLineTrnDto.equals(eadLineTrnDto2)) {
							// 同じ行はチェックしない
							continue;
						}
						if (eadLineTrnDto.productCode
								.equals(eadLineTrnDto2.productCode)
								&& eadLineTrnDto.rackCode
										.equals(eadLineTrnDto2.rackCode)) {
							err = new ActionMessage(
									"errors.line.sameProductAndRack",
									eadLineTrnDto.lineNo);
							addError(errors, err);
							break;
						}
					}
				}
			}
		}

		// 明細行が1行以上、存在するかどうか
		if (!inputLine) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.noline"));
		}

		// TODO:暫定的な処理。本来であればフレームワークのaddMessage(String... arg)に統一すべき
		super.messages.add(errors);

		return super.messages;
	}

}
