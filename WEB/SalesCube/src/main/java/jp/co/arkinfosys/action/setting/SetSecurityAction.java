/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.setting;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import javax.annotation.Resource;

import jp.co.arkinfosys.action.CommonResources;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.dto.setting.MineDto;
import jp.co.arkinfosys.entity.Mine;
import jp.co.arkinfosys.form.setting.SetSecurityForm;
import jp.co.arkinfosys.service.MineService;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.util.ActionMessagesUtil;

/**
 * セキュリティ設定画面のアクションクラスです.
 * @author Ark Information Systems
 *
 */
public class SetSecurityAction extends CommonResources {
	/**
	 * 画面遷移用のマッピングクラスです.
	 * @author Ark Information Systems
	 *
	 */
	public static class Mapping {
		public static final String INPUT = "setSecurity.jsp";
	}

	@ActionForm
	@Resource
	private SetSecurityForm setSecurityForm;

	@Resource
	private MineService mineService;

	/**
	 * 初期表示を行います.
	 * 処理終了後、{@link Mapping#INPUT}で定義されたURIに遷移します.
	 * @return 画面遷移先のURI文字列
	 * @throws Exception
	 */
	@Execute(validator = false)
	public String index() throws Exception {

		init();

		return SetSecurityAction.Mapping.INPUT;
	}




	/**
	 * 更新処理を行います.<br>
	 * 更新完了時および何かしらの問題があった場合に、画面にメッセージを表示します.<br>
	 * 処理終了後、{@link Mapping#INPUT}で定義されたURIに遷移します.
	 * @return 画面遷移先のURI文字列
	 * @throws Exception
	 */
	@Execute(validate = "validate", validator = true, input = SetSecurityAction.Mapping.INPUT)
	public String update() throws Exception {

		// 自社情報を更新する
		// 排他制御
		Mine mine = this.mineService.getMine();
		if (mine != null) {

			
			Timestamp tm  = new Timestamp(new SimpleDateFormat(
						Constants.FORMAT.TIMESTAMP2).parse(setSecurityForm.updDatetm)
						.getTime());
			

			// 更新時間判定
			if (tm.compareTo(mine.updDatetm) != 0) {
				super.messages.add(ActionMessages.GLOBAL_MESSAGE,	new ActionMessage("errors.exclusive.control.updated"));
				ActionMessagesUtil.addErrors(super.httpRequest, super.messages);
				return SetSecurityAction.Mapping.INPUT;
			}
		}

		MineDto dto = Beans.createAndCopy(MineDto.class, this.setSecurityForm).excludes(MineService.Param.UPD_DATETM).execute();
		
		this.mineService.updateMineSecurity(dto);

		super.messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
				"infos.update"));
		ActionMessagesUtil.addMessages(super.httpRequest, super.messages);

		init();

		return SetSecurityAction.Mapping.INPUT;

	}

	/**
	 * リセット処理を行います.<br>
	 * 処理終了後、{@link Mapping#INPUT}で定義されたURIに遷移します.
	 * @return 画面遷移先のURI文字列
	 * @throws Exception
	 */
	@Execute(validator = false)
	public String reset() throws Exception {
		init();

		return SetSecurityAction.Mapping.INPUT;
	}

	/**
	 * 初期化処理を行います.
	 * @throws Exception
	 */
	private void init() throws Exception {
		this.setSecurityForm.isUpdate = this.userDto.isMenuUpdate( Constants.MENU_ID.SETTING_COMPANY );

		// 自社マスタの情報を取得する
		Mine mine = this.mineService.getMine();
		Beans.copy(mine, this.setSecurityForm).execute();

	}
}
