/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action;

import javax.annotation.Resource;

import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.dto.setting.NewsDto;
import jp.co.arkinfosys.entity.News;
import jp.co.arkinfosys.service.NewsService;

import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.Execute;

/**
 * メニュー画面アクションクラスです.
 * @author Ark Information Systems
 *
 */
public class MenuAction extends CommonResources {

	@Resource
	protected NewsService newsService;

	public NewsDto newsDto;

	/**
	 * 初期表示処理を行います.<br>
	 * 処理終了後、"menu.jsp"に遷移します.
	 * @return 画面遷移先のURI文字列
	 * @throws Exception
	 */
	@Execute(validator = false)
	public String index() throws Exception {
		News news = this.newsService.getNews();
		if (news != null) {
			this.newsDto = Beans.createAndCopy(NewsDto.class, news)
					.dateConverter(Constants.FORMAT.TIMESTAMP_NOSEC).execute();
		} else {
			this.newsDto = new NewsDto();
		}

		return "menu.jsp";
	}

}
