/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.ajax.dialog;

import java.util.ArrayList;
import java.util.List;

import jp.co.arkinfosys.action.ajax.CommonAjaxResources;
import jp.co.arkinfosys.common.ConfigUtil;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.form.AbstractSearchForm;
import jp.co.arkinfosys.service.MasterSearch;
import jp.co.arkinfosys.service.exception.ServiceException;
import net.arnx.jsonic.JSON;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.seasar.framework.beans.util.BeanMap;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.util.ActionMessagesUtil;
import org.seasar.struts.util.ResponseUtil;

/**
 * 検索ダイアログの表示と、ダイアログでの検索処理を行うアクションクラスの基底クラスです.
 *
 * @author Ark Information Systems
 *
 * @param <DTOCLASS> 検索結果DTOクラス
 * @param <ENTITY> 検索対象のエンティティクラス
 */
public abstract class AbstractSearchDialogAction<DTOCLASS, ENTITY> extends
		AbstractDialogAction {

	/**
	 * 画面遷移用のマッピングを定義するクラスです.
	 */
	protected static class Mapping extends AbstractDialogAction.Mapping {
		/**
		 * 検索結果のJSPパスです.
		 */
		public static final String RESULT = "result.jsp";
	}

	/**
	 * コンフィグファイル appconfig.dicon における検索結果の最大表示件数のキー値です.
	 */
	protected static final String MAX_THRESHOLD_KEY = "SearchResultThreshold";

	/**
	 * メッセージリソースにおける検索結果件数オーバーメッセージのキー値です
	 */
	protected static final String SEARCH_THRESHOLD_OVER = "warns.search.thresholdover";

	/**
	 * 検索処理を実行するメソッドです.
	 *
	 * @return 検索結果JSPのパス文字列
	 * @throws Exception 予期しない例外が発生した場合
	 */
	@Execute(validator = true, validate = "validate", urlPattern = "search/{dialogId}", input = Mapping.ERROR_JSP)
	public String search() throws Exception {
		try {
			this.doBeforeSearch();

			// 検索結果の最大表示件数を取得する
			int threshold = this.getMaxThreshold();

			AbstractSearchForm<DTOCLASS> actionForm = this.getActionForm();

			BeanMap params = Beans.createAndCopy(BeanMap.class, actionForm)
					.excludesWhitespace().lrTrim().execute();

			// 検索を行う
			List<ENTITY> entityList = this.doSearch(params,
					actionForm.sortColumn, actionForm.sortOrderAsc);

			// 検索結果件数を設定する
			actionForm.searchResultCount = entityList.size();

			// 検索結果を最大表示件数に合わせてカット
			if (entityList.size() > threshold) {
				super.messages.add("resultThreshold", new ActionMessage(
						SEARCH_THRESHOLD_OVER, entityList.size(), threshold));
				ActionMessagesUtil.saveMessages(super.httpRequest,
						super.messages);

				// 表示件数を絞る
				List<ENTITY> tempList = new ArrayList<ENTITY>();
				tempList.addAll(entityList.subList(0, threshold));
				entityList = tempList;
			}

			actionForm.searchResultList = new ArrayList<DTOCLASS>();
			actionForm.searchResultList = this.exchange(entityList);

			this.doAfterSearch();
		} catch (ServiceException e) {
			super.errorLog(e);

			this.doAfterError();

			return null;
		}

		return Mapping.RESULT;
	}

	/**
	 * 検索実行メソッドのデフォルト実装です.{@link MasterSearch}を実装するサービスクラスの{@code MasterSearch#findByCondition(java.util.Map, String, boolean)}メソッドを呼び出してその結果を返します.
	 *
	 * @param params 検索条件を含むマップクラス
	 * @param sortColumn ソートカラム名
	 * @param sortOrderAsc 昇順か否か
	 * @return 条件に一致するエンティティのリスト
	 * @throws ServiceException 検索でエラーが発生した場合
	 */
	protected List<ENTITY> doSearch(BeanMap params, String sortColumn,
			boolean sortOrderAsc) throws ServiceException {
		return this.getService().findByCondition(params, sortColumn,
				sortOrderAsc);
	}

	/**
	 * 検索結果のエンティティリストをDTOクラスのリストに変換するメソッドのデフォルト実装です.<br>
	 * Genericsによって指定されるエンティティからDTOへ、{@code Beans#createAndCopy(Class, Object)}による値のコピーを行います.
	 *
	 * @param entityList 検索結果のエンティティリスト
	 * @return 検索結果のDTOリスト
	 * @throws Exception 値コピーでの例外発生時
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
	 * 検索実行後に行う処理のデフォルト実装です.通常は何も行いません.
	 *
	 * @throws Exception 例外発生時
	 */
	protected void doBeforeSearch() throws Exception {
	}

	/**
	 * 検索実行後に行う処理のデフォルト実装です.通常は何も行いません.
	 *
	 * @throws Exception 例外発生時
	 */
	protected void doAfterSearch() throws Exception {
	}

	/**
	 * 検索エラー時に行う処理のデフォルト実装です.{@code CommonAjaxResources#writeSystemErrorToResponse()}を呼び出してレスポンスにエラー出力を行います.
	 *
	 * @throws Exception 例外発生時
	 */
	protected void doAfterError() throws Exception {
		super.writeSystemErrorToResponse();
	}

	/**
	 * DTOのクラスを取得する抽象メソッドです.
	 *
	 * @return クラスオブジェクト
	 */
	protected abstract Class<DTOCLASS> getDtoClass();

	/**
	 * アクションフォームを取得する抽象メソッドです.
	 *
	 * @return アクションフォーム
	 */
	protected abstract AbstractSearchForm<DTOCLASS> getActionForm();

	/**
	 * 検索処理を行うサービスクラスを取得する抽象メソッドです.
	 *
	 * @return サービスクラス
	 */
	protected abstract MasterSearch<ENTITY> getService();

	/**
	 * 検索結果の最大表示件数を返すメソッドのデフォルト実装です.<br>
	 * コンフィグファイル appconfig.dicon から取得する値を返却します.数値が得られない場合は100を返します.
	 *
	 * @return 検索結果の最大表示件数
	 * @throws Exception 例外発生時
	 */
	protected int getMaxThreshold() throws Exception {
		int defaultVal = 100;
		try {
			Integer thresholdObj = (Integer) ConfigUtil
					.getConfigValue(MAX_THRESHOLD_KEY);
			if (thresholdObj != null) {
				return thresholdObj.intValue();
			}
		} catch (Exception e) {
			super.errorLog(e);
		}
		return defaultVal;
	}

	/**
	 * 検索結果から選択されたレコードの詳細情報を返すメソッドです.詳細情報はJSON形式の文字列としてレスポンスに出力されます.
	 *
	 * @return null
	 * @throws Exception 例外発生時
	 */
	@Execute(validator = false)
	public String select() throws Exception {
		try {
			ENTITY entity = this.getService().findById(this.getId());
			if (entity == null) {
				super.messages.add(ActionMessages.GLOBAL_MESSAGE,
						new ActionMessage(this.getMissingRecordMessageKey()));
				ActionMessagesUtil.addErrors(super.httpRequest, super.messages);
				return CommonAjaxResources.Mapping.ERROR_JSP;
			}

			BeanMap values = super.createBeanMapWithNullToEmpty(entity);
			ResponseUtil.write(JSON.encode(values), "text/javascript");
		} catch (ServiceException e) {
			super.errorLog(e);

			// システム例外として処理する
			super.writeSystemErrorToResponse();
			return null;
		}
		return null;
	}

	/**
	 * レコードを一意に検索するキー値を取得する抽象メソッドです.
	 *
	 * @return 検索キー値
	 */
	protected abstract String getId();

	/**
	 * レコードが見つからなかった場合に表示するメッセージリソースを表すキー値を返す抽象メソッドです.
	 *
	 * @return メッセージリソースのキー値
	 */
	protected abstract String getMissingRecordMessageKey();
}
