/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.form.porder;

import java.io.Serializable;
import java.util.List;

import jp.co.arkinfosys.common.CategoryTrns;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.dto.porder.OutputRecommendListFormDto;
import jp.co.arkinfosys.dto.porder.OutputRecommendOrderResultLine;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;
import org.seasar.struts.annotation.Arg;
import org.seasar.struts.annotation.ByteType;
import org.seasar.struts.annotation.DateType;
import org.seasar.struts.annotation.Mask;
import org.seasar.struts.annotation.Msg;
import org.seasar.struts.annotation.Required;
import org.seasar.struts.util.MessageResourcesUtil;
/**
 * 補充発注推奨リスト出力画面のアクションフォームクラスです.
 * @author Ark Information Systems
 *
 */
@Component(instance = InstanceType.SESSION)
public class OutputRecommendListForm implements Serializable {

	private static final long serialVersionUID = 1L;

	public int ML_DATE;			//日付用
	public int ML_QUANTITY; 	//数量用

	/**
	 * 仕入先
	 */
	@Required
	@Mask(mask = Constants.CODE_MASK.SUPPLIER_MASK,
			msg = @Msg(key = "errors.invalid"),
			args = @Arg(key = "labels.supplierCode", resource = true, position = 0))
	public String supplierCode;
	public String supplierName;

	/**
	 * 発注区分
	 */
	@Required
	@ByteType
	public String poCategory;
	public String poCategoryName;

	/**
	 * 都度発注区分
	 */
	@Required
	@ByteType
	public String immediatelyPOCategory;
	public String immediatelyPOCategoryName;

	/**
	 * 保有数0の商品は除く
	 */
	public boolean excludeHoldingStockZero;

	/**
	 * 平均出荷数0の商品は除く
	 */
	public boolean excludeAvgShipCountZero;

	/**
	 * 保有数 > 平均出荷数 となる商品は除く
	 */
	public boolean excludeAvgLessThanHoldingStock;

	/**
	 * 納期
	 */
	@Required
    @DateType(datePatternStrict = "yyyy/MM/dd")
    public String deliveryDate;

	/**
	 * 運送便区分
	 */
	@Required
	@ByteType
	public String transportCategory;

	/**
	 * 補充発注推奨リスト
	 */
	public List<OutputRecommendListFormDto> searchResultList;

	/**
	 * 発注結果画面用リスト
	 */
	public List<OutputRecommendOrderResultLine> orderResultList;

	/**
	 * 発注入力画面が利用可能かどうか
	 */
	public boolean validInputPOrder;
	/**
	 * !補充発注推奨リスト画面が参照権限かどうか
	 */
	public boolean updateOutputRecommendList;

    /**
     * ソートカラム
     */
    public String sortColumn;

    /**
     * ソート昇順フラグ
     */
    public boolean sortOrderAsc;

    /**
     * 検索結果件数
     */
    public long searchResultCount;

    /**
     * 検索時仕入先コード
     */
    public String searchSupplierCode;

    /**
     * 検索時仕入先名
     */
    public String searchSupplierName;

    /**
     * 検索時発注区分
     */
	public String searchPOCategory;

    /**
     * 検索時発注区分名
     */
	public String searchPOCategoryName;

	/**
	 * 検索時都度発注区分
	 */
	public String searchImmediatelyPOCategory;

	/**
	 * 検索時都度発注区分名
	 */
	public String searchImmediatelyPOCategoryName;

	/**
	 * 検索時除外条件：保有数0の商品は除く
	 */
	public boolean searchExcludeHoldingStockZero;

	/**
	 * 検索時除外条件：平均出荷数0の商品は除く
	 */
	public boolean searchExcludeAvgShipCountZero;

	/**
	 * 検索時除外条件：保有数 > 平均出荷数 となる商品は除く
	 */
	public boolean searchExcludeAvgLessThanHoldingStock;

    /**
     * 検索時ソートカラム
     */
    public String searchSortColumn;

    /**
     * 検索時ソート昇順フラグ
     */
    public boolean searchSortOrderAsc;

	/**
	 * リストに入力したチェック状態を画面から取得する
	 */
	public String[] searchResultInputOrderCheckStatusArray;
	/**
	 * リストに表示されている商品コードを画面から取得する
	 */
	public String[] searchResultInputProductCodeArray;
	/**
	 * リストに入力した発注数を画面から取得するための配列
	 */
	public String[] searchResultInputOrderQuantityArray;

	/**
	 * 初期化処理を行います.
	 */
	public void reset(){

		ML_DATE = 10;			//日付用
		ML_QUANTITY = 6; 		//数量用

		supplierCode = null;
		deliveryDate = null;

		searchResultList = null;
		orderResultList = null;
		validInputPOrder = false;
		updateOutputRecommendList = false;

		//初期選択固定値：AIR便:仕様書より
		transportCategory = CategoryTrns.TRANSPORT_CATEGORY_AIR;

		// 初期選択固定値：発注区分、都度発注区分
		poCategory = CategoryTrns.IMMEDIATELY_PORDER;
		immediatelyPOCategory = CategoryTrns.NORMAL_PORDER;

		excludeHoldingStockZero = true;
		excludeAvgShipCountZero = true;
		excludeAvgLessThanHoldingStock = true;

		// ソートカラムの初期値を設定
		sortColumn = null;
		sortOrderAsc = true;

		//検索結果件数を初期化
		searchResultCount = 0;

    	this.searchSupplierCode = null;
    	this.searchSupplierName = null;
    	this.searchPOCategory = null;
    	this.searchPOCategoryName = null;
    	this.searchImmediatelyPOCategory = null;
    	this.searchImmediatelyPOCategoryName = null;
    	this.searchExcludeHoldingStockZero = false;
    	this.searchExcludeAvgShipCountZero = false;
    	this.searchExcludeAvgLessThanHoldingStock = false;
    	this.searchSortColumn = "";
    	this.searchSortOrderAsc = true;
	}

	/**
	 * バリデートを行います.
	 *
	 * @return 表示するメッセージ
	 */
    public ActionErrors validate() {
		ActionErrors errors = new ActionErrors();

		//商品未選択あるいは有効発注数が0
		int l_validLineCount = 0;
		int lineCount = 0;

		try {
			for(OutputRecommendListFormDto l_RowData : searchResultList){
				lineCount++;

				//チェックが付いている行のみをバリデート対象とする
				if(Null2Bool(l_RowData.validRow)){
					String l_productCode = l_RowData.productCode;
					String l_pOrderQuantity = l_RowData.pOrderQuantity;

					// 発注数の正数チェック
					try{
						if(Integer.valueOf(l_pOrderQuantity).intValue() <= 0) {
							errors.add(ActionMessages.GLOBAL_MESSAGE,
									new ActionMessage("errors.line.integer.plus",
											lineCount, MessageResourcesUtil.getMessage("labels.outputRecommendList.pOrderQuantity")));
						}
					} catch(Exception e){
						errors.add(ActionMessages.GLOBAL_MESSAGE,
								new ActionMessage("errors.line.integer.plus",
										lineCount, MessageResourcesUtil.getMessage("labels.outputRecommendList.pOrderQuantity")));
					}

					if( (l_productCode != null && l_pOrderQuantity != null)&&
							(!l_productCode.isEmpty()) &&
							(!l_pOrderQuantity.isEmpty()) ){
						if ( Integer.valueOf(l_pOrderQuantity).intValue() > 0 ){
							l_validLineCount += Integer.valueOf(l_pOrderQuantity).intValue();
						}
					}
				}
			}
		} catch (Exception e) {
			l_validLineCount = 0;
		}

		if(l_validLineCount == 0){
			errors.add(ActionMessages.GLOBAL_MESSAGE,
					new ActionMessage("errors.reason.notExistPOrderList"));
		}

		return errors;
	}

    /**
     * Booleanオブジェクトをbooleanに変換します.
     * nullならばfalseを返します.
     * @param l_Bool　変換するオブジェクト
     * @return　変換したboolean値
     */
    private boolean Null2Bool(Boolean l_Bool) {
    	return (l_Bool!=null?l_Bool.booleanValue():false);
 	}

    /**
     * 入力された検索条件を保存用変数に設定します.
     */
    public void saveSearchCondition() {
    	this.searchSupplierCode = this.supplierCode;
    	this.searchSupplierName = this.supplierName;
    	this.searchPOCategory = this.poCategory;
    	this.searchPOCategoryName = this.poCategoryName;
    	this.searchImmediatelyPOCategory = this.immediatelyPOCategory;
    	this.searchImmediatelyPOCategoryName = this.immediatelyPOCategoryName;
    	this.searchExcludeHoldingStockZero = this.excludeHoldingStockZero;
    	this.searchExcludeAvgShipCountZero = this.excludeAvgShipCountZero;
    	this.searchExcludeAvgLessThanHoldingStock = this.excludeAvgLessThanHoldingStock;
    	this.searchSortColumn = this.sortColumn;
    	this.searchSortOrderAsc = this.sortOrderAsc;
    }
}
