/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.bill;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import jp.co.arkinfosys.action.AbstractSearchAction;
import jp.co.arkinfosys.common.Categories;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.dto.YmDto;
import jp.co.arkinfosys.dto.bill.CloseBillLineDto;
import jp.co.arkinfosys.entity.Bill;
import jp.co.arkinfosys.entity.Customer;
import jp.co.arkinfosys.entity.join.CustomerJoin;
import jp.co.arkinfosys.form.AbstractSearchForm;
import jp.co.arkinfosys.form.bill.CloseBillForm;
import jp.co.arkinfosys.service.BillService;
import jp.co.arkinfosys.service.CategoryService;
import jp.co.arkinfosys.service.CustomerService;
import jp.co.arkinfosys.service.SalesService;
import jp.co.arkinfosys.service.YmService;
import jp.co.arkinfosys.service.exception.ServiceException;
import jp.co.arkinfosys.service.exception.UnabledLockException;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.LabelValueBean;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.util.ActionMessagesUtil;
import org.seasar.struts.util.MessageResourcesUtil;

/**
 * 請求締処理画面のアクションクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class CloseBillAction extends AbstractSearchAction<CloseBillLineDto> {
	/**
	 * 画面遷移用のマッピングクラス
	 */
	protected static class Mapping extends AbstractSearchAction.Mapping {
		public static final String INPUT = "closeBill.jsp";
	}

	// 画面とAction間でデータをやり取りするオブジェクトを定義する
	@ActionForm
	@Resource
	public CloseBillForm closeBillForm;

	@Resource
	protected CategoryService categoryService;

	@Resource
	protected BillService billService;

	@Resource
	protected YmService ymService;

	@Resource
	protected CustomerService customerService;

	@Resource
	protected SalesService salesService;

	/**
	 * 請求締実行処理日の初期値として当日をアクションフォームにセットします.<br>
	 * @throws Exception
	 */
	@Override
	protected void doAfterIndex() throws Exception {
		this.closeBillForm.cutOffDate = new SimpleDateFormat(
				Constants.FORMAT.DATE).format(new Date());
	}

	/**
	 * 入力画面のURIを返します.<br>
	 * @return 入力画面URI
	 */
	@Override
	protected String getInputURIString() {
		return Mapping.INPUT;
	}

	/**
	 * 締処理を行います.<BR>
	 * 締処理画面で選択した顧客に対して順に請求締処理を行います.
	 *
	 * @return 入力画面URI
	 */
	@Execute(validator = true, validate = "validateCheckClose", stopOnValidationError = false, input = "init")
	public String close() {
		try {

			// 区分情報を作成する
			this.createList();

			for (CloseBillLineDto cblf : closeBillForm.searchResultList) {
				if (cblf.closeCheck == true) {
					// 掛売でチェックした顧客
					billService.closeBillArt(closeBillForm.cutOffDate,
							cblf.customerCode);
				}
			}
			// 結果の再検索
			find();

			// 完了メッセージ
			addMessage("infos.closeBill.close");
			ActionMessagesUtil.addMessages(super.httpRequest, super.messages);
		} catch (ServiceException e) {
			e.printStackTrace();
			errProc(e);
			addMessage(e.getMessage());
			ActionMessagesUtil.addErrors(super.httpRequest, super.messages);
		} catch (UnabledLockException e) {
			e.printStackTrace();
			errProc(e);
			addMessage(e.getMessage());
			ActionMessagesUtil.addErrors(super.httpRequest, super.messages);
		} catch (ParseException e) {
			e.printStackTrace();
			errProc(e);
			addMessage(e.getMessage());
			ActionMessagesUtil.addErrors(super.httpRequest, super.messages);
		} catch (Exception e) {
			e.printStackTrace();
			errProc(e);
			addMessage(e.getMessage());
			ActionMessagesUtil.addErrors(super.httpRequest, super.messages);
		}
		return CloseBillAction.Mapping.INPUT;
	}

	/**
	 * 締解除処理を行います.<BR>
	 * 締処理画面で選択した顧客に対して順に請求締解除処理を行います.
	 *
	 * @return 入力画面URI
	 * @throws Exception
	 */
	@Execute(validator = true, validate = "validateCheckReopen", stopOnValidationError = false, input = "init")
	public String reopen() throws Exception {
		try {

			// 区分情報を作成する
			this.createList();

			for (CloseBillLineDto cblf : closeBillForm.searchResultList) {
				if (cblf.closeCheck == true) {
					// 掛売でチェックした顧客
					ActionMessage msg = billService.reOpenBillArt(
							cblf.billCutoffDate, cblf.customerCode);
					if (msg != null) {
						super.messages.add(ActionMessages.GLOBAL_MESSAGE, msg);
					}
				}
			}
			// 結果の再検索
			find();

			// 完了メッセージ
			addMessage("infos.closeBill.reopen");
			ActionMessagesUtil.addMessages(super.httpRequest, super.messages);
		} catch (ServiceException e) {
			e.printStackTrace();
			errProc(e);
			addMessage(e.getMessage());
			ActionMessagesUtil.addErrors(super.httpRequest, super.messages);
		} catch (UnabledLockException e) {
			e.printStackTrace();
			errProc(e);
			addMessage(e.getMessage());
			ActionMessagesUtil.addErrors(super.httpRequest, super.messages);
		} catch (ParseException e) {
			e.printStackTrace();
			errProc(e);
			addMessage(e.getMessage());
			ActionMessagesUtil.addErrors(super.httpRequest, super.messages);
		} catch (Exception e) {
			e.printStackTrace();
			errProc(e);
			//			throw e;
			addMessage(e.getMessage());
			ActionMessagesUtil.addErrors(super.httpRequest, super.messages);
		}
		return CloseBillAction.Mapping.INPUT;
	}

	/**
	 * バリデーションエラーが発生した場合の初期化処理用メソッドです.<BR>
	 * 初期化処理を実施後、入力画面の遷移URIを返します.<BR>
	 * @return 入力画面URI
	 */
	@Execute(validator = false)
	public String init() {
		try {

			// 区分情報を作成する
			this.createList();

		} catch (ServiceException e) {
			errProc(e);
			if ("".equals(e.getMessage()) == false) {
				addMessage(e.getMessage());
			} else {
				addMessage("errors.system");
			}
			ActionMessagesUtil.addErrors(super.httpRequest, super.messages);
		} catch (Exception e) {
			errProc(e);
			addMessage("errors.system");
			ActionMessagesUtil.addErrors(super.httpRequest, super.messages);
		}
		return CloseBillAction.Mapping.INPUT;
	}

	/**
	 * エラーメッセージを登録します<br>
	 * 第一引数に"erroes.","infos."の文字列が含まれる場合には、リソースファイルから文字列を取得します<br>
	 * 第二引数以降は、第一引数が示すメッセージの置換文字列として使用されます.<br>
	 * @param arg　メッセージ引数（可変）
	 */
	private void addMessage(String... arg) {

		// 第一引数にerrors.が入っていない時には文言が入っている
		if ((arg[0].indexOf("errors.") < 0) && (arg[0].indexOf("infos.") < 0)) {
			super.messages.add(ActionMessages.GLOBAL_MESSAGE,
					new ActionMessage("errors.none", arg[0]));
		} else {
			switch (arg.length) {
			case 1:
				super.messages.add(ActionMessages.GLOBAL_MESSAGE,
						new ActionMessage(arg[0]));
				break;
			case 2:
				super.messages.add(ActionMessages.GLOBAL_MESSAGE,
						new ActionMessage(arg[0], arg[1]));
				break;
			case 3:
				super.messages.add(ActionMessages.GLOBAL_MESSAGE,
						new ActionMessage(arg[0], arg[1], arg[2]));
				break;
			default:
				super.messages.add(ActionMessages.GLOBAL_MESSAGE,
						new ActionMessage(arg[0]));
				break;
			}
		}
	}

	/**
	 * エラーログを出力します.
	 * @param e　例外オブジェクト
	 */
	private void errProc(Exception e) {
		e.printStackTrace();
		super.errorLog(e);
	}

	/**
	 * 請求締実行処理用のバリデータです.<BR>
	 * 締実行日が未設定の場合にはエラーとします<BR>
	 * 指定した締日と同じ年月の最終締めデータがある時にはエラーとします<BR>
	 * 締対象が選択されていない場合にはエラーとします.
	 *
	 * @return エラー情報　ActionErrors形式
	 */
	public ActionErrors validateCheckClose() {
		ActionErrors errors = new ActionErrors();

		// 締年度確定　値が変ならエラーで返す
		YmDto ymDto;
		try {
			ymDto = ymService.getYm(closeBillForm.cutOffDate);
			if (ymDto == null) {
				throw new Exception();
			}
		} catch (Exception e) {
			e.printStackTrace();
			String strLabel = MessageResourcesUtil
					.getMessage("labels.billCloseDate");
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.date", strLabel));
			return errors;
		}

		// チェック状態のチェック
		boolean bCheckExist = false;
		if (closeBillForm.otherUser.closeCheck == true) {
			bCheckExist = true;
			// 売掛以外の締状態をチェック
			if (checkYm(ymDto, closeBillForm.otherUser.billCutoffDate, null) == false) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.closeBill.exist", MessageResourcesUtil
								.getMessage("labels.closeBill.other"), ""));
			}
		}
		if( closeBillForm.searchResultList != null ){
			for (CloseBillLineDto cblf : closeBillForm.searchResultList) {
				if (cblf.closeCheck == true) {
					bCheckExist = true;
					// 売掛の締状態をチェック
					if (checkYm(ymDto, cblf.billCutoffDate, cblf.customerCode) == false) {
						errors.add(ActionMessages.GLOBAL_MESSAGE,
								new ActionMessage("errors.closeBill.exist",
										cblf.customerCode, cblf.customerName));
					}
				}
			}
		}
		// チェック未入力の場合
		if (bCheckExist == false) {
			String strLabel = MessageResourcesUtil
					.getMessage("words.action.cutoff");
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.closeBill.select", strLabel));
			return errors;
		}
		return errors;
	}

	/**
	 * 締処理の実行可能状態を返します.<BR>
	 * 指定した締日と同じ年月の最終締めデータがある時には実行できません.<BR>
	 * 最終締め日以前の日付での締処理はできません.<BR>
	 *
	 * @param ym　指定締年月
	 * @param cutoffDate　最終締日
	 * @param customerCode　顧客コード
	 * @return 締可能か否か
	 */
	protected boolean checkYm(YmDto ym, String cutoffDate, String customerCode) {
		YmDto ymDto;
		try {
			String passLabel = MessageResourcesUtil
					.getMessage("labels.billNoData");

			// 請求実績なしはOK
			if (passLabel.equals(cutoffDate)) {
				return true;
			}
			ymDto = ymService.getYm(cutoffDate);
			if (ymDto == null) {
				return true;
			}//else{
			if (ym.annual.equals(ymDto.annual)
					&& ym.monthly.equals(ymDto.monthly)) {
				// 直近の請求書の日付が同月かどうかチェックする
				if (customerCode != null) {
					List<Bill> billList = billService
							.findLastBillByCustomerCode(customerCode);
					if (billList.size() != 0) {
						Bill b = billList.get(0);
						// 同じ月に請求データがある場合には締めることはできない
						if (ym.annual.equals(b.billYear.intValue())
								&& ym.monthly.equals(b.billMonth.intValue())) {
							return false;
						} else {
							return true;
						}
					}
				}
				return false;
			}
			// 最終締日以前での締めはエラーとする
			SimpleDateFormat dateFormat = new SimpleDateFormat(
					Constants.FORMAT.DATE);
			Date lcd = dateFormat.parse(cutoffDate);
			Date pcd = dateFormat.parse(this.closeBillForm.cutOffDate); // 締め処理日
			if (!pcd.after(lcd)) {
				return false;
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return true;
		}
	}

	/**
	 * 請求締解除処理用に使用されるバリデータです.<BR>
	 * 締解除対象が選択されていない場合にはエラーとします.
	 *
	 * @return エラー情報　ActionErrors形式
	 */
	public ActionErrors validateCheckReopen() {
		String strLabel = MessageResourcesUtil
				.getMessage("words.action.cutoffCancel");
		return validateCheck(strLabel);
	}

	/**
	 * 締解除対象が選択されている事を確認します.
	 *
	 * @return エラー情報　ActionErrors形式
	 */
	public ActionErrors validateCheck(String label) {
		ActionErrors errors = new ActionErrors();

		if (closeBillForm.otherUser.closeCheck == true) {
			return errors;
		}
		if(closeBillForm.searchResultList != null){
			for (CloseBillLineDto cblf : closeBillForm.searchResultList) {
				if (cblf.closeCheck == true) {
					return errors;
				}
			}
		}
		// 検索条件未入力の場合
		errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
				"errors.closeBill.select", label));

		return errors;
	}

	/**
	 * 売掛顧客の検索条件にあった検索を実行します.<BR>
	 * 検索結果はアクションフォームに格納します.
	 *
	 * @throws ServiceException
	 * @see jp.co.arkinfosys.service.CustomerService#findCustomerForCloseBill
	 */
	protected void find() throws ServiceException {

		// 掛売の請求書情報を検索する
		closeBillForm.searchResultList.clear();
		List<CustomerJoin> creditList = customerService
				.findCustomerForCloseBill(
						true,
						closeBillForm.customerCode,
						closeBillForm.customerName,
						this.categoryService
								.cutoffGroupCategoryToCutoffGroup(closeBillForm.cutoffGroupCategory),
						this.categoryService
								.cutoffGroupCategoryToPaybackCycleCategory(closeBillForm.cutoffGroupCategory),
						closeBillForm.notYetRequestedCheck);

		for (Customer bj : creditList) {
			CloseBillLineDto cblf = new CloseBillLineDto();
			cblf.initialize(bj);
			closeBillForm.searchResultList.add(cblf);
		}
	}

	/**
	 * 検索条件に表示するプルダウンリストの情報をアクションフォームに設定します.
	 */
	@Override
	protected void createList() throws ServiceException {
		this.closeBillForm.cutoffGroupCategoryList = this.categoryService
				.findCategoryLabelValueBeanListById(Categories.CUTOFF_GROUP);
		this.closeBillForm.cutoffGroupCategoryList.add(0, new LabelValueBean());
		// カテゴリを作成済
		this.closeBillForm.initCategory = false;
	}

	/**
	 * 請求締処理アクションで使用するアクションフォームを返します.<br>
	 * @return 請求締処理用アクションフォーム
	 * @see jp.co.arkinfosys.form.bill.CloseBillForm
	 */
	@Override
	protected AbstractSearchForm<CloseBillLineDto> getActionForm() {
		return this.closeBillForm;
	}

	/**
	 * 請求締処理画面のメニューIDを返します.<br>
	 * @return 請求締処理画面メニューID
	 */
	@Override
	protected String getSearchMenuID() {
		return Constants.MENU_ID.CLOSE_BILL;
	}
}
