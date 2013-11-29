/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.taglib;

import jp.co.arkinfosys.common.StringUtil;

import org.seasar.struts.taglib.S2Functions;

/**
 * EL式で利用する静的メソッドを定義するクラスです.
 *
 * @author Ark Information Systems
 *
 */
public final class Functions {

	/**
	 * ページ繰りのリンクを出力します.
	 *
	 * @param resultRowSize 検索結果の全件数
	 * @param pageRowSize １ページの件数
	 * @param currentPage 現在のページ
	 * @return 出力するリンク文字列
	 */
	public static String pageLink(int resultRowSize, int pageRowSize,
			int currentPage) {

		// 1ページ件数が設定されていない場合はリンクを表示しない
		if (pageRowSize == 0) {
			return "";
		}

		int maxPage = (resultRowSize - 1) / pageRowSize + 1;

		// 1ページしかない場合は、リンクを表示しない
		if (maxPage == 1) {
			return "";
		}

		int startPage = 0;
		int endPage = 0;

		startPage = maxPage < 10 ? 1 : currentPage - 4 < 1 ? 1
				: currentPage - 4 < maxPage - 9 ? currentPage - 4 : maxPage - 9;
		endPage = maxPage < 10 ? maxPage : currentPage + 5 <= 10 ? 10
				: currentPage + 5 < maxPage ? currentPage + 5 : maxPage;

		StringBuilder sb = new StringBuilder();

		if (currentPage > 1) {
			sb.append("<span style=\"color: blue; text-decoration: underline; cursor: pointer;\" onclick=\"goPage("
							+ (currentPage - 1) + ")\">前へ</span>&nbsp;");
		}

		for (int i = startPage; i <= endPage; i++) {
			if (i == currentPage) {
				sb.append(i);
			} else {
				sb.append("<span style=\"color: blue; text-decoration: underline; cursor: pointer;\" onclick=\"goPage("
								+ i + ")\" >" + i + "</span>");
			}
			sb.append("&nbsp;");
		}

		if (currentPage < maxPage) {
			sb.append("<span style=\"color: blue; text-decoration: underline; cursor: pointer;\" onclick=\"goPage("
							+ (currentPage + 1) + ")\">次へ</span>");
		}

		return sb.toString();

	}

	/**
	 * URLエンコードの拡張版です（"/"を代替記号に置換します）.<BR>
	 * ※本メソッドでエンコードした文字列はStringUtil.decodeSL()で元に戻すこととします.
	 * @param val 文字列
	 * @return URLエンコードした文字列
	 */
	public static String u(String val){
		return S2Functions.u(StringUtil.encodeSL(val));
	}

}
