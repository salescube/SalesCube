/*
 *  Copyright 2009-2010 Ark Information Systems.
 */

package jp.co.arkinfosys.action;

import java.util.List;

import javax.annotation.Resource;

import jp.co.arkinfosys.common.Constants;
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
				
				Beans.copy(domain, super.domainDto).execute();
			}
			
			if( StringUtil.hasLength( this.loginForm.userId ) && StringUtil.hasLength( this.loginForm.password ) ){
				return login();
			}

			ActionMessages loginErrors = (ActionMessages) super.httpRequest
					.getAttribute(AbstractLoginCheckInterceptor.LOGIN_CHECK_MESSAGES_KEY);
			if (loginErrors != null) {
				
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
				
				super.messages.add(ActionMessages.GLOBAL_MESSAGE,
						new ActionMessage("errors.missing.domain"));
				ActionMessagesUtil.addErrors(super.httpRequest, super.messages);
				return LoginAction.Mapping.INPUT;
			}

			User user = this.userService.findUserByIdAndPassword(
					loginForm.userId, loginForm.password);
			if (user == null) {
				
				super.messages.add(ActionMessages.GLOBAL_MESSAGE,
						new ActionMessage("errors.invalid.login"));
				ActionMessagesUtil.addErrors(super.httpRequest, super.messages);
				return LoginAction.Mapping.INPUT;
			}

			
			Beans.copy(user, super.userDto).execute();

			
			List<MenuJoin> menuJoinList = this.menuService
					.findMenuByUserId(loginForm.userId);
			super.userDto.menuDtoList = this.menuService
					.convertMenuJoinToDto(menuJoinList);

			
			
			for (MenuJoin menuJoin : menuJoinList) {
				if (Constants.MENU_ID.REFERENCE_FILES.equals(menuJoin.menuId)) {
					super.userDto.fileOpenLevel = menuJoin.validFlag;
				}
				if (Constants.MENU_ID.MASTER_PRODUCT_UPDWN
						.equals(menuJoin.menuId)) {
					super.userDto.validUpDwnProducts = true;
				}
			}

			
			Mine mine = this.mineService.getMine();
			if (mine != null) {
				
				Beans.copy(mine, super.mineDto).execute();
				mineDto.initDecAlignFormat();

				if (mine.passwordValidDays != null
						&& this.userDto.isPasswordExpired()) {
					
					return LoginAction.Mapping.PASSWORD;
				}
			}

			return LoginAction.Mapping.SUCCESS;
		} catch (Exception e) {
			super.errorLog(e);
		}
		
		return LoginAction.Mapping.INPUT;
	}
}
