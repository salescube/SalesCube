/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.setting;


import javax.annotation.Resource;

import jp.co.arkinfosys.action.CommonResources;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.entity.News;
import jp.co.arkinfosys.form.setting.NewsForm;
import jp.co.arkinfosys.service.NewsService;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.util.ActionMessagesUtil;

/**
 * お知らせ編集画面のアクションクラスです.
 * @author Ark Information Systems
 *
 */
public class NewsAction extends CommonResources {

	/**
	 * 画面遷移用のマッピングクラスです.
	 * @author Ark Information Systems
	 *
	 */
	public static class Mapping {
		public static final String INPUT = "news.jsp";
	}

	@ActionForm
	@Resource
	public NewsForm newsForm;

	@Resource
	private NewsService newsService;

	/**
	 * 初期表示を行います.<br>
	 * 処理終了後、{@link Mapping#INPUT}で定義されたURIに遷移します.
	 * @return 画面遷移先のURI文字列
	 * @throws Exception
	 */
	@Execute(validator = false)
	public String index() throws Exception {
		init();
		return NewsAction.Mapping.INPUT;
	}

	/**
	 * 更新処理を行います.<br>
	 * 更新完了時および何かしらの問題があった場合に、画面にメッセージを表示します.<br>
	 * 処理実行後、{@link Mapping#INPUT}で定義されたURIに遷移します.
	 * @return 画面遷移先のURI文字列
	 * @throws Exception
	 */
	@Execute(validate = "validate", validator = true, input = NewsAction.Mapping.INPUT)
	public String update() throws Exception {
		// おしらせ情報を更新する
		// 排他制御
		News news = this.newsService.getNews();
		if (news != null) {
			String nowUpdatetm = "";
			if( news.updDatetm != null)	{
				nowUpdatetm = String.valueOf(news.updDatetm);
			}
			if( newsForm.updDatetm.equals(nowUpdatetm) == false){
				super.messages.add(ActionMessages.GLOBAL_MESSAGE,	new ActionMessage("errors.exclusive.control.updated"));
				ActionMessagesUtil.addErrors(super.httpRequest, super.messages);
				return NewsAction.Mapping.INPUT;
			}
			//updateする
			this.newsService.updateNews(newsForm.description);
		}else{
			//insertする
			this.newsService.insertNews(newsForm.description);
		}
		super.messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("infos.news.changed"));
		ActionMessagesUtil.addMessages(super.httpRequest, super.messages);

		init();
		return NewsAction.Mapping.INPUT;

	}

	/**
	 * リセット処理を行います.<br>
	 * 処理実行後、{@link Mapping#INPUT}で定義されたURIに遷移します.
	 * @return 画面遷移先のURI文字列
	 * @throws Exception
	 */
	@Execute(validator = false)
	public String reset() throws Exception {
		init();
		return NewsAction.Mapping.INPUT;
	}

	/**
	 * アクションフォームを初期化します.
	 * @throws Exception
	 */
	private void init() throws Exception {
		this.newsForm.isUpdate = this.userDto.isMenuUpdate( Constants.MENU_ID.SETTING_NEWS );

		// お知らせマスタの情報を取得する
		News news = this.newsService.getNews();
		newsForm.description = "";
		if (news != null) {
			if( news.description !=null) {
				newsForm.description = news.description;
			}
			if( news.updDatetm != null)	{
				newsForm.updDatetm = String.valueOf(news.updDatetm);
			}
		}
	}

}

