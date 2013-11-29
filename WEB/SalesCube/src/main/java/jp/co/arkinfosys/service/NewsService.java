/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.service;

import java.security.GeneralSecurityException;
import java.sql.SQLException;
import java.util.Map;

import jp.co.arkinfosys.entity.News;

/**
 * お知らせサービスクラスです.
 * @author Ark Information Systems
 *
 */
public class NewsService extends AbstractService<News> {

	/**
	 * パラメータマッピングクラスです.
	 * @author Ark Information Systems
	 *
	 */
    public static class Param {
	public static final String DESCRIPTION = "description";
    }

    /**
     * お知らせ情報を返します.
     * @return お知らせ情報
     * @throws SQLException
     * @throws GeneralSecurityException
     */
    public News getNews() throws SQLException, GeneralSecurityException {
    	return this.selectBySqlFile(News.class, "news/GetNews.sql", super.createSqlParam())
		.getSingleResult();
    }

    /**
     * お知らせ情報を登録します.
     * @param description お知らせ内容
     * @return 登録した件数
     * @throws SQLException
     * @throws GeneralSecurityException
     */
    public int insertNews(String description) throws SQLException,
		GeneralSecurityException {
		// SQLパラメータを構築する
		Map<String, Object> param = super.createSqlParam();
		param.put(NewsService.Param.DESCRIPTION, description);
		return this.updateBySqlFile("news/InsertNews.sql", param).execute();
	}

    /**
     * お知らせ情報を更新します.
     * @param description お知らせ内容
     * @return 更新した件数
     * @throws SQLException
     * @throws GeneralSecurityException
     */
    public int updateNews(String description) throws SQLException,
		GeneralSecurityException {
		// SQLパラメータを構築する
		Map<String, Object> param = super.createSqlParam();
		param.put(NewsService.Param.DESCRIPTION, description);
		return this.updateBySqlFile("news/UpdateNews.sql", param).execute();
	}
}
