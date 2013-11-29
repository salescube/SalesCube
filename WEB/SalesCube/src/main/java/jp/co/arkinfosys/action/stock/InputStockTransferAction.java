/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.stock;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import jp.co.arkinfosys.action.AbstractSlipEditAction;
import jp.co.arkinfosys.common.CategoryTrns;
import jp.co.arkinfosys.common.Constants.CODE_SIZE;
import jp.co.arkinfosys.common.StringUtil;
import jp.co.arkinfosys.common.ValidateUtil;
import jp.co.arkinfosys.dto.AbstractSlipDto;
import jp.co.arkinfosys.dto.stock.EadLineTrnDto;
import jp.co.arkinfosys.dto.stock.EadSlipTrnDto;
import jp.co.arkinfosys.entity.EadLineTrn;
import jp.co.arkinfosys.entity.EadSlipTrn;
import jp.co.arkinfosys.entity.Product;
import jp.co.arkinfosys.entity.Rack;
import jp.co.arkinfosys.form.AbstractSlipEditForm;
import jp.co.arkinfosys.form.stock.InputStockTransferForm;
import jp.co.arkinfosys.service.AbstractLineService;
import jp.co.arkinfosys.service.AbstractService;
import jp.co.arkinfosys.service.AbstractSlipService;
import jp.co.arkinfosys.service.exception.ServiceException;
import jp.co.arkinfosys.service.stock.InputStockTransferLineService;
import jp.co.arkinfosys.service.stock.InputStockTransferService;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.util.MessageResourcesUtil;

/**
 * 在庫移動入力画面の表示アクションクラスです.
 * @author Ark Information Systems
 *
 */
public class InputStockTransferAction extends
		AbstractSlipEditAction<EadSlipTrnDto, EadLineTrnDto> {

	/**
	 * 画面遷移用のマッピングクラスです.
	 */
	public static class Mapping {
		public static final String INPUT = "inputStockTransfer.jsp";
		public static final String SEARCH = "/stock/searchStock";
	}

	@ActionForm
	@Resource
	private InputStockTransferForm inputStockTransferForm;

	@Resource
	private InputStockTransferService inputStockTransferService;

	@Resource
	private InputStockTransferLineService inputStockTransferLineService;

	/** セット分類：単品（JSPで使用） */
	public String productSetSingle = CategoryTrns.PRODUCT_SET_TYPE_SINGLE;

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
	 * 伝票サービスを返します.
	 * @return {@link InputStockTransferService}
	 */
	@Override
	protected AbstractSlipService<EadSlipTrn, EadSlipTrnDto> getSlipService() {
		return this.inputStockTransferService;
	}

	/**
	 * 明細行サービスを返します.
	 * @return {@link InputStockTransferLineService}
	 */
	@Override
	protected AbstractLineService<EadLineTrn, EadLineTrnDto, EadSlipTrnDto> getLineService() {
		return this.inputStockTransferLineService;
	}

	/**
	 * 入力画面のURIを返します.
	 * @return 在庫移動入力画面のURI
	 */
	@Override
	protected String getInputURIString() {
		return InputStockTransferAction.Mapping.INPUT;
	}

	/**
	 * 新規在庫移動伝票DTOを作成します.
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
		// 処理なし
		return;
	}

	/**
	 * アクションフォームを返します.
	 * @return {@link InputStockTransferForm}
	 */
	@Override
	protected AbstractSlipEditForm<EadLineTrnDto> getActionForm() {
		return inputStockTransferForm;
	}

	/**
	 * 登録時に使用するサービスを返します.
	 * @return サービスリスト
	 */
	@Override
	protected AbstractService<?>[] getAdditionalServiceOnSaveSlip() {
		// 長さ0の配列を返す
		return new AbstractService[0];
	}

	/**
	 * 在庫移動伝票のラベルのキーを返します.
	 * @return 在庫移動伝票のラベルのキー
	 */
	@Override
	public String getSlipKeyLabel() {
		return "labels.eadSlipId.transfer";
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
			// 在庫移動入力情報をロードする
			EadSlipTrnDto eadSlipTrnDto = inputStockTransferService
					.loadBySlipId(inputStockTransferForm.eadSlipId);
			if (eadSlipTrnDto == null) {
				return false;
			}

			// DTOの値をFormへコピー
			Beans.copy(eadSlipTrnDto, inputStockTransferForm).execute();

			// Formの明細行を初期化する
			inputStockTransferForm.eadLineTrnDtoList = eadSlipTrnDto
					.getLineDtoList();

			inputStockTransferForm.initLoad();

			return true;

		} catch (ServiceException e) {
			super.errorLog(e);
			throw e;
		}
	}

	/**
	 * 登録時の在庫移動伝票と在庫移動伝票明細行のチェックを行います.
	 * @return アクションメッセージ
	 * @throws ServiceException
	 */
	// 登録時のvalidate
	// validateUtilの機能をAbstractSlipEditActionに上げてエラーメッセージ登録方法を
	// 全画面で統一されることが望まれる(InputROrderAction等と)
	@Override
	public ActionMessages validateAtCreateSlip() throws ServiceException {
		ActionMessages errors = new ActionMessages();
		ActionMessage err;
		List<ActionMessage> quantityErrors = new ArrayList<ActionMessage>();

		String labelProductCode = MessageResourcesUtil
				.getMessage("labels.productCode");
		String labelQuantity = MessageResourcesUtil
				.getMessage("labels.quantity");
		String labelRemarks = MessageResourcesUtil.getMessage("labels.remarks");
		String labelRackCodeSrc = MessageResourcesUtil
				.getMessage("labels.rackCodeSrc");
		String labelRackCodeDest = MessageResourcesUtil
				.getMessage("labels.rackCodeDest");

		boolean inputLine = false;

		if (inputStockTransferForm.eadLineTrnDtoList != null) {
			for (int i = 0; i < inputStockTransferForm.eadLineTrnDtoList.size(); i++) {
				EadLineTrnDto eadLineTrnDto = inputStockTransferForm.eadLineTrnDtoList
						.get(i);
				// 商品コードに入力がない場合は無視
				if (!StringUtil.hasLength(eadLineTrnDto.productCode)) {
					continue;
				}

				// 明細行が1件以上、存在する
				inputLine = true;

				// 必須チェック

				// 数量
				if (eadLineTrnDto.quantity == "") {
					eadLineTrnDto.quantity = "0";
				}
				
				err = ValidateUtil.required(eadLineTrnDto.quantity,
						"errors.line.required",
						//		new Object[] { eadLineTrnDto.eadLineNo, labelQuantity });
						new Object[] { eadLineTrnDto.lineNo, labelQuantity });
				//addError(errors, err);
				// 移動元棚番
				err = ValidateUtil
						.required(eadLineTrnDto.rackCode,
								"errors.line.required",
								//		new Object[] { eadLineTrnDto.eadLineNo, labelRackCodeSrc });
								new Object[] { eadLineTrnDto.lineNo,
										labelRackCodeSrc });
				addError(errors, err);
				// 移動先棚番
				err = ValidateUtil
						.required(eadLineTrnDto.rackCodeDest,
								"errors.line.required",
								//		new Object[] { eadLineTrnDto.eadLineNo, labelRackCodeDest });
								new Object[] { eadLineTrnDto.lineNo,
										labelRackCodeDest });
				addError(errors, err);

				// 長さチェック

				// 商品コード
				if (StringUtil.hasLength(eadLineTrnDto.productCode)) {
					err = ValidateUtil.maxlength(eadLineTrnDto.productCode,
							CODE_SIZE.PRODUCT, "errors.line.maxlength",
							//			new Object[] { eadLineTrnDto.eadLineNo, labelProductCode, Integer.toString( CODE_SIZE.PRODUCT ) });
							new Object[] { eadLineTrnDto.lineNo,
									labelProductCode,
									Integer.toString(CODE_SIZE.PRODUCT) });
					addError(errors, err);
				}
				// 備考
				if (StringUtil.hasLength(eadLineTrnDto.remarks)) {
					err = ValidateUtil.maxlength(eadLineTrnDto.remarks, 120,
							"errors.line.maxlength",
							//		new Object[] { eadLineTrnDto.eadLineNo, labelRemarks, "120" });
							new Object[] { eadLineTrnDto.lineNo, labelRemarks,
									"120" });
					addError(errors, err);
				}
				// 移動元棚番コード
				if (StringUtil.hasLength(eadLineTrnDto.rackCode)) {
					err = ValidateUtil.maxlength(eadLineTrnDto.rackCode,
							CODE_SIZE.RACK, "errors.line.maxlength",
							//		new Object[] { eadLineTrnDto.eadLineNo, labelRackCodeSrc, Integer.toString( CODE_SIZE.RACK ) });
							new Object[] { eadLineTrnDto.lineNo,
									labelRackCodeSrc,
									Integer.toString(CODE_SIZE.RACK) });
					addError(errors, err);
				}
				// 移動先棚番コード
				if (StringUtil.hasLength(eadLineTrnDto.rackCodeDest)) {
					err = ValidateUtil.maxlength(eadLineTrnDto.rackCodeDest,
							CODE_SIZE.RACK, "errors.line.maxlength",
							//		new Object[] { eadLineTrnDto.eadLineNo, labelRackCodeDest, Integer.toString( CODE_SIZE.RACK ) });
							new Object[] { eadLineTrnDto.lineNo,
									labelRackCodeDest,
									Integer.toString(CODE_SIZE.RACK) });
					addError(errors, err);
				}

				// 値チェック（相関1）

				// 移動元棚番と移動先棚番が同じ場合、エラー
				if (StringUtil.hasLength(eadLineTrnDto.rackCode)
						&& StringUtil.hasLength(eadLineTrnDto.rackCodeDest)) {
					if (eadLineTrnDto.rackCode
							.equals(eadLineTrnDto.rackCodeDest)) {
						//err = new ActionMessage("errors.line.sameRack", eadLineTrnDto.eadLineNo);
						err = new ActionMessage("errors.line.sameRack",
								eadLineTrnDto.lineNo);
						addError(errors, err);
					}
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
						//err = new ActionMessage("errors.line.integer.plus", eadLineTrnDto.eadLineNo, labelQuantity);
						err = new ActionMessage("errors.line.integer.plus",
								eadLineTrnDto.lineNo, labelQuantity);
						addError(errors, err);
					}
				}

				// 値チェック（マスタ存在確認）

				// 商品コード
				boolean productExists = false;
				if (StringUtil.hasLength(eadLineTrnDto.productCode)) {
					Product product = inputStockTransferService
							.findProductByCode(eadLineTrnDto.productCode);
					if (product == null) {
						err = new ActionMessage("errors.line.dataNotExist",
								//eadLineTrnDto.eadLineNo, labelProductCode, eadLineTrnDto.productCode);
								eadLineTrnDto.lineNo, labelProductCode,
								eadLineTrnDto.productCode);
						addError(errors, err);
					} else {
						productExists = true;
					}
				}
				// 移動元棚番
				boolean rackExists = false;
				if (StringUtil.hasLength(eadLineTrnDto.rackCode)) {
					Rack rack = inputStockTransferService
							.findRackByCode(eadLineTrnDto.rackCode);
					if (rack == null) {
						err = new ActionMessage("errors.line.dataNotExist",
								//eadLineTrnDto.eadLineNo, labelRackCodeSrc, eadLineTrnDto.rackCode);
								eadLineTrnDto.lineNo, labelRackCodeSrc,
								eadLineTrnDto.rackCode);
						addError(errors, err);
					} else {
						rackExists = true;
					}
				}
				// 移動先棚番
				if (StringUtil.hasLength(eadLineTrnDto.rackCodeDest)) {
					Rack rack = inputStockTransferService
							.findRackByCode(eadLineTrnDto.rackCodeDest);
					if (rack == null) {
						err = new ActionMessage("errors.line.dataNotExist",
								//eadLineTrnDto.eadLineNo, labelRackCodeDest, eadLineTrnDto.rackCodeDest);
								eadLineTrnDto.lineNo, labelRackCodeDest,
								eadLineTrnDto.rackCodeDest);
						addError(errors, err);
					}
				}

				// 値チェック（相関2）

				// 数量が移動可能数を超えている場合、エラー
				if (quantityType && productExists && rackExists) {
					int quantity = Integer.parseInt(eadLineTrnDto.quantity);
					int unclosedQuantity = inputStockTransferService
							.countUnclosedQuantityByProductCode(
									eadLineTrnDto.productCode,
									eadLineTrnDto.rackCode);
					int closedQuantity = inputStockTransferService
							.countClosedQuantityByProductCode(
									eadLineTrnDto.productCode,
									eadLineTrnDto.rackCode);
					if (quantity > unclosedQuantity + closedQuantity) {
						//err = new ActionMessage("errors.line.movableQuantity.over", eadLineTrnDto.eadLineNo);
						err = new ActionMessage(
								"errors.line.movableQuantity.over",
								eadLineTrnDto.lineNo);
						addError(errors, err);
					}
					// 同商品、同移動元棚番の数量を合算して、引当可能数を超えていないかをチェック
					boolean targetExists = false;
					int sumQuantity = quantity;
					for (int j = 0; j < inputStockTransferForm.eadLineTrnDtoList
							.size(); j++) {
						if (i == j) {
							// 同じ行は対象外
							continue;
						}
						EadLineTrnDto eadLineTrnDto2 = inputStockTransferForm.eadLineTrnDtoList
								.get(j);
						// 同商品、同移動元棚番の場合
						if (eadLineTrnDto.productCode
								.equals(eadLineTrnDto2.productCode)
								&& eadLineTrnDto.rackCode
										.equals(eadLineTrnDto2.rackCode)) {
							err = ValidateUtil.intRange(
									eadLineTrnDto2.quantity, 1, null,
									labelQuantity);
							if (err == null) {
								if (i > j) {
									// 処理行以前の行の場合、処理済みな為、チェック終了
									break;
								}
								targetExists = true;
								sumQuantity += Integer
										.parseInt(eadLineTrnDto2.quantity);
							}
						}
					}
					if (targetExists
							&& (sumQuantity > unclosedQuantity + closedQuantity)) {
						err = new ActionMessage(
								"errors.movableQuantity.sum.over",
								eadLineTrnDto.productCode,
								eadLineTrnDto.rackCode);
						quantityErrors.add(err);
					}
				}
				// 同商品、同移動元棚番、同移動先棚番はエラー
				if (StringUtil.hasLength(eadLineTrnDto.productCode)
						&& StringUtil.hasLength(eadLineTrnDto.rackCode)
						&& StringUtil.hasLength(eadLineTrnDto.rackCodeDest)) {
					for (EadLineTrnDto eadLineTrnDto2 : inputStockTransferForm.eadLineTrnDtoList) {
						if (eadLineTrnDto.equals(eadLineTrnDto2)) {
							// 同じ行はチェックしない
							continue;
						}
						if (eadLineTrnDto.productCode
								.equals(eadLineTrnDto2.productCode)
								&& eadLineTrnDto.rackCode
										.equals(eadLineTrnDto2.rackCode)
								&& eadLineTrnDto.rackCodeDest
										.equals(eadLineTrnDto2.rackCodeDest)) {
							//err = new ActionMessage("errors.line.sameProductAndMoveRack", eadLineTrnDto.eadLineNo);
							err = new ActionMessage(
									"errors.line.sameProductAndMoveRack",
									eadLineTrnDto.lineNo);
							addError(errors, err);
							break;
						}
					}
				}
			}

			// 同商品、同移動元棚番の数量を合算して、引当可能数を超えていないかをチェック
			for (ActionMessage message : quantityErrors) {
				addError(errors, message);
			}
		}

		// 明細行が1行以上、存在するかどうか
		if (!inputLine) {
			err = new ActionMessage("errors.noline");
			addError(errors, err);
		}

		// TODO:暫定的な処理。本来であればフレームワークのaddMessage(String... arg)に統一すべき
		super.messages.add(errors);

		return super.messages;
	}

	/**
	 * 削除の後処理を行います.<br>
	 * 入庫側の伝票、明細行を削除します.
	 *
	 * @throws Exception
	 */
	@Override
	protected void afterDelete(AbstractSlipDto<EadLineTrnDto> dto) throws Exception {
		// 入庫側伝票のIDを取得
		EadSlipTrnDto eadSlipDto = (EadSlipTrnDto) dto;
		String moveDepositSlipId = eadSlipDto.moveDepositSlipId;

		this.getSlipService().updateAudit(moveDepositSlipId);

		// 入庫側伝票の削除
		this.getSlipService().deleteById(moveDepositSlipId,
				this.inputStockTransferForm.updDatetm);

		this.getSlipService().updateAudit(moveDepositSlipId);

		// 入庫伝票明細行の削除
		this.getLineService().deleteRecords(moveDepositSlipId);
	}

}
