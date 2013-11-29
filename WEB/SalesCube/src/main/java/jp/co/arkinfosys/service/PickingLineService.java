/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import jp.co.arkinfosys.common.CategoryTrns;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.dto.sales.SalesLineDto;
import jp.co.arkinfosys.entity.PickingLine;
import jp.co.arkinfosys.s2extend.NumberConverter;
import jp.co.arkinfosys.service.exception.ServiceException;

import org.seasar.framework.beans.util.BeanMap;
import org.seasar.framework.beans.util.Beans;

/**
 *  出荷指示書明細サービスクラスです.
 * @author Ark Information Systems
 *
 */
public class PickingLineService extends AbstractService<PickingLine> {

	//発番用サービス
	@Resource
	public SeqMakerService seqMakerService;

	/**
	 * SQLファイルのパラメータ名定義
	 */
	public static class Param {
		// ソート方向
		private static final String SORT_ORDER = "sortOrder";
		// 取得件数
		private static final String ROW_COUNT = "rowCount";
		// 取得件数
		private static final String OFFSET_ROW = "offsetRow";

		public static final String PICKING_LINE_ID = "pickingLineId";
		public static final String PICKING_LIST_ID = "pickingListId";
		public static final String RO_LINE_ID = "roLineId";
		public static final String PICKING_LINE_NO = "pickingLineNo";
		public static final String SALES_LINE_ID = "salesLineId";
		private static final String SORT_COLUMN_LINE_NO = "sortColumnLineNo"; // 行番号のソート条件
		public static final String SALES_SLIP_ID = "salesSlipId";
		public static final String SET_TYPE_CATEGORY = "setTypeCategory";

	}

	public String[] paramNames = { Param.SORT_ORDER, Param.ROW_COUNT,
			Param.OFFSET_ROW, Param.PICKING_LINE_ID, Param.PICKING_LIST_ID,
			Param.RO_LINE_ID, Param.PICKING_LINE_NO, Param.SALES_LINE_ID,
			Param.SORT_COLUMN_LINE_NO, Param.SALES_SLIP_ID,
			Param.SET_TYPE_CATEGORY };

	public static final String SORT_COLUMN_PICKING_LINE_NO = "LINE_NO";

	/**
	 * エンティティ情報から登録用Mapオブジェクトを生成します.
	 * @param pll　{@link PickingLine}
	 * @return　登録用マップ
	 */
	private Map<String, Object> createParamMap(PickingLine pll) {

		//MAPの生成
		Map<String, Object> param = new HashMap<String, Object>();

		//基礎となるカラム名(空で)をエンティティからPUT
		//		BeanMap FoundationParam = Beans.createAndCopy(BeanMap.class,this.depositLine).execute();
		//		param.putAll(FoundationParam);

		//アクションフォームの情報をPUT
		BeanMap AFparam = Beans.createAndCopy(BeanMap.class, pll).execute();
		param.putAll(AFparam);

		//更新日時とかPUT
		Map<String, Object> CommonParam = super.createSqlParam();
		param.putAll(CommonParam);

		return param;
	}

	/**
	 * 出荷指示書行IDを発番します.
	 * @return　出荷指示書行ID
	 * @throws Exception
	 */
	public Long getNextVal() throws Exception {

		Long newSlipId = -1L;
		//伝票番号の発番
		try {
			newSlipId = seqMakerService.nextval(PickingLine.TABLE_NAME);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return newSlipId;
	}

	/**
	 * DTOからエンティティへの変換します.
	 * @param unitFract　単価端数処理区分
	 * @param taxFract　税端数処理区分
	 * @param lineDto　{@link SalesLineDto}
	 * @return　{@link PickingLine}
	 */
	public PickingLine createAndCopy(String unitFract, String taxFract,
			SalesLineDto lineDto) {
		NumberConverter convTR = createTaxRateConverter();
		NumberConverter convPD = createProductNumConverter();
		NumberConverter convUP = createUnitPriceConverter(unitFract);
		NumberConverter convTax = createUnitPriceConverter(taxFract);
		return Beans.createAndCopy(PickingLine.class, lineDto).converter(
				convTR, "ctaxRate").converter(convPD, "quantity", "stockNum")
				.converter(convUP, "unitPrice", "unitRetailPrice",
						"retailPrice", "unitCost", "cost", "gm").converter(
						convTax, "ctaxPrice").dateConverter(
						Constants.FORMAT.TIMESTAMP, "creDatetm", "updDatetm")
				.execute();
	}

	/**
	 * エンティティからDTOへの変換します.
	 * @param unitFract　単価端数処理区分
	 * @param taxFract　税端数処理区分
	 * @param ss　{@link PickingLine}
	 * @return　{@link SalesLineDto}
	 */
	public SalesLineDto createAndCopy(String unitFract, String taxFract,
			PickingLine ss) {
		NumberConverter convTR = createTaxRateConverter();
		NumberConverter convPD = createProductNumConverter();
		NumberConverter convUP = createUnitPriceConverter(unitFract);
		NumberConverter convTax = createUnitPriceConverter(taxFract);
		return Beans.createAndCopy(SalesLineDto.class, ss).converter(convTR,
				"ctaxRate").converter(convPD, "quantity", "stockNum")
				.converter(convUP, "unitPrice", "unitRetailPrice",
						"retailPrice", "unitCost", "cost", "gm").converter(
						convTax, "ctaxPrice").dateConverter(
						Constants.FORMAT.TIMESTAMP, "creDatetm", "updDatetm")
				.execute();
	}

	/**
	 * DTOからエンティティへコピーします.
	 * @param unitFract　単価端数処理区分
	 * @param taxFract　税端数処理区分
	 * @param lineDto {@link SalesLineDto}
	 * @param ss {@link PickingLine}
	 */
	public void copy(String unitFract, String taxFract, SalesLineDto lineDto,
			PickingLine ss) {
		NumberConverter convTR = createTaxRateConverter();
		NumberConverter convPD = createProductNumConverter();
		NumberConverter convUP = createUnitPriceConverter(unitFract);
		NumberConverter convTax = createUnitPriceConverter(taxFract);
		Beans.copy(lineDto, ss).converter(convTR, "ctaxRate").converter(convPD,
				"quantity", "stockNum").converter(convUP, "unitPrice",
				"unitRetailPrice", "retailPrice", "unitCost", "cost", "gm")
				.converter(convTax, "ctaxPrice").dateConverter(
						Constants.FORMAT.TIMESTAMP, "creDatetm", "updDatetm")
				.execute();
	}

	/**
	 * エンティティからDTOへコピーします.
	 * @param unitFract　単価端数処理区分
	 * @param taxFract　税端数処理区分
	 * @param ss {@link PickingLine}
	 * @param lineDto {@link SalesLineDto}
	 */
	public void copy(String unitFract, String taxFract, PickingLine ss,
			SalesLineDto lineDto) {
		NumberConverter convTR = createTaxRateConverter();
		NumberConverter convPD = createProductNumConverter();
		NumberConverter convUP = createUnitPriceConverter(unitFract);
		NumberConverter convTax = createUnitPriceConverter(taxFract);
		Beans.copy(ss, lineDto).converter(convTR, "ctaxRate").converter(convPD,
				"quantity", "stockNum").converter(convUP, "unitPrice",
				"unitRetailPrice", "retailPrice", "unitCost", "cost", "gm")
				.converter(convTax, "ctaxPrice").dateConverter(
						Constants.FORMAT.TIMESTAMP, "creDatetm", "updDatetm")
				.execute();
	}

	/**
	 * エンティティ情報をDBに登録します.
	 * @param pll {@link PickingLine}
	 * @return　実行行数
	 *
	 */
	public int insert(PickingLine pll) {
		//SQLクエリを投げる
		return this.updateBySqlFile("picking/InsertPickingLineBySales.sql",
				createParamMap(pll)).execute();
	}

	/**
	 * エンティティ情報でDBを更新します.<BR>
	 * エンティティ内の情報のうち、使用するのは売上伝票IDのみです.
	 * @param pll {@link PickingLine}
	 * @return　実行行数
	 *
	 */
	public int update(PickingLine pll) {
		//SQLクエリを投げる
		return this.updateBySqlFile("picking/UpdatePickingLineBySales.sql",
				createParamMap(pll)).execute();
	}

	/**
	 * エンティティ情報でDBから削除します.
	 * @param pll {@link PickingLine}
	 * @return　実行行数
	 *
	 */
	public int delete(PickingLine pll) {
		super.updateAudit(PickingLine.TABLE_NAME,
				new String[] { Param.SALES_LINE_ID },
				new Object[] { pll.salesLineId });
		return this.updateBySqlFile("picking/DeletePickingLineBySales.sql",
				createParamMap(pll)).execute();
	}

	/**
	 *　出荷指示書番号を指定して出荷指示書の明細行を行番号昇順で取得します.
	 * @param pickingListId 出荷指示書番号
	 * @return {@link PickingLine}のリスト
	 * @throws ServiceException
	 */
	public List<PickingLine> findPickingLineByPickingListId(String pickingListId)
			throws ServiceException {

		LinkedHashMap<String, Object> conditions = new LinkedHashMap<String, Object>();

		// 条件設定
		// 顧客コードが一致
		conditions.put(Param.PICKING_LIST_ID, pickingListId);
		conditions.put(Param.SORT_COLUMN_LINE_NO, SORT_COLUMN_PICKING_LINE_NO);
		conditions.put(Param.SORT_ORDER, Constants.SQL.ASC);

		return findByCondition(conditions, paramNames,
				"picking/FindPickingLine.sql");

	}

	/**
	 *　売上伝票行IDを指定して出荷指示書の明細行を取得します.
	 * @param salesLineId 売上伝票明細行ID
	 * @return {@link PickingLine}
	 * @throws ServiceException
	 */
	public PickingLine findPickingLineBySalesLineId(String salesLineId)
			throws ServiceException {

		LinkedHashMap<String, Object> conditions = new LinkedHashMap<String, Object>();

		// 条件設定
		// 顧客コードが一致
		conditions.put(Param.SALES_LINE_ID, salesLineId);

		List<PickingLine> plList = findByCondition(conditions, paramNames,
				"picking/FindPickingLine.sql");
		if (plList.size() != 1) {
			throw new ServiceException("errors.system");
		} else {
			return plList.get(0);
		}
	}

	/**
	 *　売上伝票IDを指定して出荷指示書の明細行を取得します.
	 * @param salesSlipId 売上伝票ID
	 * @return {@link PickingLine}のリスト
	 * @throws ServiceException
	 */
	public List<PickingLine> findPickingLineBySalesSlipId(String salesSlipId)
			throws ServiceException {

		LinkedHashMap<String, Object> conditions = new LinkedHashMap<String, Object>();

		// 条件設定
		// 顧客コードが一致
		conditions.put(Param.SALES_SLIP_ID, salesSlipId);
		conditions.put(Param.SORT_COLUMN_LINE_NO, SORT_COLUMN_PICKING_LINE_NO);
		conditions.put(Param.SORT_ORDER, Constants.SQL.ASC);

		return findByCondition(conditions, paramNames,
				"picking/FindPickingLineBySales.sql");
	}

	/**
	 *　売上伝票行IDを指定して出荷指示書の明細行を取得します.
	 * @param salesLineId 売上伝票明細行ID
	 * @return {@link PickingLine}をBeanMapに入れたリスト
	 * @throws ServiceException
	 */
	public List<BeanMap> findPickingLineBySalesSlipIdSimple(String salesLineId)
			throws ServiceException {

		LinkedHashMap<String, Object> conditions = new LinkedHashMap<String, Object>();

		// 条件設定
		// 顧客コードが一致
		conditions.put(Param.SALES_SLIP_ID, salesLineId);
		conditions.put(Param.SORT_COLUMN_LINE_NO, SORT_COLUMN_PICKING_LINE_NO);
		conditions.put(Param.SORT_ORDER, Constants.SQL.ASC);

		List<PickingLine> plList = findByCondition(conditions, paramNames,
				"picking/FindPickingLineBySales.sql");
		List<BeanMap> listBean = new ArrayList<BeanMap>();
		for (PickingLine pl : plList) {
			listBean.add(Beans.createAndCopy(BeanMap.class, pl).execute());
		}
		return listBean;
	}

	/**
	 *　売上伝票行IDを指定して出荷指示書のセット商品明細行を取得します.
	 * @param salesLineId 売上伝票明細行ID
	 * @return {@link PickingLine}をBeanMapに入れたリスト
	 * @throws ServiceException
	 */
	public List<BeanMap> findPickingLineSetBySalesSlipIdSimple(
			String salesLineId) throws ServiceException {

		LinkedHashMap<String, Object> conditions = new LinkedHashMap<String, Object>();

		// 条件設定
		// 顧客コードが一致
		conditions.put(Param.SALES_SLIP_ID, salesLineId);
		conditions.put(Param.SET_TYPE_CATEGORY,
				CategoryTrns.PRODUCT_SET_TYPE_SET);
		conditions.put(Param.SORT_COLUMN_LINE_NO, SORT_COLUMN_PICKING_LINE_NO);
		conditions.put(Param.SORT_ORDER, Constants.SQL.ASC);

		List<PickingLine> plList = findByCondition(conditions, paramNames,
				"picking/FindPickingLineBySales.sql");
		List<BeanMap> listBean = new ArrayList<BeanMap>();
		for (PickingLine pl : plList) {
			listBean.add(Beans.createAndCopy(BeanMap.class, pl).execute());
		}
		return listBean;
	}

	/**
	 * 売上伝票情報から出荷指示書明細エンティティを作成します.
	 * @param lineDto {@link SalesLineDto}
	 * @param pickingListId 出荷指示書番号
	 * @return {@link PickingLine}
	 * @throws Exception
	 */
	public PickingLine createPickingLine(SalesLineDto lineDto,
			String pickingListId) throws Exception {
		Long newLineId = getNextVal();

		PickingLine pll = new PickingLine();
		pll.pickingLineId = Integer.parseInt(newLineId.toString());
		pll.pickingListId = Integer.parseInt(pickingListId);
		pll.salesLineId = Integer.parseInt(lineDto.salesLineId);

		return pll;
	}
}
