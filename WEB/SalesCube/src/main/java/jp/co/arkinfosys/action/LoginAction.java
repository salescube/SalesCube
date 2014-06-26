/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action;

import java.util.List;

import javax.annotation.Resource;

import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.common.EncryptUtil;
import jp.co.arkinfosys.common.StringUtil;
import jp.co.arkinfosys.entity.Domain;
import jp.co.arkinfosys.entity.Mine;
import jp.co.arkinfosys.entity.User;
import jp.co.arkinfosys.entity.join.MenuJoin;
import jp.co.arkinfosys.form.LoginForm;
import jp.co.arkinfosys.interceptor.AbstractLoginCheckInterceptor;
import jp.co.arkinfosys.service.DomainService;
import jp.co.arkinfosys.service.MenuService;
import jp.co.arkinfosys.service.MineService;
import jp.co.arkinfosys.service.UserService;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.util.ActionMessagesUtil;

/**
 * ログイン処理アクションクラスです.
 * @author Ark Information Systems
 *
 */
public class LoginAction extends CommonResources {

	/**
	 * 画面遷移用のマッピングクラスです.
	 * @author Ark Information Systems
	 *
	 */
	public static class Mapping {
		public static final String INPUT = "login.jsp";

		public static final String SUCCESS = "/menu";

		public static final String PASSWORD = "/setting/changePassword";
	}

	@ActionForm
	@Resource
	private LoginForm loginForm;

	@Resource
	private DomainService domainService;

	@Resource
	private UserService userService;

	@Resource
	private MenuService menuService;

	@Resource
	private MineService mineService;

	protected Logger logger = Logger.getLogger(LoginAction.class);

	/**
	 * 初期表示処理を行います.<br>
	 * 何かしらの問題があった場合、画面にメッセージを表示します.<br>
	 * 処理実行後、{@link Mapping#INPUT}で定義されたURIに遷移します.
	 * @return 画面遷移先のURI文字列
	 */
	@Execute(validator = false, urlPattern = "{domainId}")
	public String index() {
		try {
			if (this.loginForm.domainId == null
					&& super.domainDto.domainId == null) {
				// ドメインIDの存在チェック
				super.messages.add(ActionMessages.GLOBAL_MESSAGE,
						new ActionMessage("errors.missing.domain"));
				ActionMessagesUtil.addErrors(super.httpRequest, super.messages);
				return LoginAction.Mapping.INPUT;
			}
			String domainId = this.loginForm.domainId == null ? super.domainDto.domainId
					: this.loginForm.domainId;

			Domain domain = this.domainService.findById(domainId);
			if (domain == null) {
				super.messages.add(ActionMessages.GLOBAL_MESSAGE,
						new ActionMessage("errors.missing.domain"));
			} else {
				// ドメイン情報をセッション管理DTOに格納する
				Beans.copy(domain, super.domainDto).execute();
			}
			// SSO用修正
			if( StringUtil.hasLength( this.loginForm.userId ) && StringUtil.hasLength( this.loginForm.password ) ){
				return login();
			}

			ActionMessages loginErrors = (ActionMessages) super.httpRequest
					.getAttribute(AbstractLoginCheckInterceptor.LOGIN_CHECK_MESSAGES_KEY);
			if (loginErrors != null) {
				// 未ログインエラー
				super.messages.add(loginErrors);
			}

			ActionMessagesUtil.addErrors(super.httpRequest, super.messages);

		} catch (Exception e) {
			super.errorLog(e);
		}
		return LoginAction.Mapping.INPUT;
	}

	/**
	 * ログイン処理を行います.<br>
	 * 何かしらの問題があった場合、画面にメッセージを表示します.<br>
	 * 正常終了時は{@link Mapping#SUCCESS}、失敗時は{@link Mapping#INPUT}、パスワード期限切れ時は{@link Mapping#PASSWORD}で定義されたURIに遷移します.
	 * @return 画面遷移先のURI文字列
	 */
	@Execute(validator = true, validate = "validate", input = LoginAction.Mapping.INPUT)
	public String login() {
		try {
			if (super.domainDto.domainId == null) {
				// ドメインチェック
				super.messages.add(ActionMessages.GLOBAL_MESSAGE,
						new ActionMessage("errors.missing.domain"));
				ActionMessagesUtil.addErrors(super.httpRequest, super.messages);
				return LoginAction.Mapping.INPUT;
			}

			// 自社マスタのパスワード入力失敗回数を取得する
			Integer retryCount = 0;
			Mine mine = this.mineService.getMine();
			if (mine != null) {
				retryCount = mine.totalFailCount;
			}

			// ユーザIDでユーザ存在チェック
			User user = this.userService.findById(loginForm.userId);

			// ユーザが存在しない場合はエラー
			if (user == null) {
				super.messages.add(ActionMessages.GLOBAL_MESSAGE,
						new ActionMessage("errors.invalid.login"));
				ActionMessagesUtil.addErrors(super.httpRequest, super.messages);
				return LoginAction.Mapping.INPUT;
			}

			// ユーザが存在する場合の処理
			if (user.lockflg == null) {
				user.lockflg = "0";
			}

			// パスワードロックされているユーザの場合はエラー
			if (user.lockflg.equalsIgnoreCase("1")) {
				super.messages.add(ActionMessages.GLOBAL_MESSAGE,
						new ActionMessage("errors.invalid.login"));
				super.messages.add(ActionMessages.GLOBAL_MESSAGE,
						new ActionMessage("errors.lock.user", retryCount.toString()));
				ActionMessagesUtil.addErrors(super.httpRequest, super.messages);
				return LoginAction.Mapping.INPUT;
			}

			String encryptPassword = EncryptUtil.encrypt(loginForm.password);

			// 入力されたパスワードが社員マスタに登録されたパスワードと異なる場合はエラー
			if (!encryptPassword.equalsIgnoreCase(user.password)) {

				// エラーメッセージ
				super.messages.add(ActionMessages.GLOBAL_MESSAGE,
						new ActionMessage("errors.invalid.login"));

				// 自社マスタのパスワード入力失敗回数に値が設定されている場合
				if (retryCount != null) {

					// 失敗カウントがnullの場合は0を設定する
					if (user.failCount == null) {
						user.failCount = 0;
					}

					user.failCount++;

					// 社員マスタの失敗カウントが自社マスタのパスワード入力失敗回数以上になった場合は、社員マスタのロックフラグをONにする
					if (user.failCount >= retryCount) {
						user.lockflg = "1";
					}

					this.userService.updateFailCountAndLockFlg(user.userId, user.lockflg, user.failCount);

					// 指定回数以上のエラーでロックされる旨のメッセージを出力
					super.messages.add(ActionMessages.GLOBAL_MESSAGE,
							new ActionMessage("errors.lock.user", retryCount.toString()));
				}

				ActionMessagesUtil.addErrors(super.httpRequest, super.messages);
				return LoginAction.Mapping.INPUT;
			}

			// 入力されたパスワードが社員マスタに登録されたパスワードと同じ場合はOK
			// 社員マスタの失敗カウントを0に更新する
			this.userService.updateFailCountAndLockFlg(user.userId, "0", 0);

			// ログイン情報をセッション管理DTOに格納する
			Beans.copy(user, super.userDto).execute();

			// ユーザーが利用可能なメニュー一覧を取得する
			List<MenuJoin> menuJoinList = this.menuService
					.findMenuByUserId(loginForm.userId);
			super.userDto.menuDtoList = this.menuService
					.convertMenuJoinToDto(menuJoinList);

			// ユーザーのファイル参照権限を設定する
			// 商品のダウンロード/アップロード権限を設定する
			for (MenuJoin menuJoin : menuJoinList) {
				if (Constants.MENU_ID.REFERENCE_FILES.equals(menuJoin.menuId)) {
					super.userDto.fileOpenLevel = menuJoin.validFlag;
				}
				if (Constants.MENU_ID.MASTER_PRODUCT_UPDWN
						.equals(menuJoin.menuId)) {
					super.userDto.validUpDwnProducts = true;
				}
			}

			// 自社マスタの情報を取得する
			//Mine mine = this.mineService.getMine();
			if (mine != null) {
				// 自社情報をセッション管理DTOに格納する
				Beans.copy(mine,super.mineDto).execute();
				mineDto.initDecAlignFormat();

				if (mine.passwordValidDays != null
						&& this.userDto.isPasswordExpired()) {
					// 自社マスタの設定がnullでなく、パスワードが期限切れの場合
					return LoginAction.Mapping.PASSWORD;
				}
			}

			return LoginAction.Mapping.SUCCESS;
		} catch (Exception e) {
			super.errorLog(e);
		}
		// ログイン画面ではエラー画面に遷移しない
		return LoginAction.Mapping.INPUT;
	}
}
