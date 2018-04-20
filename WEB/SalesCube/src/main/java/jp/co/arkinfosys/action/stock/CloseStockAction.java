/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.stock;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.annotation.Resource;

import jp.co.arkinfosys.action.CommonResources;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.common.StringUtil;
import jp.co.arkinfosys.dto.YmDto;
import jp.co.arkinfosys.form.stock.CloseStockForm;
import jp.co.arkinfosys.service.exception.ServiceException;
import jp.co.arkinfosys.service.stock.CloseStockService;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.util.ActionMessagesUtil;

/**
 * 在庫締処理画面のアクションです.
 * @author Ark Information Systems
 *
 */
public class CloseStockAction extends CommonResources {

	/**
	 * 画面遷移用のマッピングクラスです.
	 */
	public static class Mapping {
		public static final String INDEX = "/stock/closeStock/";
		public static final String INPUT = "closeStock.jsp";
	}

	@ActionForm
	@Resource
	private CloseStockForm closeStockForm;

	@Resource
	private CloseStockService closeStockService;

	/**
	 * 初期表示処理を行います.
	 * @throws Exception
	 */
	@Execute(validator = false)
	public String index() throws Exception {
		// 最終締年月日
		Date lastCutoffDate = closeStockService.findMaxStockPDateDate();
		closeStockForm.lastCutoffDate = StringUtil.getDateString(Constants.FORMAT.DATE, lastCutoffDate);

		return Mapping.INPUT;
	}

	/**
	 * 締処理を実行します.
	 * @throws Exception
	 */
	@Execute(validator = true, stopOnValidationError = true, validate = "validate", input = Mapping.INDEX)
	public String close() throws Exception {
		try {
			// 締実行
			Integer resultCount = closeStockService.close( StringUtil.zenkakuNumToHankaku(closeStockForm.cutoffDate));

			if(resultCount == 0) {
				// 締実行完了メッセージ表示（処理対象なし）
				super.messages.add(ActionMessages.GLOBAL_MESSAGE,
									new ActionMessage("infos.stock.close.noData"));
				ActionMessagesUtil.addErrors(super.httpSession, super.messages);
			} else {
				// 締実行完了メッセージ表示
				super.messages.add(ActionMessages.GLOBAL_MESSAGE,
									new ActionMessage("infos.stock.close"));
				ActionMessagesUtil.addMessages(super.httpSession, super.messages);
			}
		} catch (ServiceException e) {
			super.errorLog(e);

			// 続行可能？
			if(e.isStopOnError()) {
				// システム例外として処理する
				throw e;
			}
		}

		return Mapping.INDEX + "?redirect=true";
	}

	/**
	 * 締解除処理を実行します.
	 * @return 遷移先URI
	 * @throws Exception
	 */
	@Execute(validator = false, input = Mapping.INPUT)
	public String reopen() throws Exception {
		try {
			// 締解除
			closeStockService.reopen();

			// 締実行完了メッセージ表示
			super.messages.add(ActionMessages.GLOBAL_MESSAGE,
								new ActionMessage("infos.stock.reopen"));
			ActionMessagesUtil.addMessages(super.httpSession, super.messages);
		} catch (ServiceException e) {
			super.errorLog(e);

			// 続行可能？
			if(e.isStopOnError()) {
				// システム例外として処理する
				throw e;
			}
		}

		return Mapping.INDEX + "?redirect=true";
	}

	/**
	 * 締処理日のチェックを行います.
	 * @return アクションメッセージ
	 * @throws ServiceException
	 */
    public ActionMessages validate() throws ServiceException {
		ActionMessages errors = new ActionMessages();
		SimpleDateFormat sdf = new SimpleDateFormat(Constants.FORMAT.DATE);

		// 最終締年月日を再取得する
		Date lastCutoffDate = closeStockService.findMaxStockPDateDate();

		// 値チェック

		// 在庫締処理日
		if(StringUtil.hasLength(closeStockForm.cutoffDate) && lastCutoffDate != null) {
			try {
				boolean err = false;
				Date cutoffDate = sdf.parse(closeStockForm.cutoffDate);
				if(lastCutoffDate.compareTo(cutoffDate) >= 0) {
					// 在庫締処理日が最終締処理日以前の場合はエラー
					err = true;
					errors.add(ActionMessages.GLOBAL_MESSAGE,
							new ActionMessage("errors.cutoffDate.eq.less"));
				}

				if(!err) {
					YmDto cutoffDto = closeStockService.getYm(cutoffDate);
					YmDto lastCutoffDto = closeStockService.getYm(lastCutoffDate);
					if(cutoffDto.ym.equals(lastCutoffDto.ym)) {
						// 在庫締処理日が最終締処理日と同じ年月の場合はエラー
						errors.add(ActionMessages.GLOBAL_MESSAGE,
								new ActionMessage("errors.cutoffDate.eq"));
					}
				}
			} catch (ParseException e) {
				// パースエラーの場合はフォーマットチェックエラーが
				// 既に表示されているので何もしない
			}
		}

		return errors;
    }

}
