/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.bill;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import jp.co.arkinfosys.action.CommonResources;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.dto.YmDto;
import jp.co.arkinfosys.form.bill.CloseArtBalanceForm;
import jp.co.arkinfosys.service.ArtBalanceService;
import jp.co.arkinfosys.service.BillService;
import jp.co.arkinfosys.service.CategoryService;
import jp.co.arkinfosys.service.CustomerService;
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
 * 売掛締め処理を実行するアクションクラスです
 *
 * @author Ark Information Systems
 *
 */
public class CloseArtBalanceAction extends CommonResources {
	/**
	 * 画面遷移用のマッピングクラス
	 */
	public static class Mapping {
		public static final String INPUT = "closeArtBalance.jsp";
	}

	// 画面とAction間でデータをやり取りするオブジェクトを定義する
	@ActionForm
	@Resource
	public CloseArtBalanceForm closeArtBalanceForm;

	@Resource
	protected CategoryService categoryService;

	@Resource
	protected BillService billService;

	@Resource
	protected YmService ymService;

	@Resource
	protected CustomerService customerService;

	@Resource
	private ArtBalanceService artBalanceService;

	// 支払条件リストの内容
	public List<LabelValueBean> cutoffGroupCategoryList = new ArrayList<LabelValueBean>();

	private SimpleDateFormat DF_YMD = new SimpleDateFormat(Constants.FORMAT.DATE);

	/**
	 * 初期表示処理を行います.<BR>
	 * 入力フォームを初期化し、再検索を行います.<BR>
	 *
	 * @return 画面遷移先URI
	 * @see CloseArtBalanceAction.Mapping.INPUT
	 */
	@Execute(validator = false)
	public String index() {
		try {

			// 入力フォームを初期化する
			closeArtBalanceForm.initialize();

			// 結果の再検索
			findOther();

		} catch (ServiceException e) {
			errProc(e);
			return null;
		}
		return CloseArtBalanceAction.Mapping.INPUT;
	}

	/**
	 * 売掛締処理を行います.<BR>
	 * 売掛締めの対象は全ての顧客となります.
	 * 締処理実施後は再検索を行い、処理終了メッセージを表示します.
	 *
	 * @return 画面遷移先URI
	 * @see CloseArtBalanceAction.Mapping.INPUT
	 */
	@Execute(validator = true, validate="validateCheckClose", stopOnValidationError=false, input = "index")
	public String close() {
		try {

			// 全てのユーザに対する売掛残高締め処理を行う
			ActionMessages msgs = artBalanceService.closeAll(closeArtBalanceForm.cutOffDate);
			if( msgs.size() > 0 ){
				ActionMessagesUtil.addMessages(super.httpRequest, msgs);
			}

			// 結果の再検索
			findOther();

			// 完了メッセージ
			addMessage("infos.closeArtBalance.close");
			ActionMessagesUtil.addMessages(super.httpRequest, super.messages);
		} catch (ServiceException e) {
			e.printStackTrace();
			errProc(e);
			addMessage( e.getMessage() );
			ActionMessagesUtil.addErrors(super.httpRequest, super.messages);
		} catch (UnabledLockException e) {
			e.printStackTrace();
			errProc(e);
			addMessage( e.getMessage() );
			ActionMessagesUtil.addErrors(super.httpRequest, super.messages);
		} catch (ParseException e) {
			e.printStackTrace();
			errProc(e);
			addMessage( e.getMessage() );
			ActionMessagesUtil.addErrors(super.httpRequest, super.messages);
		} catch (Exception e) {
			e.printStackTrace();
			errProc(e);
			addMessage( e.getMessage() );
			ActionMessagesUtil.addErrors(super.httpRequest, super.messages);
		}
		return CloseArtBalanceAction.Mapping.INPUT;
	}

	/**
	 * 売掛締解除処理を行います.<BR>
	 * 売掛締め解除の対象は全ての顧客となります.
	 * 締解除実施後は再検索を行い、処理終了メッセージを表示します.
	 *
	 * @return 画面遷移先URI
	 * @throws Exception
	 * @see CloseArtBalanceAction.Mapping.INPUT
	 */
	@Execute(validator = true, validate="validateCheckReopen", stopOnValidationError=false, input = "index")
	public String reopen() throws Exception {
		try {

			// 全顧客を順に締め解除する
			ActionMessages msgs = artBalanceService.reOpenAll(closeArtBalanceForm.lastCutOffDate);
			if( msgs.size() > 0 ){
				ActionMessagesUtil.addMessages(super.httpRequest, msgs);
			}
			// 結果の再検索
			findOther();

			// 完了メッセージ
			addMessage("infos.closeArtBalance.reopen");
			ActionMessagesUtil.addMessages(super.httpRequest, super.messages);
		} catch (ServiceException e) {
			e.printStackTrace();
			errProc(e);
			addMessage( e.getMessage() );
			ActionMessagesUtil.addErrors(super.httpRequest, super.messages);
		} catch (UnabledLockException e) {
			e.printStackTrace();
			errProc(e);
			addMessage( e.getMessage() );
			ActionMessagesUtil.addErrors(super.httpRequest, super.messages);
		} catch (ParseException e) {
			e.printStackTrace();
			errProc(e);
			addMessage( e.getMessage() );
			ActionMessagesUtil.addErrors(super.httpRequest, super.messages);
		} catch (Exception e) {
			e.printStackTrace();
			errProc(e);
//			throw e;
			addMessage( e.getMessage() );
			ActionMessagesUtil.addErrors(super.httpRequest, super.messages);
		}
		return CloseArtBalanceAction.Mapping.INPUT;
	}

	/**
	 * エラーメッセージを登録します.<BR>
	 * 可変引数の第一引数にリスースを示す文字列（"errors.","infos"）が
	 * 含まれていない場合には、そのまま登録し、含まれる場合には
	 * リソースからメッセージを取りだして登録します。<BR>
	 * それ以外の引数は、第一引数が示すメッセージ文字列に対する置換文字列として使用されます.
	 *
	 * @param arg メッセージ引数（可変）
	 */
	private void addMessage( String... arg  ) {

		// 第一引数にerrors.が入っていない時には文言が入っている
		if(( arg[0].indexOf("errors.") < 0 )&&( arg[0].indexOf("infos.") < 0 )){
			super.messages.add(ActionMessages.GLOBAL_MESSAGE,
					new ActionMessage( "errors.none", arg[0]));
		}else{
			switch (arg.length) {
			case 1:
				super.messages.add(ActionMessages.GLOBAL_MESSAGE,
						new ActionMessage( arg[0] ));
				break;
			case 2:
				super.messages.add(ActionMessages.GLOBAL_MESSAGE,
						new ActionMessage( arg[0], arg[1]));
				break;
			case 3:
				super.messages.add(ActionMessages.GLOBAL_MESSAGE,
						new ActionMessage( arg[0], arg[1], arg[2]));
				break;
			default:
				super.messages.add(ActionMessages.GLOBAL_MESSAGE,
						new ActionMessage( arg[0] ));
				break;
			}
		}
	}

	/**
	 * エラーログを出力します
	 * @param e 例外オブジェクト
	 */
	private void errProc( Exception e) {
		e.printStackTrace();
		super.errorLog(e);
	}


	/**
	 * 売掛締実行処理で使用するバリデータです.<BR>
	 * 最終締日以前を指定して締処理を行おうとした場合にはエラーとします.<BR>
	 * 同月で再度締めようとした場合にもエラーとします.
	 *
	 * @return エラー情報　ActionErrors形式
	 */
	public ActionErrors validateCheckClose() {
		ActionErrors errors = new ActionErrors();

		// 売掛締が1度も行われていない場合はエラーなし
		if(closeArtBalanceForm.lastCutOffDate.length() == 0) {
			return errors;
		}

		// 締年度確定　値が変ならエラーで返す
		YmDto ymDto;
		try {
			ymDto = ymService.getYm(closeArtBalanceForm.cutOffDate);
			if( ymDto == null ){
				throw new Exception();
			}
			// 最終締日以前での締めはエラーとする
			Date lcd = DF_YMD.parse(closeArtBalanceForm.lastCutOffDate);
			Date pcd = DF_YMD.parse(closeArtBalanceForm.cutOffDate); // 締め処理日
			if( !pcd.after(lcd) ){
				errors.add(ActionMessages.GLOBAL_MESSAGE,
						new ActionMessage("errors.closeArtBalance.closeDate"));
				return errors;
			}
			// 同月での締めはエラーとする
			YmDto ymLast = ymService.getYm(closeArtBalanceForm.lastCutOffDate);
			if( ymLast.annual.equals( ymDto.annual )&& ymLast.monthly.equals( ymDto.monthly )){
				errors.add(ActionMessages.GLOBAL_MESSAGE,
						new ActionMessage("errors.closeArtBalance.exist"));
				return errors;
			}
		} catch (Exception e) {
			e.printStackTrace();
			String strLabel = MessageResourcesUtil.getMessage("labels.billCloseDate");
			errors.add(ActionMessages.GLOBAL_MESSAGE,
					new ActionMessage("errors.date", strLabel));
			return errors;
		}

		return errors;
	}

	/**
	 * 売掛締解除処理で使用するバリデータです.<BR>
	 * 現在の実装では、チェックを行っていません.<BR>
	 *
	 * @return エラー情報　ActionErrors形式
	 */
	public ActionErrors validateCheckReopen() {
		String strLabel = MessageResourcesUtil.getMessage("words.action.cutoffCancel");
		return validateCheck( strLabel );
	}

	/**
	 * 売掛締解除処理で使用されるバリデーションチェックです.<BR>
	 * 現在の実装では、チェックを行っていません.<BR>
	 *
	 * @param label エラー発生時のメッセージ文字列
	 * @return エラー情報　ActionErrors形式
	 */
	public ActionErrors validateCheck(String label ) {
		ActionErrors errors = new ActionErrors();

		return errors;
	}

	/**
	 * 売掛締め処理日を検索し、アクションフォームに設定します.
	 * @throws ServiceException
	 */
	protected void findOther() throws ServiceException {

		// 最も新しい売掛締め処理日を検索
		Date lastDate = customerService.findLastCloseArtBalanceCutOffDate();
		if( lastDate != null ){
			SimpleDateFormat DF_YMD = new SimpleDateFormat(Constants.FORMAT.DATE);
			closeArtBalanceForm.lastCutOffDate = DF_YMD.format(lastDate);
		}else{
			closeArtBalanceForm.initialize();
		}
	}

}
