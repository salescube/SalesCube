/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.ajax.dialog;

import java.util.LinkedHashMap;

import javax.annotation.Resource;

import jp.co.arkinfosys.dto.StockInfoDto;
import jp.co.arkinfosys.form.ajax.dialog.ShowStockInfoDialogForm;
import jp.co.arkinfosys.service.ProductStockService;
import jp.co.arkinfosys.service.exception.ServiceException;
import net.arnx.jsonic.JSON;

import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.util.ResponseUtil;

/**
 * 商品在庫情報ダイアログの表示処理アクションクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class ShowStockInfoDialogAction extends AbstractDialogAction {

	/**
	 * アクションフォームです.
	 */
	@ActionForm
	@Resource
	public ShowStockInfoDialogForm showStockInfoDialogForm;

	/**
	 * 商品在庫テーブルに対するサービスクラスです.
	 */
	@Resource
	public ProductStockService productStockService;

	/**
	 * 表示する在庫情報を取得してアクションフォームに情報を格納します.
	 *
	 * @throws ServiceException サービス例外発生時
	 */
	@Override
	protected void createList() throws ServiceException {
		this.showStockInfoDialogForm.stockInfoDto = this.productStockService
				.calcStockQuantityByProductCode(this.showStockInfoDialogForm.productCode);
		if (this.showStockInfoDialogForm.stockInfoDto.productCode != null
				&& this.showStockInfoDialogForm.stockInfoDto.productName != null) {
			this.showStockInfoDialogForm.rorderRestDetailList = this.productStockService
					.findRorderRestDetailByProductCode(showStockInfoDialogForm.productCode);
			this.showStockInfoDialogForm.porderRestDetailList = this.productStockService
					.findPorderRestDetailByProductCode(showStockInfoDialogForm.productCode);
			this.showStockInfoDialogForm.entrustPorderRestDetailList = this.productStockService
					.findEntrustPorderRestDetailByProductCode(showStockInfoDialogForm.productCode);
			this.showStockInfoDialogForm.entrustStockDetailList = this.productStockService
					.findEntrustStockDetailByProductCode(showStockInfoDialogForm.productCode);
		}
	}

	/**
	 * 在庫数検索の結果値を取得するためのSELECT項目名を定義するクラスです.
	 */
	public static class Param {
		/**
		 * 商品コードです.
		 */
		public static final String PRODUCT_CODE = "productCode";

		/**
		 * 現在庫数です.
		 */
		public static final String CURRENT_TOTAL_QUANTITY = "currentTotalQuantity";

		/**
		 * 受注残数です.
		 */
		public static final String RORDER_REST_QUANTITY = "rorderRestQuantity";

		/**
		 * 発注残数です.
		 */
		public static final String PORDER_REST_QUANTITY = "porderRestQuantity";

		/**
		 * 委託発注残数です.
		 */
		public static final String ENTRUST_REST_QUANTITY = "entrustRestQuantity";

		/**
		 * 引当可能数です.
		 */
		public static final String POSSIBLE_DROW_QUANTITY = "possibleDrawQuantity";

		/**
		 * 保有数です.保有数は次の式の値となります.（現在庫数 ＋ 委託在庫数 ＋ 発注残数 ＋ 委託残数 － 受注残数）
		 */
		public static final String HOLDING_STOCK_QUANTITY = "holdingStockQuantity";
	}

	/**
	 * 商品在庫情報ダイアログの表示処理メソッドです.
	 *
	 * @return 在庫情報ダイアログのJPSパス
	 * @throws Exception 例外発生時
	 */
	@Execute(validator = false)
	public String calcStock() throws Exception {
		try {
			StockInfoDto stockInfoDto = this.productStockService
					.calcStockQuantityByProductCode(showStockInfoDialogForm.productCode);

			LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
			map.put(ShowStockInfoDialogAction.Param.PRODUCT_CODE,
					(stockInfoDto.productCode == null ? ""
							: stockInfoDto.productCode));
			map.put(ShowStockInfoDialogAction.Param.CURRENT_TOTAL_QUANTITY,
					(String.valueOf(stockInfoDto.currentTotalQuantity)));
			map.put(ShowStockInfoDialogAction.Param.RORDER_REST_QUANTITY,
					(String.valueOf(stockInfoDto.rorderRestQuantity)));
			map.put(ShowStockInfoDialogAction.Param.PORDER_REST_QUANTITY,
					(String.valueOf(stockInfoDto.porderRestQuantity)));
			map.put(ShowStockInfoDialogAction.Param.ENTRUST_REST_QUANTITY,
					(String.valueOf(stockInfoDto.entrustRestQuantity)));
			map.put(ShowStockInfoDialogAction.Param.POSSIBLE_DROW_QUANTITY,
					(String.valueOf(stockInfoDto.possibleDrawQuantity)));

			ResponseUtil.write(JSON.encode(map), "text/javascript");

		} catch (Exception e) {
			super.errorLog(e);

			// システム例外として処理する
			super.writeSystemErrorToResponse();
			return null;
		}

		return null;
	}
}
