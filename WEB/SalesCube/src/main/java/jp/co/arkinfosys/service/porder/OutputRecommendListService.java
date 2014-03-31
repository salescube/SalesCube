/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.service.porder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.transaction.UserTransaction;

import jp.co.arkinfosys.common.Categories;
import jp.co.arkinfosys.common.CategoryTrns;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.dto.DetailDispItemDto;
import jp.co.arkinfosys.dto.YmDto;
import jp.co.arkinfosys.dto.porder.OutputRecommendListFormDto;
import jp.co.arkinfosys.dto.stock.ProductStockInfoDto;
import jp.co.arkinfosys.entity.PoLineTrn;
import jp.co.arkinfosys.entity.PoSlipTrn;
import jp.co.arkinfosys.entity.Supplier;
import jp.co.arkinfosys.service.AbstractService;
import jp.co.arkinfosys.service.ProductService;
import jp.co.arkinfosys.service.SeqMakerService;
import jp.co.arkinfosys.service.SupplierService;
import jp.co.arkinfosys.service.YmService;
import jp.co.arkinfosys.service.exception.ServiceException;

import org.seasar.framework.beans.util.Beans;
/**
 * 補充発注推奨リスト出力サービスクラスです.
 * @author Ark Information Systems
 *
 */
public class OutputRecommendListService extends AbstractService<PoSlipTrn> {

	/**
	 * パラメータ定義クラスです.
	 */
	public static class Param {
		private static final String PO_DATE = "poDate";
		private static final String PO_ANNUAL = "poAnnual";
		private static final String PO_MONTHLY = "poMonthly";
		private static final String PO_YM = "poYm";

		private static final String SUPPLIER_CODE = "supplierCode";

		private static final String EAD_CATEGORY_ENTER = "eadCategoryEnter";
		private static final String EAD_CATEGORY_DISPATCH = "eadCategoryDispatch";
		private static final String RACK_CATEGORY_OWN = "rackCategoryOwn";
		private static final String RACK_CATEGORY_ENTRUST = "rackCategoryEntrust";
		private static final String PRODUCT_SET_TYPE_SINGLE = "productSetTypeSingle";
		private static final String TRANSPORT_CATEGORY_NOT_ENTRUST = "transportCategoryNotEntrust";
		private static final String TRANSPORT_CATEGORY_ENTRUST = "transportCategoryEntrust";
		//在庫管理区分	単体テスト懸念事項#63対応
		private static final String STOCK_CTL_CATEGORY_NO = "stockCtlCategoryNo";

		private static final String PRODUCT_STATUS_ONSALE = "productStatusOnsale";

		private static final String PRODUCT_CODE = "productCode";
		private static final String PO_QUANTITY = "poQuantity";
		private static final String PO_LOT = "poLot";
		private static final String AVG_SHIP_COUNT = "avgShipCount";
		private static final String STOCK_QUANTITY = "currentStockQuantity";
		private static final String PO_NUM = "poNum";
		private static final String HOLD_TERM = "holdingStockMonth";
		private static final String HOLD_QUANTITY = "holdingStockQuantity";
		private static final String PORDER_REST_QUANTITY = "restQuantityPo";
		private static final String PORDER_REST_ENTRUST_QUANTITY = "restQuantityEntrust";
		private static final String RORDER_REST_QUANTITY = "restQuantityRo";
		private static final String ENTRUST_STOCK_QUANTITY = "stockQuantityEntrustEad";

		private static final String LEAD_TIME = "leadTime";
		private static final String SALES_STANDARD_DEVIATION = "salesStandardDeviation";
		private static final String MINE_SFETY_STOCK = "mineSafetyStock";
		private static final String ENTRUST_SFETY_STOCK = "entrustSafetyStock";
		private static final String ENTRUST_PO_NUM = "entrustPoNum";

		public static final String PO_CATEGORY = "poCategory";
		public static final String IMMEDIATELY_PO_CATEGORY = "immediatelyPOCategory";
	}

	/**
	 * カラム定義クラスです.
	 */
	public static class Column {
		// 伝票
		public static final String SRC_FUNC = "SRC_FUNC";
	}

	//仕入先情報取得用
	@Resource
	protected SupplierService supplierService;

	/**
	 * 商品情報取得用サービス
	 */
	@Resource
	protected ProductService productService;

	/**
	 * 補充発注推奨リストにリストアップされる仕入先の一覧を返します.
	 * @return 仕入先一覧
	 * @throws ServiceException
	 */
	public List<Supplier> findRecommendSuppliers() throws ServiceException {
		//ドメイン情報設定
		Map<String, Object> param = super.createSqlParam();
		//残りの情報を設定
		setDefaultCondition(param);
		//検索して結果を返す
		return this.selectBySqlFile(Supplier.class,
				"porder/FindRecommendSuppliers.sql", param).getResultList();
	}

	/**
	 * カラム情報リスト(ソートのための情報)を返します.
	 * @return カラム情報リスト
	 */
	public List<DetailDispItemDto> getColumnInfoList() {
		List<DetailDispItemDto>	columnInfoList = new ArrayList<DetailDispItemDto>();
		DetailDispItemDto dto;
		dto = new DetailDispItemDto();
		dto.itemId = Param.PRODUCT_CODE;
		dto.itemName = "商品コード";
		dto.sortFlag = "1";
		columnInfoList.add(dto);

		dto = new DetailDispItemDto();
		dto.itemId = Param.PO_QUANTITY;
		dto.itemName = "発注数";
		dto.sortFlag = "0";
		columnInfoList.add(dto);

		dto = new DetailDispItemDto();
		dto.itemId = Param.PO_LOT;
		dto.itemName = "発注ロット";
		dto.sortFlag = "1";
		columnInfoList.add(dto);

		dto = new DetailDispItemDto();
		dto.itemId = Param.LEAD_TIME;
		dto.itemName = "リードタイム";
		dto.sortFlag = "1";
		columnInfoList.add(dto);

		dto = new DetailDispItemDto();
		dto.itemId = Param.AVG_SHIP_COUNT;
		dto.itemName = "平均出荷数";
		dto.sortFlag = "1";
		columnInfoList.add(dto);

		dto = new DetailDispItemDto();
		dto.itemId = Param.SALES_STANDARD_DEVIATION;
		dto.itemName = "出荷数偏差(σ)";
		dto.sortFlag = "1";
		columnInfoList.add(dto);

		dto = new DetailDispItemDto();
		dto.itemId = Param.STOCK_QUANTITY;
		dto.itemName = "現在庫数";
		dto.sortFlag = "1";
		columnInfoList.add(dto);

		dto = new DetailDispItemDto();
		dto.itemId = Param.MINE_SFETY_STOCK;
		dto.itemName = "自社安全在庫数";
		dto.sortFlag = "1";
		columnInfoList.add(dto);

		dto = new DetailDispItemDto();
		dto.itemId = Param.PO_NUM;
		dto.itemName = "自社在庫発注点";
		dto.sortFlag = "1";
		columnInfoList.add(dto);

		dto = new DetailDispItemDto();
		dto.itemId = Param.ENTRUST_STOCK_QUANTITY;
		dto.itemName = "委託在庫数";
		dto.sortFlag = "1";
		columnInfoList.add(dto);

		dto = new DetailDispItemDto();
		dto.itemId = Param.ENTRUST_SFETY_STOCK;
		dto.itemName = "委託安全在庫数";
		dto.sortFlag = "1";
		columnInfoList.add(dto);

		dto = new DetailDispItemDto();
		dto.itemId = Param.ENTRUST_PO_NUM;
		dto.itemName = "委託在庫発注数";
		dto.sortFlag = "1";
		columnInfoList.add(dto);

		dto = new DetailDispItemDto();
		dto.itemId = Param.HOLD_TERM;
		dto.itemName = "保有月数";
		dto.sortFlag = "1";
		columnInfoList.add(dto);

		dto = new DetailDispItemDto();
		dto.itemId = Param.HOLD_QUANTITY;
		dto.itemName = "保有数";
		dto.sortFlag = "1";
		columnInfoList.add(dto);

		dto = new DetailDispItemDto();
		dto.itemId = Param.PORDER_REST_QUANTITY;
		dto.itemName = "発注残数";
		dto.sortFlag = "1";
		columnInfoList.add(dto);

		dto = new DetailDispItemDto();
		dto.itemId = Param.PORDER_REST_ENTRUST_QUANTITY;
		dto.itemName = "委託残数";
		dto.sortFlag = "1";
		columnInfoList.add(dto);

		dto = new DetailDispItemDto();
		dto.itemId = Param.RORDER_REST_QUANTITY;
		dto.itemName = "受注残数";
		dto.sortFlag = "1";
		columnInfoList.add(dto);

		return columnInfoList;
	}

	/**
	 * 検索条件に応じたカラム情報リスト(ソートのための情報)を返します.
	 *
	 * @param param 検索条件
	 * @return カラム情報リスト
	 */
	public List<DetailDispItemDto> getColumnInfoList(Map<String, Object> param) {
		List<DetailDispItemDto> columnInfoList =  this.getColumnInfoList();

		// 発注区分毎の検索条件を設定する
		if (CategoryTrns.IMMEDIATELY_PORDER
				.equals(param.get(Param.PO_CATEGORY))
				&& CategoryTrns.MOVE_ENTRUST_STOCK.equals(param
						.get(Param.IMMEDIATELY_PO_CATEGORY))) {
			for(DetailDispItemDto columnInfo : columnInfoList) {
				if(Param.PO_QUANTITY.equals(columnInfo.itemId)) {
					columnInfo.itemName = "倉庫移動数";
					break;
				}
			}
		}

		return columnInfoList;
	}

	/**
	 * 指定された条件の補充発注推奨リストを返します.
	 * @param param 検索条件
	 * @param sortColumn ソート対象カラム
	 * @param sortOrderAsc 昇順でソートするか否か
	 * @param productCodeInputArray 補充発注推奨リストの商品コードリスト
	 * @param checkInputArray 補充発注推奨リストのチェック状態のリスト
	 * @param orderInputArray 補充発注推奨リストの発注数リスト
	 * @return 補充発注推奨リスト
	 * @throws ServiceException
	 */
	public List<OutputRecommendListFormDto> findRecommendByCondition(
			Map<String, Object> param, String sortColumn, boolean sortOrderAsc, String [] productCodeInputArray, String [] checkInputArray, String [] orderInputArray) throws ServiceException {
		try {
			//戻り値
			List<OutputRecommendListFormDto> searchResultList = new ArrayList<OutputRecommendListFormDto>();

			// 検索条件の設定
			Map<String, Object> map = new HashMap<String, Object>();
			// 固定設定項目
			map.put(ProductService.Param.SET_TYPE_CATEGORY, CategoryTrns.PRODUCT_SET_TYPE_SINGLE);		//単品
			map.put(ProductService.Param.PRODUCT_STATUS_CATEGORY, CategoryTrns.PRODUCT_STATUS_ONSALE);	//販売中商品
			map.put(ProductService.Param.STOCK_CTL_CATEGORY, CategoryTrns.PRODUCT_STOCK_CTL_YES);		//在庫管理する商品
			map.put(ProductService.Param.PRODUCT_STOCK_CATEGORY, CategoryTrns.PRODUCT_STOCK_INSTOCK);	//自社在庫品
			map.put(ProductService.Param.PRODUCT_STANDARD_CATEGORY, CategoryTrns.PRODUCT_STANDARD_STD);		//標準品
			map.putAll(param);

			// 発注区分毎の検索条件を設定する
			if(CategoryTrns.IMMEDIATELY_PORDER.equals(param.get(Param.PO_CATEGORY))) {
				// 都度発注
				map.put(ProductService.Param.HOLDING_STOCK_LESS_THAN_PO_NUM, true);

				if(CategoryTrns.NORMAL_PORDER.equals(param.get(Param.IMMEDIATELY_PO_CATEGORY))) {
					// 通常発注
					map.put(ProductService.Param.ENTRUST_STOCK_ZERO, true);
				}
				else if(CategoryTrns.MOVE_ENTRUST_STOCK.equals(param.get(Param.IMMEDIATELY_PO_CATEGORY))) {
					// 倉庫移動
					map.put(ProductService.Param.ENTRUST_STOCK_LARGER_THAN_ZERO, true);
					map.put(ProductService.Param.ADD_PORDER_INFO, true);
				}
			}
			else if(CategoryTrns.ENTRUST_PORDER.equals(param.get(Param.PO_CATEGORY))) {
				// 委託発注
				map.put(ProductService.Param.ENTRUST_PORDER_QUANTITY_LARGER_THAN_ZERO, true);
			}

			//検索する
			List<ProductStockInfoDto> stockInfoList = productService.aggregateProductStockInfoByCondition(map, sortColumn, sortOrderAsc);

			for(ProductStockInfoDto productStockInfoDto: stockInfoList){

				OutputRecommendListFormDto productStockJoinDto
					= Beans.createAndCopy(
							OutputRecommendListFormDto.class, productStockInfoDto).execute();
				//発注可否
				productStockJoinDto.validRow = false;	// 初期状態はチェックボックスを未選択状態とする。
				//発注ロット
				productStockJoinDto.poLot = productStockInfoDto.poLot.toPlainString();// getPoLotStr();
				//平均出庫数
				productStockJoinDto.avgShipCount = nullToZero(productStockJoinDto.avgShipCount);
				//出荷数標準偏差
				productStockJoinDto.salesStandardDeviation = productStockInfoDto.salesStandardDeviation.toPlainString(); // getSalesStandardDeviationStr();
				//現在庫数
				productStockJoinDto.stockQuantity = productStockInfoDto.currentStockQuantity.toPlainString(); // getCurrentStockQuantityStr();
				//委託在庫
				productStockJoinDto.entrustQuantity = productStockInfoDto.stockQuantityEntrustEad.toPlainString(); // getStockQuantityEntrustEadStr();
				//発注点
				productStockJoinDto.poNum = nullToZero(productStockJoinDto.poNum);
				//発注残
				productStockJoinDto.poRestQuantity = productStockInfoDto.restQuantityPo.toPlainString(); // getRestQuantityPoStr();
				//委託残
				productStockJoinDto.entrustRestQuantity = productStockInfoDto.restQuantityEntrust.toPlainString(); // getRestQuantityEntrustStr();
				//受注残
				productStockJoinDto.roRestQuantity = productStockInfoDto.restQuantityRo.toPlainString(); // getRestQuantityRoStr();
				// 委託発注数
				productStockJoinDto.entrustPoNum = productStockInfoDto.entrustPoNum.toPlainString(); // getEntrustPoNumStr();
				// 発注伝票番号
				if(productStockInfoDto.poSlipId != null) {
					productStockJoinDto.poSlipId = productStockInfoDto.poSlipId.toString();
				}

				//保有数を計算
				productStockJoinDto.holdQuantity = productStockInfoDto.holdingStockQuantity.toPlainString(); // getHoldingStockQuantityStr();
				//保有月数を計算
				productStockJoinDto.holdTerm = productStockInfoDto.getHoldingStockMonthStr();
				if( "".equals(productStockJoinDto.holdTerm) ) {
					productStockJoinDto.holdTerm="--";		//保有月数が計算不能(平均出荷数=0の場合、0割りが発生して計算不能となる)の場合の表示値
				}

				// 発注数量の初期化
				if(CategoryTrns.ENTRUST_PORDER.equals(param.get(Param.PO_CATEGORY))) {
					// 委託在庫発注の場合は委託発注数を設定
					productStockJoinDto.pOrderQuantity = productStockJoinDto.entrustPoNum;
				}
				else {
					// 発注数量(発注ロットと平均出荷数の大きい値の方で初期化する)
					if( nullToZero(productStockInfoDto.poLot).compareTo( new BigDecimal( nullToZero(productStockInfoDto.avgShipCount) ) ) > 0 ) {
						productStockJoinDto.pOrderQuantity = productStockJoinDto.poLot;
					} else {
						productStockJoinDto.pOrderQuantity = productStockJoinDto.avgShipCount;
					}
				}

				// 入力された状態でソートされた場合、入力されていた値を元の商品の行へ復元する
				if(productCodeInputArray != null){
					List<String> productCodeInputList = Arrays.asList(productCodeInputArray);
					int currentIndex = productCodeInputList.indexOf(productStockInfoDto.productCode);

					if(currentIndex != -1){
						if( "true".equals(checkInputArray[currentIndex]) ){
							productStockJoinDto.validRow = true;
						}

						productStockJoinDto.pOrderQuantity = orderInputArray[currentIndex];
					}
				}

				//リストに追加
				searchResultList.add(productStockJoinDto);
			}
			return searchResultList;

		} catch (Exception e) {
			ServiceException se = new ServiceException(e);
			se.setStopOnError(true);
			throw se;
		}
	}

	/**
	 * Null値を0に変換して返します.
	 * @param l_target 変換元の値
	 * @return Nullを0に変換した値
	 */
	//Nullをゼロに変換
	private String nullToZero(String l_target){
		return ((l_target == null || l_target.length() == 0)?"0":l_target);
	}

	/**
	 * Null値を0に変換して返します.
	 * @param target 変換元の値
	 * @return Nullを0に変換した値
	 */
	//Nullをゼロに変換
	private Integer nullToZero(Integer target){
		if(target == null)
			return 0;
		else
			return target;
	}

	/**
	 * Null値を0に変換して返します.
	 * @param target 変換元の値
	 * @return Nullを0に変換した値
	 */
	//Nullをゼロに変換
	private BigDecimal nullToZero(BigDecimal target){
		if(target == null)
			return new BigDecimal(0);
		else
			return target;
	}


	/**
	 * 検索条件のデフォルト値を設定します.
	 * @param param 検索条件
	 * @return デフォルト値を設定した検索条件
	 */
	private Map<String, Object> setDefaultCondition(Map<String, Object> param) {
		param.put(Param.SUPPLIER_CODE, null);
		param.put(Param.EAD_CATEGORY_ENTER, CategoryTrns.EAD_CATEGORY_ENTER);
		param.put(Param.EAD_CATEGORY_DISPATCH, CategoryTrns.EAD_CATEGORY_DISPATCH);
		param.put(Param.RACK_CATEGORY_OWN, CategoryTrns.RACK_CATEGORY_OWN);
		param.put(Param.RACK_CATEGORY_ENTRUST, CategoryTrns.RACK_CATEGORY_ENTRUST);

		param.put(Param.PRODUCT_SET_TYPE_SINGLE, CategoryTrns.PRODUCT_SET_TYPE_SINGLE);
		List<String> transportCategoriesE = new ArrayList<String>();
		transportCategoriesE.add(CategoryTrns.TRANSPORT_CATEGORY_ENTRUST);
		param.put(Param.TRANSPORT_CATEGORY_ENTRUST, transportCategoriesE);
		List<String> transportCategoriesNE = new ArrayList<String>();
		transportCategoriesNE.add(CategoryTrns.TRANSPORT_CATEGORY_AIR);
		transportCategoriesNE.add(CategoryTrns.TRANSPORT_CATEGORY_SHIP);
		transportCategoriesNE.add(CategoryTrns.TRANSPORT_CATEGORY_DELIVERY);
		param.put(Param.TRANSPORT_CATEGORY_NOT_ENTRUST, transportCategoriesNE);
		//在庫管理区分	単体テスト懸念事項#63対応
		param.put(Param.STOCK_CTL_CATEGORY_NO, CategoryTrns.PRODUCT_STOCK_CTL_NO);
		//分類状況		単体テスト懸念事項#63追加対応
		param.put(Param.PRODUCT_STATUS_ONSALE, CategoryTrns.PRODUCT_STATUS_ONSALE);
		return param;
	}

	//発番のため
	public SeqMakerService seqMakerService;

	// 年月度取得のため
	@Resource
	protected YmService ymService;

	//発番エラー検出用初期値
	public static final Long DEFAULT_ID = -1L;
	//発番エラー時の戻り値
	public static final Long CANNOT_GET_ID = DEFAULT_ID;
	//伝票登録失敗時の戻り値
	public static final Long CANNOT_CREATE_SLIP = -3L;

	/**
	 *
	 * 伝票検索パラメータ定義クラスです.
	 *
	 */
	//独自に代入あるいはチェックすべきパラメータ(DB対応有)
	public static class SlipParam {
		public static final String PO_SLIP_ID = "poSlipId";
		public static final String DELIVERY_DATE = "deliveryDate";
		public static final String SUPPLIER_CODE = "supplierCode";

		public static final String TRANSPORT_CATEGORY = "transportCategory";

		public static final String PO_LINE_ID = "poLineId";
		public static final String LINE_NO = "lineNo";
		public static final String PRODUCT_CODE = "productCode";
		public static final String QUANTITY = "quantity";

		public static final String TAX_TYPE_CATEGORY = "taxTypeCategory";
		public static final String TAX_SHIFT_CATEGORY_LIST = "taxShiftCategoryList";

		public static final String ROUND_DOWN_ID = "roundDownId";
		public static final String HALF_UP_ID = "halfUpId";
		public static final String ROUND_UP_ID = "roundUpId";
		public static final String NUM_ALIGN_MAX = "numAlignMax";
		public static final String PRICE_ALIGN_MAX = "priceAlignMax";
		public static final String QUANTITY_FRACT_CATEGORY_ID = "quantityFractCategoryId";
		public static final String PRICE_FRACT_CATEGORY_ID = "priceFractCategoryId";
		public static final String TAX_FRACT_CATEGORY_ID = "taxFractCategoryId";
		
		public static final String CTAX_RATE = "ctaxRate";
		public static final String CTAX_TOTAL = "ctaxTotal";
	}

	//任意のタイミングでロールバックしたい
	public UserTransaction userTransaction;

	/**
	 * 共通の検索パラメータを作成します.
	 * @return 検索パラメータ
	 */
	//共通変数取得
	public Map<String, Object> createCommonParam(){
		Map<String, Object> commonParam = super.createSqlParam();

		//発注日用に当日日付を取得
		String poDate = this.selectBySqlFile(String.class, "porder/GetTodayForPODate.sql").getSingleResult();
		commonParam.put(Param.PO_DATE, poDate);

		// 年度、月度、年月度を計算
		YmDto ymDto;
		try {
			ymDto = ymService.getYm(poDate);
			commonParam.put(Param.PO_ANNUAL, (ymDto==null?null:ymDto.annual));
			commonParam.put(Param.PO_MONTHLY, (ymDto==null?null:ymDto.monthly));
			commonParam.put(Param.PO_YM, (ymDto==null?null:ymDto.ym));
		} catch (ServiceException e) {
		}

		return commonParam;
	}

	/**
	 * 必要情報を補って発注伝票を作成します.
	 * @param slipParam 伝票パラメータ
	 * @param lineParam 明細行パラメータ
	 * @return 発注伝票番号
	 * @throws Exception
	 */
	public long createSlipByParam(Map<String, Object> slipParam,List<Map<String, Object>> lineParam) throws Exception {
		//伝票番号発番
		long slipId = DEFAULT_ID;
		slipId = seqMakerService.nextval(PoSlipTrn.TABLE_NAME);
		if(slipId == DEFAULT_ID){return CANNOT_GET_ID;}

		//明細行ID発番
		Long lineId[] = new Long[lineParam.size()];
		for(int i=0; i<lineParam.size(); i++){
		lineId[i] = DEFAULT_ID;
		lineId[i] = seqMakerService.nextval(PoLineTrn.TABLE_NAME);
			if(lineId[i].equals(DEFAULT_ID)) {return CANNOT_GET_ID;}
		}

		//ドメイン情報設定
		Map<String, Object> param = new HashMap<String, Object>();

		//定数パラメータ
		//区分敬称
		param.put(SupplierService.Param.PRE_TYPE_CATEGORY_ID, Categories.PRE_TYPE);
		//区分消費税
		param.put(SlipParam.TAX_TYPE_CATEGORY, CategoryTrns.TAX_TYPE_CTAX);
		//税転嫁
		List<String> taxShiftCategoryList = new ArrayList<String>();
		taxShiftCategoryList.add(CategoryTrns.TAX_SHIFT_CATEGORY_SLIP_TOTAL);
		taxShiftCategoryList.add(CategoryTrns.TAX_SHIFT_CATEGORY_CLOSE_THE_BOOKS);
		param.put(SlipParam.TAX_SHIFT_CATEGORY_LIST, taxShiftCategoryList);
		//端数処理パラメータ
		param.put(SlipParam.ROUND_DOWN_ID, CategoryTrns.FLACT_CATEGORY_DOWN);
		param.put(SlipParam.HALF_UP_ID, CategoryTrns.FLACT_CATEGORY_HALF_UP);
		param.put(SlipParam.ROUND_UP_ID, CategoryTrns.FLACT_CATEGORY_UP);
		param.put(SlipParam.NUM_ALIGN_MAX, Constants.DECIMAL_NUM_ALIGN_MAX);
		param.put(SlipParam.PRICE_ALIGN_MAX, Constants.DECIMAL_PRICE_ALIGN_MAX);
		param.put(SlipParam.QUANTITY_FRACT_CATEGORY_ID, Categories.PRODUCT_FRACT_CATEGORY);
		param.put(SlipParam.PRICE_FRACT_CATEGORY_ID, Categories.PRICE_FRACT_CATEGORY);
		param.put(SlipParam.TAX_FRACT_CATEGORY_ID, Categories.TAX_FRACT_CATEGORY);
		
		// 消費税対応
		param.put(SlipParam.CTAX_RATE, null);
		param.put(SlipParam.CTAX_TOTAL, null);

		//伝票用パラメータ
		param.putAll(slipParam);
		param.put(SlipParam.PO_SLIP_ID, slipId);
		//伝票登録
		if( ((this.updateBySqlFile("porder/InsertPOrderSlipWithAutoFill.sql", param).execute()) != 1) ){
				//意図的にrollback
				userTransaction.setRollbackOnly();
				return CANNOT_CREATE_SLIP;
			}

		//明細行数
		int SuccessedLinesCount = 0;
		//明細行登録
		for(int i=0; i<lineParam.size(); i++){

			//明細行用パラメータ
			Map<String, Object> lparam = param;
			lparam.putAll(lineParam.get(i));
			lparam.put(SlipParam.PO_LINE_ID, lineId[i]);
			SuccessedLinesCount++;
			lparam.put(SlipParam.LINE_NO, SuccessedLinesCount);

			//明細行登録 with レート、消費税取得
			if( ((this.updateBySqlFile("porder/InsertPOrderLineWithAutoFill.sql", lparam).execute()) != 1) ||
					((this.updateBySqlFile("porder/UpdatePOrderLineQuantitiesWithFract.sql", lparam).execute()) != 1) ){
				//意図的にrollback
				userTransaction.setRollbackOnly();
				return CANNOT_CREATE_SLIP;
			}
		}

		//伝票合計金額更新
		if( ( (this.updateBySqlFile("porder/UpdatePOrderSlipTotalPrice.sql", param).execute()) != 1) ||
				((this.updateBySqlFile("porder/UpdatePOrderSlipQuantitiesWithFract.sql", param).execute()) != 1)
			){
			//意図的にrollback
			userTransaction.setRollbackOnly();
			return CANNOT_CREATE_SLIP;
		}

		return slipId;
	}
}
