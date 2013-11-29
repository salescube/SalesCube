/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.dto.DetailDispItemDto;
import jp.co.arkinfosys.form.AbstractSearchForm;
import jp.co.arkinfosys.service.DetailDispItemService;
import jp.co.arkinfosys.service.MasterSearch;
import jp.co.arkinfosys.service.exception.ServiceException;

import org.seasar.framework.beans.util.BeanMap;
import org.seasar.framework.beans.util.Beans;

/**
 * 検索実行の基底アクションクラスです.
 * @author Ark Information Systems
 *
 * @param <DTOCLASS>
 * @param <ENTITY>
 */
public abstract class AbstractSearchResultAction<DTOCLASS, ENTITY> extends
		CommonResources {

	/**
	 * 画面遷移用のマッピングクラスです.
	 * @author Ark Information Systems
	 *
	 */
	public static class Mapping {
		/**
		 * 検索結果JSPへのパスです
		 */
		public static final String RESULT = "result.jsp";

		/**
		 * 検索結果EXCEL形式JSPへのパスです
		 */
		public static final String EXCEL = "excel.jsp";
	}

	// Excel出力フラグ
	public boolean outputExcel = false;

	@Resource
	protected DetailDispItemService detailDispItemService;

	/**
	 * 検索結果列情報リスト
	 */
	public List<DetailDispItemDto> columnInfoList;

	/**
	 * 検索を実行します.<br>
	 * <p>
	 * 以下の処理を実行します.<br>
	 * 1. 前処理<br>
	 * 2. 検索、入力画面の権限をアクションフォームに設定<br>
	 * 3. 検索結果表示項目を取得<br>
	 * 4. ページ繰り情報を取得<br>
	 * 5. 検索条件を取得<br>
	 * 6. 検索結果件数、検索結果を取得し、アクションフォームに設定<br>
	 * 7. 後処理
	 * </p>
	 * 正常終了時は{@link AbstractSearchResultAction#getResultURIString()}、失敗時は{@link AbstractSearchResultAction#getErrorURIString()}で取得したURIに遷移します.
	 * @return 画面遷移先のURI文字列
	 * @throws Exception
	 */
	protected String doSearch() throws Exception {
		try {
			this.doBeforeSearch();

			// アクションフォームを取得
			AbstractSearchForm<DTOCLASS> actionForm = this.getActionForm();

			// 検索画面の権限を取得する
			actionForm.isUpdate = super.userDto.isMenuUpdate(this
					.getSearchMenuID());

			// 入力画面の表示権限を取得する
			actionForm.isInputValid = super.userDto.isMenuValid(this
					.getInputMenuID());

			// 検索結果表示項目の取得
			this.columnInfoList = detailDispItemService.createResult(null,
					null, this.getSearchMenuID(), actionForm.searchTarget);

			// ページ繰り情報を取得
			int pageNo = actionForm.pageNo;
			int rowCount = actionForm.rowCount;
			int offset = rowCount * (pageNo - 1);
			String sortColumn = actionForm.sortColumn;
			boolean sortOrderAsc = actionForm.sortOrderAsc;

			// 検索条件を取得
			BeanMap params = Beans.createAndCopy(BeanMap.class, actionForm)
					.excludesWhitespace().lrTrim().execute();

			// 検索結果件数を取得する
			actionForm.searchResultCount = this.doCount(params);

			// 検索する
			List<ENTITY> resultList = this.execSearch(params, sortColumn,
					sortOrderAsc, rowCount, offset);

			actionForm.searchResultList = new ArrayList<DTOCLASS>();
			actionForm.searchResultList = this.exchange(resultList);

			this.doAfterSearch();
		} catch (Exception e) {
			super.errorLog(e);

			this.doAfterError(e);

			return this.getErrorURIString();
		}

		return this.getResultURIString();
	}

	/**
	 * 検索結果件数を返します.<br>
	 * デフォルト実装では{@link AbstractSearchResultAction#getService()}で規定の検索サービスが返される場合、{@link MasterSearch#countByCondition(java.util.Map)}の結果を返します.<br>
	 * 必要に応じてサブクラスでオーバーライドしてください.
	 * @param params パラメータを設定したマップ
	 * @return 検索結果件数
	 * @throws ServiceException
	 */
	protected int doCount(BeanMap params) throws ServiceException {
		return this.getService().countByCondition(params);
	}

	/**
	 * 検索を実行します.<br>
	 * デフォルト実装では{@link AbstractSearchResultAction#getService()}で規定の検索サービスが返される場合、{@link MasterSearch#findByConditionLimit(java.util.Map, String, boolean, int, int)}の結果を返します.<br>
	 * 必要に応じてサブクラスでオーバーライドしてください.
	 * @param params パラメータを設定したマップ
	 * @param sortColumn ソート対象カラム名
	 * @param sortOrderAsc 昇順にソートするか否か
	 * @param rowCount 取得する検索件数
	 * @param offset 取得開始位置
	 * @return 検索結果のリスト
	 * @throws ServiceException
	 */
	protected List<ENTITY> execSearch(BeanMap params, String sortColumn,
			boolean sortOrderAsc, int rowCount, int offset)
			throws ServiceException {
		return this.getService().findByConditionLimit(params, sortColumn,
				sortOrderAsc, rowCount, offset);
	}

	/**
	 * ENTITYのリストをDTOのリストに変換します.<br>
	 * デフォルト実装ではENTITYの型からDTOCLASSの型に変換したリストを返します.<br>
	 * サブクラスで使用する型でオーバーライドしてください.
	 * @param entityList ENTITYのリスト
	 * @return DTOCLASSのリスト
	 * @throws Exception
	 */
	protected List<DTOCLASS> exchange(List<ENTITY> entityList) throws Exception {
		List<DTOCLASS> dtoList = new ArrayList<DTOCLASS>();
		for (ENTITY entity : entityList) {
			DTOCLASS dto = Beans.createAndCopy(this.getDtoClass(), entity)
					.timestampConverter(Constants.FORMAT.TIMESTAMP)
					.dateConverter(Constants.FORMAT.DATE).execute();
			dtoList.add(dto);
		}
		return dtoList;
	}

	/**
	 * {@link AbstractSearchResultAction#doSearch()}検索前に必要な処理を行います.<br>
	 * デフォルト実装では何も処理しません.<br>
	 * 必要に応じてサブクラスでオーバーライドしてください.
	 * @throws Exception
	 */
	protected void doBeforeSearch() throws Exception {
	}

	/**
	 * {@link AbstractSearchResultAction#doSearch()}検索後に必要な処理を行います.<br>
	 * デフォルト実装では何もしません.<br>
	 * 必要に応じてサブクラスでオーバーライドしてください.
	 * @throws Exception
	 */
	protected void doAfterSearch() throws Exception {
	}

	/**
	 * Exception発生後の処理を行います.<br>
	 * デフォルト実装では何も処理しません.<br>
	 * 必要に応じてオーバーライドしてください.
	 * @param e 発生したException
	 * @throws Exception
	 */
	protected void doAfterError(Exception e) throws Exception {
	}

	/**
	 * 検索エラー時の画面遷移先URIを返します.<br>
	 * デフォルト実装ではnullを返します.<br>
	 * 必要に応じてサブクラスでオーバーライドしてください.
	 * @return 画面遷移先のURI文字列
	 */
	protected String getErrorURIString() {
		return null;
	}

	/**
	 * 検索正常終了時の画面遷移先URIを返します.
	 * @return 画面遷移先のURI文字列
	 */
	protected abstract String getResultURIString();

	/**
	 * DTOクラスを返します.
	 * @return DTOクラス
	 */
	protected abstract Class<DTOCLASS> getDtoClass();

	/**
	 * アクションフォームを返します.
	 * @return アクションフォーム
	 */
	protected abstract AbstractSearchForm<DTOCLASS> getActionForm();

	/**
	 * 検索サービスを返します.
	 * @return 検索サービス
	 */
	protected abstract MasterSearch<ENTITY> getService();

	/**
	 * 入力画面のメニューIDを返します.<br>
	 * デフォルト実装ではnullを返します.<br>
	 * 必要に応じてサブクラスでオーバーライドしてください.
	 * @return 入力画面メニューID
	 */
	protected String getInputMenuID() {
		return null;
	}

	/**
	 * 検索画面のメニューIDを返します.<br>
	 * デフォルト実装ではnullを返します.<br>
	 * 必要に応じてサブクラスでオーバーライドしてください.
	 * @return 検索画面メニューID
	 */
	protected abstract String getSearchMenuID();

}
