/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.rorder;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import jp.co.arkinfosys.action.AbstractXSVUploadAction;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.common.ValidateUtil;
import jp.co.arkinfosys.dto.rorder.OnlineOrderWorkDto;
import jp.co.arkinfosys.entity.OnlineOrderWork;
import jp.co.arkinfosys.form.rorder.ImportOnlineOrderForm;
import jp.co.arkinfosys.service.OnlineOrderService;
import jp.co.arkinfosys.service.exception.ServiceException;
import jp.co.arkinfosys.service.rorder.ImportOnlineOrderService;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.util.ActionMessagesUtil;
import org.seasar.struts.util.MessageResourcesUtil;

/**
 * オンライン受注データ取込画面のアクションクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class ImportOnlineOrderAction extends AbstractXSVUploadAction {

	/**
	 * 画面遷移用のマッピングクラスです.
	 */
	public static class Mapping {
		public static final String INPUT = "importOnlineOrder.jsp";
	}

	@ActionForm
	@Resource
	private ImportOnlineOrderForm importOnlineOrderForm;

	@Resource
	private ImportOnlineOrderService importOnlineOrderService;

	@Resource
	private OnlineOrderService onlineOrderService;

	private List<OnlineOrderWorkDto> dtoList =  new ArrayList<OnlineOrderWorkDto>();

	/**
	 * 初期表示処理です.
	 * @return 遷移先URI
	 * @throws Exception
	 */
	@Execute(validator = false)
	public String index() throws Exception {
		importOnlineOrderForm.reset();
		importOnlineOrderForm.isUpdate = userDto.isMenuUpdate(Constants.MENU_ID.INPUT_RORDER);
		importOnlineOrderForm.isInputValid = userDto.isMenuValid(Constants.MENU_ID.INPUT_RORDER);
		importOnlineOrderForm.showExist = true;
		return Mapping.INPUT;
	}

	/**
	 * 初期化処理です.
	 * @return 遷移先URI
	 * @throws Exception
	 */
	@Execute(validator = false)
	public String init() throws Exception {
		try {
			importOnlineOrderService.deleteWorksAll();
			importOnlineOrderForm.isUpdate = userDto.isMenuUpdate(Constants.MENU_ID.INPUT_RORDER);
			importOnlineOrderForm.isInputValid = userDto.isMenuValid(Constants.MENU_ID.INPUT_RORDER);
			importOnlineOrderForm.showExist = true;
		} catch (ServiceException e) {
			super.errorLog(e);

			// 続行可能？
			if(e.isStopOnError()) {
				throw e;
			}
		}

		return Mapping.INPUT;
	}

	/**
	 * 再表示処理です.
	 * @return 遷移先URI
	 * @throws Exception
	 */
	@Execute(validator = false, urlPattern = "redraw/{showExist}")
	public String redraw() throws Exception {
		importOnlineOrderForm.isUpdate = userDto.isMenuUpdate(Constants.MENU_ID.INPUT_RORDER);
		importOnlineOrderForm.isInputValid = userDto.isMenuValid(Constants.MENU_ID.INPUT_RORDER);
		return Mapping.INPUT;
	}

	/**
	 * ファイル取込み処理です.
	 * <p>
	 * オンライン受注データファイルを読み込んで、読み込んだデータをデータベースに登録します.
	 * </p>
	 * @return 遷移先URI
	 * @throws Exception
	 */
	@Execute(validator = false)
	public String importFile() throws Exception {
		try {
			importOnlineOrderForm.isUpdate = userDto.isMenuUpdate(Constants.MENU_ID.INPUT_RORDER);
			importOnlineOrderForm.isInputValid = userDto.isMenuValid(Constants.MENU_ID.INPUT_RORDER);

			ActionMessages results = this.importOnlineOrderForm.validate();
			if (results.size() > 0) {
				ActionMessagesUtil.addErrors(super.httpSession, results);
				return Mapping.INPUT;
			}

			// オンライン受注データの読み込み
			readXSV(importOnlineOrderForm.uploadFile);
			// エラーがあるか？
			if(messages.size() != 0) {
				// エラーの設定
				ActionMessagesUtil.addErrors(super.httpSession, messages);
				return Mapping.INPUT;
			}

			// 取り込んだ内容がない場合
			if(dtoList.size()==0){
				ActionMessages l_result = new ActionMessages();
				l_result.add(ActionMessages.GLOBAL_MESSAGE
						,new ActionMessage("errors.upload.content.none"));
				ActionMessagesUtil.addErrors(super.httpSession, l_result);
				return Mapping.INPUT;
			}

			// 読み込んだデータをInsert
			int lineno = 1;
			for(OnlineOrderWorkDto dto : dtoList) {
				dto.lineNo = lineno;
				try {
					List<OnlineOrderWork> larw = onlineOrderService.findOnlineOrderWorkByRoId(dto.onlineOrderId);
					if( larw.size() == 0 ){
						importOnlineOrderService.insertWork(dto);
					}
					lineno++;
				} catch (ServiceException e) {
					if( e.getCause().getLocalizedMessage().indexOf("ESSR0744") == -1 ){
						e.printStackTrace();
						throw e;
					}
				}
			}
			// 完了メッセージを表示
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("infos.import"));
			ActionMessagesUtil.addMessages(super.httpSession, messages);
		} catch (UnsupportedEncodingException e) {
			addError(new ActionMessage("errors.file.encoding"));
			ActionMessagesUtil.addErrors(super.httpSession, messages);
		} catch (ServiceException e) {
			super.errorLog(e);

			// 続行可能？
			if(e.isStopOnError()) {
				// システム例外として処理する
				throw e;
			}
		}

		return Mapping.INPUT;
	}

	/**
	 * 取込済みデータを削除します.
	 * @throws Exception
	 */
	@Execute(validator = false)
	public String delete() throws Exception {
		try{
			// 以前のデータを削除する
			importOnlineOrderService.deleteWorksByRoId(this.importOnlineOrderForm.roId);
			importOnlineOrderForm.isUpdate = userDto.isMenuUpdate(Constants.MENU_ID.INPUT_RORDER);
			importOnlineOrderForm.isInputValid = userDto.isMenuValid(Constants.MENU_ID.INPUT_RORDER);
		} catch (ServiceException e) {
			super.errorLog(e);

			// 続行可能？
			if(e.isStopOnError()) {
				// システム例外として処理する
				throw e;
			}
		}

		return Mapping.INPUT;
	}

	/**
	 * 区切り文字を返します.
	 * @return 区切り文字
	 */
	@Override
	protected String getSeparator() {
		// オンライン受注データはタブ区切り
		return SEPARATOR.TAB;
	}


	/**
	 * 読み込み行の処理です.<br>
	 * 取り込んだ値をチェックして、エラーがなければInsert用のリストに追加します.
	 * @param index 行番号
	 * @param line 未使用
	 * @param values オンライン受注データ配列（１行分）
	 * @throws Exception
	 */
	@Override
	protected void processLine(int index, String line, String[] values)
			throws Exception {

		ActionMessage error;

		// エラーが最大件数に達している場合は処理しない
		if(isErrorsMax()) {
			return;
		}

		// カラム数
		if (values == null
				|| values.length != Constants.ONLINE_ORDER_FILE.COLUMN_COUNT) {
			addError("errors.line.onlineorder.format",
					new Object[] { index, MessageResourcesUtil.getMessage("errors.onlineorder.reason.column") });
			return;
		}

		// オンライン受注Dtoの生成
		OnlineOrderWorkDto dto = importOnlineOrderService.createOnlineOrderWorkDto(values);

		// 型チェック

		// purchase-date(日付)
		error = ValidateUtil.dateType(dto.supplierDate, Constants.FORMAT.ISO8601_DATE, false, "errors.line.onlineorder.format",
				new Object[] { index, MessageResourcesUtil.getMessage("errors.onlineorder.reason.purchase-date") });
		if(error != null) {
			addError(error);
		}
		// payments-date(日付)
		error = ValidateUtil.dateType(dto.paymentDate, Constants.FORMAT.ISO8601_DATE, false, "errors.line.onlineorder.format",
				new Object[] { index, MessageResourcesUtil.getMessage("errors.onlineorder.reason.payments-date") });
		if(error != null) {
			addError(error);
		}
		// delivery-start-date(日付)
		error = ValidateUtil.dateType(dto.deliveryStartDate, Constants.FORMAT.ISO8601_DATE, false, "errors.line.onlineorder.format",
				new Object[] { index, MessageResourcesUtil.getMessage("errors.onlineorder.reason.delivery-start-date") });
		if(error != null) {
			addError(error);
		}
		// delivery-end-date(日付)
		error = ValidateUtil.dateType(dto.deliveryEndDate, Constants.FORMAT.ISO8601_DATE, false, "errors.line.onlineorder.format",
				new Object[] { index, MessageResourcesUtil.getMessage("errors.onlineorder.reason.delivery-end-date") });
		if(error != null) {
			addError(error);
		}
		// quantity-purchased(数値)
		error = ValidateUtil.integerType(dto.quantity, "errors.line.onlineorder.format",
				new Object[] { index, MessageResourcesUtil.getMessage("errors.onlineorder.reason.quantity-purchased") });
		if(error != null) {
			addError(error);
		}
		// item-price(数値)
		error = ValidateUtil.integerType(dto.price, "errors.line.onlineorder.format",
				new Object[] { index, MessageResourcesUtil.getMessage("errors.onlineorder.reason.item-price") });
		if(error != null) {
			addError(error);
		}
		// item-tax(数値)
		error = ValidateUtil.integerType(dto.taxPrice, "errors.line.onlineorder.format",
				new Object[] { index, MessageResourcesUtil.getMessage("errors.onlineorder.reason.item-tax") });
		if(error != null) {
			addError(error);
		}
		// shipping-price(数値)
		error = ValidateUtil.integerType(dto.shippingPrice, "errors.line.onlineorder.format",
				new Object[] { index, MessageResourcesUtil.getMessage("errors.onlineorder.reason.shipping-price") });
		if(error != null) {
			addError(error);
		}
		// shipping-tax(数値)
		error = ValidateUtil.integerType(dto.shippingTax, "errors.line.onlineorder.format",
				new Object[] { index, MessageResourcesUtil.getMessage("errors.onlineorder.reason.shipping-tax") });
		if(error != null) {
			addError(error);
		}

		// 長さチェック

		// order-id
		error = ValidateUtil.maxlength(dto.onlineOrderId, 30, "errors.line.maxlength",
				new Object[] { index, MessageResourcesUtil.getMessage("errors.onlineorder.reason.order-id"), 30 });
		if(error != null) {
			addError(error);
		}
		// order-item-id
		error = ValidateUtil.maxlength(dto.onlineItemId, 30, "errors.line.maxlength",
				new Object[] { index, MessageResourcesUtil.getMessage("errors.onlineorder.reason.order-item-id"), 30 });
		if(error != null) {
			addError(error);
		}
		// buyer-email
		error = ValidateUtil.maxlength(dto.customerEmail, 255, "errors.line.maxlength",
				new Object[] { index, MessageResourcesUtil.getMessage("errors.onlineorder.reason.buyer-email"), 255 });
		if(error != null) {
			addError(error);
		}
		// buyer-name
		error = ValidateUtil.maxlength(dto.customerName, 60, "errors.line.maxlength",
				new Object[] { index, MessageResourcesUtil.getMessage("errors.onlineorder.reason.buyer-name"), 60 });
		if(error != null) {
			addError(error);
		}
		// buyer-phone-number
		error = ValidateUtil.maxlength(dto.customerTel, 15, "errors.line.maxlength",
				new Object[] { index, MessageResourcesUtil.getMessage("errors.onlineorder.reason.buyer-phone-number"), 15 });
		if(error != null) {
			addError(error);
		}
		// sku
		error = ValidateUtil.maxlength(dto.sku, 30, "errors.line.maxlength",
				new Object[] { index, MessageResourcesUtil.getMessage("errors.onlineorder.reason.sku"), 30 });
		if(error != null) {
			addError(error);
		}
		// product-name
		error = ValidateUtil.maxlength(dto.productName, 120, "errors.line.maxlength",
				new Object[] { index, MessageResourcesUtil.getMessage("errors.onlineorder.reason.product-name"), 120 });
		if(error != null) {
			addError(error);
		}
		// currency
		error = ValidateUtil.maxlength(dto.currency, 10, "errors.line.maxlength",
				new Object[] { index, MessageResourcesUtil.getMessage("errors.onlineorder.reason.currency"), 10 });
		if(error != null) {
			addError(error);
		}
		// ship-service-level
		error = ValidateUtil.maxlength(dto.shipServiceLevel, 30, "errors.line.maxlength",
				new Object[] { index, MessageResourcesUtil.getMessage("errors.onlineorder.reason.ship-service-level"), 30 });
		if(error != null) {
			addError(error);
		}
		// recipient-name
		error = ValidateUtil.maxlength(dto.recipientName, 60, "errors.line.maxlength",
				new Object[] { index, MessageResourcesUtil.getMessage("errors.onlineorder.reason.recipient-name"), 60 });
		if(error != null) {
			addError(error);
		}
		// ship-address-1
		error = ValidateUtil.maxlength(dto.address1, 50, "errors.line.maxlength",
				new Object[] { index, MessageResourcesUtil.getMessage("errors.onlineorder.reason.ship-address-1"), 50 });
		if(error != null) {
			addError(error);
		}
		// ship-address-2とship-address-3
		StringBuffer sb = new StringBuffer(dto.address2);
		sb.append(dto.address3);
		error = ValidateUtil.maxlength(sb.toString(), 50, "errors.line.maxlength.sum",
				new Object[] { index, MessageResourcesUtil.getMessage("errors.onlineorder.reason.ship-address-2and3"), 50 });
		if(error != null) {
			addError(error);
		}
		// ship-city
		error = ValidateUtil.maxlength(dto.city, 60, "errors.line.maxlength",
				new Object[] { index, MessageResourcesUtil.getMessage("errors.onlineorder.reason.ship-city"), 60 });
		if(error != null) {
			addError(error);
		}
		// ship-state
		error = ValidateUtil.maxlength(dto.state, 60, "errors.line.maxlength",
				new Object[] { index, MessageResourcesUtil.getMessage("errors.onlineorder.reason.ship-state"), 60 });
		if(error != null) {
			addError(error);
		}
		// ship-postal-code
		error = ValidateUtil.maxlength(dto.zipCode, 8, "errors.line.maxlength",
				new Object[] { index, MessageResourcesUtil.getMessage("errors.onlineorder.reason.ship-postal-code"), 8 });
		if(error != null) {
			addError(error);
		}
		// ship-country
		error = ValidateUtil.maxlength(dto.country, 10, "errors.line.maxlength",
				new Object[] { index, MessageResourcesUtil.getMessage("errors.onlineorder.reason.ship-country"), 10 });
		if(error != null) {
			addError(error);
		}
		// ship-phone-number
		error = ValidateUtil.maxlength(dto.shipTel, 15, "errors.line.maxlength",
				new Object[] { index, MessageResourcesUtil.getMessage("errors.onlineorder.reason.ship-phone-number"), 15 });
		if(error != null) {
			addError(error);
		}
		// delivery-time-zone
		error = ValidateUtil.maxlength(dto.deliveryTimeZone, 20, "errors.line.maxlength",
				new Object[] { index, MessageResourcesUtil.getMessage("errors.onlineorder.reason.delivery-time-zone"), 20 });
		if(error != null) {
			addError(error);
		}
		// delivery-Instructions
		error = ValidateUtil.maxlength(dto.deliveryInst, 60, "errors.line.maxlength",
				new Object[] { index, MessageResourcesUtil.getMessage("errors.onlineorder.reason.delivery-Instructions"), 60 });
		if(error != null) {
			addError(error);
		}

		// Insert用リストに追加
		dtoList.add(dto);

	}
}
