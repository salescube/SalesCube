/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.common;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts.util.LabelValueBean;

/**
 * プルダウンリストのユーティリティクラスです.
 *
 * @author Ark Information Systems
 *
 */
public final class ListUtil {

	/**
	 * 検索対象の選択値リストを返します.
	 * @return 検索対象の選択値リスト
	 */
	public static List<LabelValueBean> getSearchTargetList() {
		List<LabelValueBean> list = new ArrayList<LabelValueBean>();

		// 伝票
		list.add(new LabelValueBean(Constants.SEARCH_TARGET.LABEL_SLIP,
				Constants.SEARCH_TARGET.VALUE_SLIP));
		// 明細
		list.add(new LabelValueBean(Constants.SEARCH_TARGET.LABEL_LINE,
				Constants.SEARCH_TARGET.VALUE_LINE));

		return list;
	}

	/**
	 * 残高一覧出力対象の選択値リストを返します.
	 * @return 残高一覧出力対象の選択値リスト
	 */
	public static List<LabelValueBean> getOutputBalanceTargetList() {
		List<LabelValueBean> list = new ArrayList<LabelValueBean>();

		String[] labels = {
			// LABEL:買掛残高一覧
			Constants.OUTPUT_BALANCE_TARGET.LABEL_PORDER,
			// LABEL:売掛残高一覧
			Constants.OUTPUT_BALANCE_TARGET.LABEL_RORDER
		};

		String[] values = {
			// VALUE:買掛残高一覧
			Constants.OUTPUT_BALANCE_TARGET.VALUE_PORDER,
			// VALUE:売掛残高一覧
			Constants.OUTPUT_BALANCE_TARGET.VALUE_RORDER
		};

		for (int i=0;i<labels.length;i++) {
			list.add(new LabelValueBean(labels[i],values[i]));
		}

		return list;
	}

	/**
	 * 履歴参照出力対象の選択値リストを返します.
	 * @return 履歴参照出力対象の選択値リスト
	 */
	public static List<LabelValueBean> getReferenceHistoryTargetList() {
		List<LabelValueBean> list = new ArrayList<LabelValueBean>();

		String[] labels = {
				// LABEL:見積
				Constants.REFERENCE_HISTORY_TARGET.LABEL_ESTIMATE,
				// LABEL:受注
				Constants.REFERENCE_HISTORY_TARGET.LABEL_RORDER,
				// LABEL:売上
				Constants.REFERENCE_HISTORY_TARGET.LABEL_SALES,
				// LABEL:入金
				Constants.REFERENCE_HISTORY_TARGET.LABEL_DEPOSIT,
				// LABEL:発注
				Constants.REFERENCE_HISTORY_TARGET.LABEL_PORDER,
				// LABEL:仕入
				Constants.REFERENCE_HISTORY_TARGET.LABEL_PURCHASE,
				// LABEL:支払
				Constants.REFERENCE_HISTORY_TARGET.LABEL_PAYMENT,
				// LABEL:入出庫
				Constants.REFERENCE_HISTORY_TARGET.LABEL_STOCK,
				// LABEL:顧客
				Constants.REFERENCE_HISTORY_TARGET.LABEL_CUSTOMER,
				// LABEL:商品
				Constants.REFERENCE_HISTORY_TARGET.LABEL_PRODUCT,
				// LABEL:仕入先
				Constants.REFERENCE_HISTORY_TARGET.LABEL_SUPPLIER,
				// LABEL:社員
				Constants.REFERENCE_HISTORY_TARGET.LABEL_USER
			};

			String[] values = {
					// VALUE:見積
					Constants.REFERENCE_HISTORY_TARGET.VALUE_ESTIMATE,
					// VALUE:受注
					Constants.REFERENCE_HISTORY_TARGET.VALUE_RORDER,
					// VALUE:売上
					Constants.REFERENCE_HISTORY_TARGET.VALUE_SALES,
					// VALUE:入金
					Constants.REFERENCE_HISTORY_TARGET.VALUE_DEPOSIT,
					// VALUE:発注
					Constants.REFERENCE_HISTORY_TARGET.VALUE_PORDER,
					// VALUE:仕入
					Constants.REFERENCE_HISTORY_TARGET.VALUE_PURCHASE,
					// VALUE:支払
					Constants.REFERENCE_HISTORY_TARGET.VALUE_PAYMENT,
					// VALUE:入出庫
					Constants.REFERENCE_HISTORY_TARGET.VALUE_STOCK,
					// VALUE:顧客
					Constants.REFERENCE_HISTORY_TARGET.VALUE_CUSTOMER,
					// VALUE:商品
					Constants.REFERENCE_HISTORY_TARGET.VALUE_PRODUCT,
					// VALUE:仕入先
					Constants.REFERENCE_HISTORY_TARGET.VALUE_SUPPLIER,
					// VALUE:社員
					Constants.REFERENCE_HISTORY_TARGET.VALUE_USER
			};

			for (int i=0;i<labels.length;i++) {
				list.add(new LabelValueBean(labels[i],values[i]));
			}

		return list;
	}

	/**
	 * 引数のリストの先頭に空の選択値を追加して返します.
	 * @param list　リスト
	 * @return 引数のリストの先頭に空の選択値を追加した選択値リスト
	 */
	public static List<LabelValueBean> addEmptyLabelValue(List<LabelValueBean> list) {
		// NULLの場合、オブジェクトを作成
		if(list == null) {
			list = new ArrayList<LabelValueBean>();
		}

		// 空白値を先頭に追加
		list.add(0, new LabelValueBean("", ""));

		return list;
	}


	/**
	 * マスタリスト出力対象の選択値リストを返します.
	 * @return マスタリスト出力対象の選択値リスト
	 */
	public static List<LabelValueBean> getReferenceMstTargetList() {
		List<LabelValueBean> list = new ArrayList<LabelValueBean>();

		String[] labels = {
				// LABEL:顧客
				Constants.REFERENCE_MST_TARGET.LABEL_CUSTOMER,
			};

			String[] values = {
					// VALUE:顧客
					Constants.REFERENCE_MST_TARGET.VALUE_CUSTOMER,
			};

			for (int i=0;i<labels.length;i++) {
				list.add(new LabelValueBean(labels[i],values[i]));
			}

		return list;
	}
}
