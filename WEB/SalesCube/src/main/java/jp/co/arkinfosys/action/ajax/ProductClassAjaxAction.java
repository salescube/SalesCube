/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.ajax;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import jp.co.arkinfosys.action.ajax.AbstractSearchResultAjaxAction;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.common.StringUtil;
import jp.co.arkinfosys.dto.master.ProductClassDto;
import jp.co.arkinfosys.entity.ProductClass;
import jp.co.arkinfosys.entity.join.ProductClassJoin;
import jp.co.arkinfosys.form.AbstractSearchForm;
import jp.co.arkinfosys.form.master.SearchProductClassForm;
import jp.co.arkinfosys.service.MasterSearch;
import jp.co.arkinfosys.service.ProductClassService;
import jp.co.arkinfosys.service.exception.ServiceException;
import net.arnx.jsonic.JSON;

import org.seasar.framework.beans.util.BeanMap;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.util.ResponseUtil;

/**
 * 商品分類マスタ情報を取得するアクションクラスです.
 *
 *
 * @author Ark Information Systems
 *
 */
public class ProductClassAjaxAction extends
		AbstractSearchResultAjaxAction<ProductClassDto, ProductClassJoin> {

	@ActionForm
	@Resource
	public SearchProductClassForm searchProductClassForm;

	@Resource
	public ProductClassService productClassService;

	/**
	 * 検索結果件数を返します.<BR>
	 * 常に0を返します.
	 *
	 * @return 0
	 */
	@Override
	protected int doCount(BeanMap params) throws ServiceException {
		return 0;
	}

	/**
	 * ENTITYのリストをDTOのリストに変換します.<BR>
	 * 未実装です.
	 * @param entityList {@link ProductClassJoin}のリスト
	 * @return null
	 */
	@Override
	protected List<ProductClassDto> exchange(List<ProductClassJoin> entityList)
			throws Exception {
		return null;
	}

	/**
	 * 検索を実行します.
	 * @param params パラメータを設定したマップ
	 * @param sortColumn ソート対象カラム名
	 * @param sortOrderAsc 昇順にソートするか否か
	 * @param rowCount 取得する検索件数
	 * @param offset 取得開始位置
	 * @return {@link ProductClassJoin}のリスト
	 * @throws ServiceException
	 */
	@Override
	protected List<ProductClassJoin> execSearch(BeanMap params,
			String sortColumn, boolean sortOrderAsc, int rowCount, int offset)
			throws ServiceException {
		params = Beans
				.createAndCopy(BeanMap.class, this.searchProductClassForm)
				.excludesNull().execute();
		ResponseUtil.write(JSON.encode(this.productClassService
				.findProductClassByCondition(params)), "text/javascript");
		return null;
	}

	/**
	 * 検索画面のアクションフォームを返します.
	 * @return {@link SearchProductClassForm}
	 */
	@Override
	protected AbstractSearchForm<ProductClassDto> getActionForm() {
		return this.searchProductClassForm;
	}

	/**
	 * 商品分類マスタDTOを返します.
	 * @return {@link ProductClassDto}
	 */
	@Override
	protected Class<ProductClassDto> getDtoClass() {
		return ProductClassDto.class;
	}

	@Override
	protected String getResultURIString() {
		return null;
	}

	/**
	 * 検索処理を行う商品分類マスタサービスを返します.
	 * @return {@link ProductClassService}
	 */
	@Override
	protected MasterSearch<ProductClassJoin> getService() {
		return productClassService;
	}

	/**
	 * 検索画面のメニューIDを返します.
	 * @return 商品分類マスタ画面のメニューID
	 */
	@Override
	protected String getSearchMenuID() {
		return Constants.MENU_ID.MASTER_PRODUCT_CLASS;
	}

	/**
	 * プルダウン大のリストを取得します．
	 * @return プルダウン大のリスト
	 * @throws Exception
	 */
	@Execute(validator = false)
	public String searchClass1() throws Exception {
		try {
			// 検索を行う
			List<ProductClass> productClassList = this.productClassService
					.findAllProductClass1();

			// 検索結果件数を設定する
			ResponseUtil
					.write(JSON.encode(productClassList), "text/javascript");
		} catch (ServiceException e) {
			super.errorLog(e);

			super.doAfterError(e);
		}
		return null;
	}

	/**
	 * 指定条件で次の値を取り出します.
	 * @return 商品分類マスタ情報
	 * @throws Exception
	 */
	@Execute(validator = false)
	public String searchNextValue() throws Exception {
		try {
			// 指定条件で次の値を取り出す
			String classCode1 = this.searchProductClassForm.classCode1;
			String classCode2 = this.searchProductClassForm.classCode2;

			Map<String, Object> conditions = new HashMap<String, Object>();
			if (!StringUtil.hasLength(classCode1)
					&& !StringUtil.hasLength(classCode2)) {
				// 分類（大）
				conditions.put(ProductClassService.Param.TARGET_COLUMN,
						ProductClassService.COLUMN_CLASS_CODE_1);
			} else if (!StringUtil.hasLength(classCode2)) {
				// 分類（中）
				conditions.put(ProductClassService.Param.TARGET_COLUMN,
						ProductClassService.COLUMN_CLASS_CODE_2);
				conditions.put(ProductClassService.Param.CLASS_CODE_1,
						classCode1);
			} else if (StringUtil.hasLength(classCode1)
					&& StringUtil.hasLength(classCode2)) {
				// 分類（小）
				conditions.put(ProductClassService.Param.TARGET_COLUMN,
						ProductClassService.COLUMN_CLASS_CODE_3);
				conditions.put(ProductClassService.Param.CLASS_CODE_1,
						classCode1);
				conditions.put(ProductClassService.Param.CLASS_CODE_2,
						classCode2);
			}
			String nextVal = this.productClassService.getNextCode(conditions);
			Map<String, String> result = new HashMap<String, String>();
			result.put("nextVal", nextVal);
			List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();
			resultList.add(result);
			ResponseUtil.write(JSON.encode(resultList), "text/javascript");
		} catch (ServiceException e) {
			super.errorLog(e);

			super.doAfterError(e);
		}
		return null;
	}
}
