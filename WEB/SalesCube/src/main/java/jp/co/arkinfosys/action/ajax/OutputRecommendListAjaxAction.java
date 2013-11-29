/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.ajax;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import jp.co.arkinfosys.common.Categories;
import jp.co.arkinfosys.dto.DetailDispItemDto;
import jp.co.arkinfosys.form.porder.OutputRecommendListForm;
import jp.co.arkinfosys.service.CategoryService;
import jp.co.arkinfosys.service.ProductService;
import jp.co.arkinfosys.service.SupplierService;
import jp.co.arkinfosys.service.exception.ServiceException;
import jp.co.arkinfosys.service.porder.OutputRecommendListService;

import org.seasar.framework.beans.util.BeanMap;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
/**
 * 補充発注推奨リストを出力するアクションクラスです.
 * @author Ark Information Systems
 *
 */
public class OutputRecommendListAjaxAction extends CommonAjaxResources {

	/**
	 * 画面遷移用のマッピングクラス
	 */
	public static class Mapping {
		public static final String RESULT = "searchResultList.jsp";
		public static final String EXCEL = "excel.jsp";
	}

	@ActionForm
	@Resource
	protected OutputRecommendListForm outputRecommendListForm;

	// Excel出力フラグ
	public boolean isOutputExcel = false;

	/**
	 * 検索結果列情報リスト
	 */
	public List<DetailDispItemDto> columnInfoList = new ArrayList<DetailDispItemDto>();

	/**
	 * 画面専用サービス
	 */
	@Resource
	private OutputRecommendListService outputRecommendListService;

	/**
	 * 区分情報
	 */
	@Resource
	private CategoryService categoryService;

	@Resource
	private SupplierService supplierService;

	/**
	 * 初期表示処理を行います.
	 * @return 遷移先URI
	 */
	@Execute(validator = false)
	public String index() {
		return Mapping.RESULT;
	}

	/**
	 * 検索を実行します(ソート無し画面表示用).<BR>
	 * バリデートはしません(仕入先リストはシステムで生成したものだから).
	 * @return 遷移先URI
	 */
	@Execute(validator = false)
	public String search() {
		isOutputExcel = false;

		//(ソートでない)通常検索の場合、以前の入力値が格納されている可能性があるので削除する
		outputRecommendListForm.searchResultInputProductCodeArray = null;
		outputRecommendListForm.searchResultInputOrderCheckStatusArray = null;
		outputRecommendListForm.searchResultInputOrderQuantityArray = null;

		return searchCommon(Mapping.RESULT, true);
	}

	/**
	 * 検索を実行します(ソート有り画面表示用).<BR>
	 * バリデートはしません(仕入先リストはシステムで生成したものだから).
	 * @return 遷移先URI
	 */
	@Execute(validator = false)
	public String sortSearch() {
		isOutputExcel = false;

		return searchCommon(Mapping.RESULT, true);
	}

	/**
	 * 検索を実行します(Excel出力用).<BR>
	 * バリデートはしません(仕入先リストはシステムで生成したものだから).
	 * @return 遷移先URI
	 */
	@Execute(validator = false)
	public String excel() {
		isOutputExcel = true;

		return searchCommon(Mapping.EXCEL, false);
	}

	/**
	 * 検索処理を実行します.<BR>
	 * 検索共通(EXCEL出力と通常検索、ソート検索での共通処理)
	 * @param destJSP 遷移先URI
	 * @param isStoreCount 検索件数を取得するか否か
	 * @return destJSP
	 */
	private String searchCommon(String destJSP, boolean isStoreCount) {
		try {
			// 条件をマップ化する
			BeanMap conditions = Beans
					.createAndCopy(BeanMap.class, this.outputRecommendListForm)
					.excludesNull()
					.excludesWhitespace()
					.lrTrim()
					.includes(
							ProductService.Param.SUPPLIER_CODE,
							OutputRecommendListService.Param.PO_CATEGORY,
							OutputRecommendListService.Param.IMMEDIATELY_PO_CATEGORY,
							ProductService.Param.EXCLUDES_HOLDING_STOCK_ZERO,
							ProductService.Param.EXCLUDES_AVG_SHIP_COUNT_ZERO,
							ProductService.Param.EXCLUDES_AVG_LESS_THAN_HOLDING_STOCK)
					.execute();

			columnInfoList = outputRecommendListService.getColumnInfoList(conditions);

			// 検索を行う
			outputRecommendListForm.searchResultList = outputRecommendListService
					.findRecommendByCondition(
							conditions,
							outputRecommendListForm.sortColumn,
							outputRecommendListForm.sortOrderAsc,
							outputRecommendListForm.searchResultInputProductCodeArray,
							outputRecommendListForm.searchResultInputOrderCheckStatusArray,
							outputRecommendListForm.searchResultInputOrderQuantityArray);

			// 検索結果件数を取得する
			if(isStoreCount) {
				outputRecommendListForm.searchResultCount = outputRecommendListForm.searchResultList.size();
			}

			// 仕入先、発注区分、都度発注区分をセットする
			outputRecommendListForm.supplierName = supplierService.findById(outputRecommendListForm.supplierCode).supplierName;
			outputRecommendListForm.poCategoryName = categoryService.findCategoryNameByIdAndCode( Categories.PO_CATEGORY, outputRecommendListForm.poCategory);
			outputRecommendListForm.immediatelyPOCategoryName = categoryService.findCategoryNameByIdAndCode( Categories.IMMEDIATELY_PO_CATEGORY, outputRecommendListForm.immediatelyPOCategory);

			outputRecommendListForm.saveSearchCondition();
		} catch (ServiceException e) {
			super.errorLog(e);

			// システム例外として処理する
			super.writeSystemErrorToResponse();
			return null;
		}
		return destJSP;
	}


}
