/*
 *  Copyright 2009-2010 Ark Information Systems.
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

		
		list.add(new LabelValueBean(Constants.SEARCH_TARGET.LABEL_SLIP,
				Constants.SEARCH_TARGET.VALUE_SLIP));
		
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
			
			Constants.OUTPUT_BALANCE_TARGET.LABEL_PORDER,
			
			Constants.OUTPUT_BALANCE_TARGET.LABEL_RORDER
		};

		String[] values = {
			
			Constants.OUTPUT_BALANCE_TARGET.VALUE_PORDER,
			
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
				
				Constants.REFERENCE_HISTORY_TARGET.LABEL_ESTIMATE,
				
				Constants.REFERENCE_HISTORY_TARGET.LABEL_RORDER,
				
				Constants.REFERENCE_HISTORY_TARGET.LABEL_SALES,
				
				Constants.REFERENCE_HISTORY_TARGET.LABEL_DEPOSIT,
				
				Constants.REFERENCE_HISTORY_TARGET.LABEL_PORDER,
				
				Constants.REFERENCE_HISTORY_TARGET.LABEL_PURCHASE,
				
				Constants.REFERENCE_HISTORY_TARGET.LABEL_PAYMENT,
				
				Constants.REFERENCE_HISTORY_TARGET.LABEL_STOCK,
				
				Constants.REFERENCE_HISTORY_TARGET.LABEL_CUSTOMER,
				
				Constants.REFERENCE_HISTORY_TARGET.LABEL_PRODUCT,
				
				Constants.REFERENCE_HISTORY_TARGET.LABEL_SUPPLIER,
				
				Constants.REFERENCE_HISTORY_TARGET.LABEL_USER
			};

			String[] values = {
					
					Constants.REFERENCE_HISTORY_TARGET.VALUE_ESTIMATE,
					
					Constants.REFERENCE_HISTORY_TARGET.VALUE_RORDER,
					
					Constants.REFERENCE_HISTORY_TARGET.VALUE_SALES,
					
					Constants.REFERENCE_HISTORY_TARGET.VALUE_DEPOSIT,
					
					Constants.REFERENCE_HISTORY_TARGET.VALUE_PORDER,
					
					Constants.REFERENCE_HISTORY_TARGET.VALUE_PURCHASE,
					
					Constants.REFERENCE_HISTORY_TARGET.VALUE_PAYMENT,
					
					Constants.REFERENCE_HISTORY_TARGET.VALUE_STOCK,
					
					Constants.REFERENCE_HISTORY_TARGET.VALUE_CUSTOMER,
					
					Constants.REFERENCE_HISTORY_TARGET.VALUE_PRODUCT,
					
					Constants.REFERENCE_HISTORY_TARGET.VALUE_SUPPLIER,
					
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
		
		if(list == null) {
			list = new ArrayList<LabelValueBean>();
		}

		
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
				
				Constants.REFERENCE_MST_TARGET.LABEL_CUSTOMER,
			};

			String[] values = {
					
					Constants.REFERENCE_MST_TARGET.VALUE_CUSTOMER,
			};

			for (int i=0;i<labels.length;i++) {
				list.add(new LabelValueBean(labels[i],values[i]));
			}

		return list;
	}
}
