/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.dto.stock;

import java.math.BigDecimal;
import java.math.MathContext;
import java.text.DecimalFormat;
import java.util.Date;

import jp.co.arkinfosys.common.CategoryTrns;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.common.NumberUtil;
import jp.co.arkinfosys.common.StringUtil;

/**
 * 商品在庫情報を管理するDTOクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class ProductStockInfoDto {

	public Integer aggregateMonthsRange;

	public Short numDecAlignment;

	public Short unitPriceDecAlignment;

	public Short statsDecAlignment;

	public String priceFractCategory;

	public String productFractCategory;

	public String productCode;

	public String productName;

	public String rackCode;

	public String rackName;

	public Integer avgShipCount;

	public Integer maxStockNum;

	public Integer poNum;

	public BigDecimal poLot;

	public BigDecimal supplierPriceYen;

	public BigDecimal supplierPriceDol;

	public BigDecimal retailPrice;

	public Integer leadTime;

	public BigDecimal salesStandardDeviation;

	public Integer mineSafetyStock;

	public Integer entrustSafetyStock;

	public BigDecimal stockQuantityEadUnclosed;

	public BigDecimal stockQuantityEadClosed;

	public BigDecimal stockQuantityEntrustEad;

	public BigDecimal restQuantityRo;

	public BigDecimal restQuantityPoShip;

	public BigDecimal restQuantityPoAir;

	public BigDecimal restQuantityPoDelivery;

	public BigDecimal restQuantityEntrust;

	public Date deliveryDate;

	public BigDecimal restQuantityPo;

	public BigDecimal currentStockQuantity;

	public BigDecimal availableStockQuantity;

	public BigDecimal holdingStockQuantity;

	public BigDecimal holdingStockMonth;

	public BigDecimal roQuantity;

	public BigDecimal salesQuantity;

	public BigDecimal maxSalesQuantity;

	public BigDecimal retailPriceTotal;

	public BigDecimal grossMarginTotal;

	public BigDecimal entrustPoNum;

	public Integer poSlipId;

	public String cUnitSign;

	/**
	 * 標準偏差の文字列表現を返します.
	 *
	 * @return 標準偏差の文字列表現
	 */
	public String getSalesStandardDeviationStr() {
		return this.getStatString(this
				.notNullValue(this.salesStandardDeviation));
	}

	/**
	 * 発注ロットの文字列表現を返します.
	 *
	 * @return　発注ロットの文字列表現
	 */
	public String getPoLotStr() {
		return this.getQuantityString(this.notNullValue(this.poLot));
	}

	/**
	 * 仕入単価(円)の文字列表現を返します.
	 *
	 * @return　仕入単価(円)の文字列表現
	 */
	public String getSupplierPriceYenStr() {
		return this.getPriceYenString(this.notNullValue(this.supplierPriceYen));
	}

	/**
	 * 仕入外貨単価の文字列表現を返します.
	 *
	 * @return　仕入外貨単価の文字列表現
	 */
	public String getSupplierPriceDolStr() {
		return this.getPriceDolString(this.notNullValue(this.supplierPriceDol));
	}

	/**
	 * 売単価の文字列表現を返します.
	 *
	 * @return　売単価の文字列表現
	 */
	public String getRetailPriceStr() {
		return this.getPriceYenString(this.notNullValue(this.retailPrice));
	}

	/**
	 * 委託在庫数量の文字列表現を返します.
	 *
	 * @return　委託在庫数量の文字列表現
	 */
	public String getStockQuantityEntrustEadStr() {
		return this.getQuantityString(this
				.notNullValue(this.stockQuantityEntrustEad));
	}

	/**
	 * 受注残数の文字列表現を返します.
	 *
	 * @return　受注残数の文字列表現
	 */
	public String getRestQuantityRoStr() {
		return this.getQuantityString(this.notNullValue(this.restQuantityRo));
	}

	/**
	 * 船便発注残数の文字列表現を返します.
	 *
	 * @return　船便発注残数の文字列表現
	 */
	public String getRestQuantityPoShipStr() {
		return this.getQuantityString(this
				.notNullValue(this.restQuantityPoShip));
	}

	/**
	 * AIR便発注残数の文字列表現を返します.
	 *
	 * @return　AIR便発注残数の文字列表現
	 */
	public String getRestQuantityPoAirStr() {
		return this
				.getQuantityString(this.notNullValue(this.restQuantityPoAir));
	}

	/**
	 * 宅配便発注残数の文字列表現を返します.
	 *
	 * @return　宅配便発注残数の文字列表現
	 */
	public String getRestQuantityPoDeliveryStr() {
		return this.getQuantityString(this
				.notNullValue(this.restQuantityPoDelivery));
	}

	/**
	 * 委託発注残数の文字列表現を返します.
	 *
	 * @return　委託発注残数の文字列表現
	 */
	public String getRestQuantityEntrustStr() {
		return this.getQuantityString(this
				.notNullValue(this.restQuantityEntrust));
	}

	/**
	 * 最短入荷日を文字列として返します.
	 *
	 * @return　最短入荷日文字列
	 */
	public String getDeliveryDateStr() {
		return StringUtil.getDateString(Constants.FORMAT.DATE,
				this.deliveryDate);
	}

	/**
	 * 受注数量の文字列表現を返します.
	 *
	 * @return　受注数量の文字列表現
	 */
	public String getRoQuantityStr() {
		return this.getQuantityString(this.notNullValue(this.roQuantity));
	}

	/**
	 * 出荷数量の文字列表現を返します.
	 *
	 * @return　出荷数量の文字列表現
	 */
	public String getSalesQuantityStr() {
		return this.getQuantityString(this.notNullValue(this.salesQuantity));
	}

	/**
	 * 月の平均出荷数量の文字列表現を返します.
	 *
	 * @return　月の平均出荷数量の文字列表現
	 */
	private BigDecimal getAvgSalesQuantity() {
		return this.notNullValue(this.salesQuantity).divide(
				new BigDecimal(this.aggregateMonthsRange.intValue() + 1),
				MathContext.DECIMAL64);
	}

	/**
	 * 月の平均出荷数量の文字列表現を返します.
	 *
	 * @return　月の平均出荷数量の文字列表現
	 */
	public String getAvgSalesQuantityStr() {
		return this.getQuantityString(getAvgSalesQuantity());
	}

	/**
	 * 過去最大出荷数(月間)の文字列表現を返します.
	 *
	 * @return　過去最大出荷数(月間)の文字列表現
	 */
	public String getMaxSalesQuantityStr() {
		return this.getQuantityString(this.notNullValue(this.maxSalesQuantity));
	}

	/**
	 * 売上金額の文字列表現を返します.
	 *
	 * @return　売上金額の文字列表現
	 */
	public String getRetailPriceTotalStr() {
		return this.getPriceYenString(this.notNullValue(this.retailPriceTotal));
	}

	/**
	 * 粗利益の文字列表現を返します.
	 *
	 * @return　粗利益の文字列表現
	 */
	public String getGrossMarginTotalStr() {
		return this.getPriceYenString(this.notNullValue(this.grossMarginTotal));
	}

	/**
	 * 商品の現在庫数の文字列表現を返します.
	 *
	 * @return　 商品の現在庫数の文字列表現
	 */
	public String getCurrentStockQuantityStr() {
		return this.getQuantityString(this
				.notNullValue(this.currentStockQuantity));
	}

	/**
	 * 商品の引当可能数の文字列表現を返します.
	 *
	 * @return　商品の引当可能数の文字列表現
	 */
	public String getAvailableStockQuantityStr() {
		return this.getQuantityString(this
				.notNullValue(this.availableStockQuantity));
	}

	/**
	 * 商品の発注残数の文字列表現を返します.
	 *
	 * @return　商品の発注残数の文字列表現
	 */
	public String getRestQuantityPoStr() {
		return this.getQuantityString(this.notNullValue(this.restQuantityPo));
	}

	/**
	 * 商品の在庫保有数の文字列表現を返します.
	 *
	 * @return　商品の在庫保有数の文字列表現
	 */
	public String getHoldingStockQuantityStr() {
		return this.getQuantityString(this
				.notNullValue(this.holdingStockQuantity));
	}

	/**
	 * 商品の在庫保有月数の文字列表現を返します.
	 *
	 * @return　商品の在庫保有月数の文字列表現
	 */
	public String getHoldingStockMonthStr() {
		return this.getStatString(this.holdingStockMonth);
	}

	/**
	 * 商品在庫高を返します.
	 *
	 * @return　商品在庫高
	 */
	private BigDecimal getCurrentStockPrice() {
		return this.notNullValue(this.currentStockQuantity)
				.multiply(this.notNullValue(this.supplierPriceYen),
						MathContext.DECIMAL64);
	}

	/**
	 * 商品在庫高の文字列表現を返します.
	 *
	 * @return　商品在庫高の文字列表現
	 */
	public String getCurrentStockPriceStr() {
		return this.getQuantityString(getCurrentStockPrice());
	}

	/**
	 * 粗利益率を返します.
	 *
	 * @return　粗利益率
	 */
	private BigDecimal getGrossMarginRatio() {
		if (BigDecimal.ZERO.compareTo(this.notNullValue(this.retailPriceTotal)) == 0) {
			return BigDecimal.ZERO;
		}
		return this.notNullValue(this.grossMarginTotal).divide(
				this.retailPriceTotal, MathContext.DECIMAL64);
	}

	/**
	 * 粗利益率の文字列表現を返します.
	 *
	 * @return　粗利益率の文字列表現
	 */
	public String getGrossMarginRatioStr() {
		return this.getStatString(getGrossMarginRatio());
	}

	/**
	 * 在庫回転率を返します.
	 *
	 * @return　在庫回転率
	 */
	private BigDecimal getStockTurnoverRate() {
		BigDecimal currentStockPrice = this.getCurrentStockPrice();
		if (BigDecimal.ZERO.compareTo(currentStockPrice) == 0) {
			return BigDecimal.ZERO;
		}
		return this.notNullValue(this.retailPriceTotal).divide(
				currentStockPrice, MathContext.DECIMAL64);
	}

	/**
	 * 在庫回転率の文字列表現を返します.
	 *
	 * @return　在庫回転率の文字列表現
	 */
	public String getStockTurnoverRateStr() {
		return this.getStatString(this.getStockTurnoverRate());
	}

	/**
	 * 交差比率の文字列表現を返します.
	 *
	 * @return　交差比率の文字列表現
	 */
	public String getGrossMarginRatioToStockStr() {
		return this.getStatString(this.getGrossMarginRatio().multiply(
				this.getStockTurnoverRate(), MathContext.DECIMAL64));
	}

	/**
	 * 委託在庫の発注数を返します.
	 *
	 * @return　委託在庫の発注数
	 */
	public String getEntrustPoNumStr() {
		return this.getQuantityString(this.notNullValue(this.entrustPoNum));
	}

	/**
	 * この商品が補充発注に該当するか否かを返します.
	 *
	 * @return　true:補充発注に該当する false:補充発注に該当しない
	 */
	public boolean isRecommended() {
		if (this.poNum == null) {
			return false;
		}

		if (this.notNullValue(this.holdingStockQuantity).compareTo(
				new BigDecimal(this.poNum)) <= 0) {
			return true;
		}
		return false;
	}

	/**
	 * この商品が [保有数＜発注点] に該当するか否かを返します.
	 *
	 * @return　true:保有数＜発注点　false:保有数≧発注点
	 */
	public boolean isHoldingStockQuantityLessPoNum() {
		if (this.poNum == null) {
			return false;
		}

		if (this.notNullValue(this.holdingStockQuantity).compareTo(
				new BigDecimal(this.poNum)) < 0) {
			return true;
		}
		return false;
	}

	/**
	 * この商品が [現在庫数＜発注点] に該当するか否かを返します.
	 *
	 * @return　true：現在庫数＜発注点　false:現在庫数≧発注点
	 */
	public boolean isCurrentStockQuantityLessPoNum() {
		if (this.poNum == null) {
			return false;
		}

		if (this.notNullValue(this.availableStockQuantity).compareTo(
				new BigDecimal(this.poNum)) < 0) {
			return true;
		}
		return false;
	}

	/**
	 * この商品が [引当可能数＜発注点（発注点がゼロのものは除く）] に該当するか否かを返します.
	 *
	 * @return true: 引当可能数＜発注点（発注点がゼロのものは除く）<BR>
	 *         false:引当可能数≧発注点（発注点がゼロのものは除く）
	 */
	public boolean isAvailableStockQuantityLessPoNum() {
		if (this.poNum == null || this.poNum.intValue() == 0) {
			return false;
		}

		if (this.notNullValue(this.availableStockQuantity).compareTo(
				new BigDecimal(this.poNum)) < 0) {
			return true;
		}
		return false;
	}

	/**
	 * この商品が [引当可能数＋発注残数＜発注点（発注点がゼロのものは除く）] に該当するか否かを返します.
	 *
	 * @return true: 引当可能数＋発注残数＜発注点（発注点がゼロのものは除く）<BR>
	 *         false:引当可能数＋発注残数≧発注点（発注点がゼロのものは除く）
	 */
	public boolean isAvailableStockQuantityAndRestQuantityPoLessPoNum() {
		if (this.poNum == null || this.poNum.intValue() == 0) {
			return false;
		}

		if (this.notNullValue(this.availableStockQuantity).add(
				this.notNullValue(this.restQuantityPo)).compareTo(
				new BigDecimal(this.poNum)) < 0) {
			return true;
		}
		return false;
	}

	/**
	 * この商品が [引当可能数 targetQuantity 個以下] に該当するか否かを返します.
	 *
	 * @param targetQuantity　比較対象の数量
	 * @return true: 引当可能数≦targetQuantity
	 *         false:引当可能数＞targetQuantity
	 */
	public boolean isAvailableStockQuantityLess(int targetQuantity) {
		if (this.notNullValue(this.availableStockQuantity).compareTo(
				new BigDecimal(targetQuantity)) <= 0) {
			return true;
		}
		return false;
	}

	/**
	 * この商品が 最大在庫保有数超過 に該当するか否かを返します.
	 *
	 * @return　true：最大在庫保有数を超過している　false:最大在庫保有数を超過していない
	 */
	public boolean isOverMaxHoldingQuantity() {
		if (this.maxStockNum == null) {
			return false;
		}

		if (this.notNullValue(this.holdingStockQuantity).compareTo(
				new BigDecimal(this.maxStockNum)) > 0) {
			return true;
		}
		return false;
	}

	/**
	 * BigDecimal値を受け取り、NULLであれば0のBigDecimalを、そうでなければ元のオブジェクトを返します.
	 *
	 * @param value　BigDecimal値
	 * @return　0のBigDecimal または　value
	 */
	private BigDecimal notNullValue(BigDecimal obj) {
		if (obj != null) {
			return obj;
		}
		return BigDecimal.ZERO;
	}

	/**
	 * 商品数量小数桁、商品数量端数処理を適用した文字列を返します.
	 *
	 * @param obj　BigDecimal値
	 * @return　商品数量小数桁、商品数量端数処理を適用した文字列
	 */
	private String getQuantityString(BigDecimal obj) {
		if (obj == null) {
			return "";
		}
		DecimalFormat df = NumberUtil.createDecimalFormat(
				this.productFractCategory, this.numDecAlignment, true);
		return df.format(obj);
	}

	/**
	 * 円単価小数桁、単価端数処理を適用した文字列を返します.
	 *
	 * @param obj　BigDecimal値
	 * @return　円単価小数桁、単価端数処理を適用した文字列
	 */
	private String getPriceYenString(BigDecimal obj) {
		if (obj == null) {
			return "";
		}
		DecimalFormat df = NumberUtil.createDecimalFormat(
				this.priceFractCategory, 0, true);
		return df.format(obj);
	}

	/**
	 * 外貨単価小数桁、単価端数処理を適用した文字列を返します.
	 *
	 * @param obj　BigDecimal値
	 * @return　外貨単価小数桁、単価端数処理を適用した文字列
	 */
	private String getPriceDolString(BigDecimal obj) {
		if (obj == null) {
			return "";
		}
		DecimalFormat df = NumberUtil.createDecimalFormat(
				this.priceFractCategory, this.unitPriceDecAlignment, true);
		return df.format(obj);
	}

	/**
	 * 統計小数桁を適用した文字列を返します.
	 *
	 * @param obj　BigDecimal値
	 * @return　統計小数桁を適用した文字列
	 */
	private String getStatString(BigDecimal obj) {
		if (obj == null) {
			return "";
		}
		DecimalFormat df = NumberUtil.createDecimalFormat(
				CategoryTrns.FLACT_CATEGORY_HALF_UP, this.statsDecAlignment,
				false);
		return df.format(obj);
	}
}
