/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import jp.co.arkinfosys.common.CategoryTrns;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.entity.Customer;
import jp.co.arkinfosys.entity.DepositLine;
import jp.co.arkinfosys.entity.SalesLineTrn;

/**
 * 請求書と売掛残高のサービスクラスです.
 *
 *
 * @author Ark Information Systems
 */
public class BillAndArtService extends AbstractService {


	private SimpleDateFormat DF_YMD = new SimpleDateFormat(Constants.FORMAT.DATE);


	/**
	 * 請求書に設定する入金額を計算して返します.
	 * @param depositLineList 入金伝票明細行リスト
	 * @return 入金額
	 */
	public BigDecimal getDepositPrice(List<DepositLine> depositLineList) {
		Double depositPrice = 0.0;
		for( DepositLine dl : depositLineList ){
			if( dl.price != null ){
				depositPrice = depositPrice + dl.price.doubleValue();
			}
		}
		return new BigDecimal(depositPrice);
	}
	/**
	 * 請求書に設定する売上額を計算して返します.
	 * @param salesLineList 売上伝票明細行リスト
	 * @return 売上金額
	 */
	public BigDecimal getSalesPrice(List<SalesLineTrn> salesLineList) {
		Double salesPrice = 0.0;
		for( SalesLineTrn sl : salesLineList ){
			if( sl.retailPrice != null ){
//				if( SalesLineService.isEtcPriceCode(sl.productCode) == false ){
					salesPrice = salesPrice + sl.retailPrice.doubleValue();
//				}
			}
		}
		return new BigDecimal(salesPrice);
	}

	/**
	 * 請求書に設定するその他金額を計算して返します.<br>
	 * 未使用です.
	 * @param salesLineList 売上伝票明細行リスト
	 * @return null
	 */
	public BigDecimal getEtcPrice(List<SalesLineTrn> salesLineList) {
//		Double salesPrice = 0.0;
//		for( SalesLineTrn sl : salesLineList ){
//			if( sl.retailPrice != null ){
//				if( SalesLineService.isEtcPriceCode(sl.productCode) == true ){
//					salesPrice = salesPrice + sl.retailPrice.doubleValue();
//				}
//			}
//		}
//		return new BigDecimal(salesPrice);
		return null;
	}

	/**
	 * 請求書に設定する消費税を計算して返します.<br>
	 * 区分コード毎に計算方法が異なります.
	 * @param customer 顧客マスタエンティティ
	 * @param salesLineList 売上伝票明細行リスト
	 * @return 消費税金額
	 */
	public BigDecimal getCTaxPrice(Customer customer, List<SalesLineTrn> salesLineList) {
		Double ctaxPrice = 0.0;

//		Double rateBase = 1.0;	// 内税計算用

		if( CategoryTrns.TAX_SHIFT_CATEGORY_INCLUDE_CTAX.equals(customer.taxShiftCategory)) {
			// 区分名：税転嫁、区分コード名：内税

			// 内税は消費税なし
//			for( SalesLineTrn sl : salesLineList ){
//				if( sl.retailPrice != null ){
//					// 課税区分を確認
//					String taxCategory = SalesService.checkTaxCategory(sl.taxCategory);
//					if( CategoryTrns.TAX_CATEGORY_FREE.equals( taxCategory) ){
//						// 免税
//					}else if( CategoryTrns.TAX_CATEGORY_IMPOSITION.equals( taxCategory)){
//						// 外税
//						ctaxPrice = ctaxPrice + sl.ctaxPrice.doubleValue();
//					}else if( CategoryTrns.TAX_CATEGORY_INCLUDED.equals( taxCategory)){
//						// 内税
//						ctaxPrice = ctaxPrice +
//						( sl.retailPrice.doubleValue()/( rateBase + sl.ctaxRate.doubleValue() ));
//					}
//				}
//			}

		}else if( CategoryTrns.TAX_SHIFT_CATEGORY_SLIP_TOTAL.equals(customer.taxShiftCategory)){
			// 区分名：税転嫁、区分コード名：外税伝票計
			HashMap<Double,Double> ctaxMap = new HashMap<Double, Double>();

			// 伝票ごとの消費税額を集計
			if( salesLineList.size() > 0 ){
				SalesLineTrn slTmp = salesLineList.get(0);
				for( SalesLineTrn sl : salesLineList ){
					if( sl.salesSlipId.equals(slTmp.salesSlipId) == false ){
						// 税率ごとの消費税を集計
						ctaxPrice += getTax( ctaxMap, customer.taxFractCategory );
						ctaxMap.clear();
					}
					if( sl.retailPrice != null ){
						Double price = ctaxMap.get(sl.ctaxRate.doubleValue());
						// 課税対象額を取得
						Double thisPrice = getForTaxPrice( sl );
						if( price == null ){
							price = thisPrice;
						}else{
							price = price + thisPrice;
						}
						ctaxMap.put(sl.ctaxRate.doubleValue(), price);
					}

					slTmp = sl;
				}
			}
			ctaxPrice += getTax( ctaxMap, customer.taxFractCategory );

		}else if( CategoryTrns.TAX_SHIFT_CATEGORY_CLOSE_THE_BOOKS.equals(customer.taxShiftCategory)){
			// 区分名：税転嫁、区分コード名：外税締単位
			HashMap<Double,Double> ctaxMap = new HashMap<Double, Double>();

			// 税率ごとの売上金額を集計
			for( SalesLineTrn sl : salesLineList ){
				if( sl.retailPrice != null ){
					Double price = ctaxMap.get(sl.ctaxRate.doubleValue());
					// 課税対象額を取得
					Double thisPrice = getForTaxPrice( sl );
					if( price == null ){
						price = thisPrice;
					}else{
						price = price + thisPrice;
					}
					ctaxMap.put(sl.ctaxRate.doubleValue(), price);
				}
			}
			// 税率ごとの消費税を集計
			ctaxPrice = getTax( ctaxMap, customer.taxFractCategory );
		}
		return new BigDecimal(ctaxPrice);
	}

	/**
	 * 対象明細の課税対象金額を取得します.
	 * @param sl 売上伝票明細行エンティティ
	 * @return 課税対象金額
	 */
	public Double getForTaxPrice( SalesLineTrn sl ) {
		Double rateBase = 1.0;	// 内税計算用
		Double thisPrice = 0.0;
		// 課税区分を確認
		if( CategoryTrns.TAX_CATEGORY_FREE.equals(sl.taxCategory) ){
			// 免税
		}else if( CategoryTrns.TAX_CATEGORY_IMPOSITION.equals(sl.taxCategory)){
			// 外税
			thisPrice = sl.retailPrice.doubleValue();
		}else if( CategoryTrns.TAX_CATEGORY_INCLUDED.equals(sl.taxCategory)){
			// 内税
			thisPrice =
				( sl.retailPrice.doubleValue()/( rateBase + sl.ctaxRate.doubleValue() ));
		}
		return thisPrice;
	}

	/**
	 * 税率ごとの消費税を集計して返します.
	 * @param ctaxMap 税率マップ
	 * @param taxFractCategory 税端数処理
	 * @return 集計された消費税額
	 */
	public Double getTax( HashMap<Double,Double> ctaxMap, String taxFractCategory ) {
		Double tax = 0.0;
		Set<Entry<Double,Double>> entrySet = ctaxMap.entrySet();     //すべてのvalue
		Iterator<Entry<Double, Double>> entryIte = entrySet.iterator();
		while(entryIte.hasNext()) {              //ループ
			Map.Entry<Double, Double> ent = entryIte.next();        //key=value
			if( ent.getKey() != null ){
				// 税率×金額
				tax = tax + ( ent.getValue() * ( ent.getKey() / 100.0 ) );	// key=税率は％表記なので100.0で割る
			}
		}
		BigDecimal bd = new BigDecimal( tax );
		Double retVal = 0.0;
		if( CategoryTrns.FLACT_CATEGORY_DOWN.equals( taxFractCategory )){
			retVal = bd.setScale(0,BigDecimal.ROUND_DOWN).doubleValue();
		}else if( CategoryTrns.FLACT_CATEGORY_HALF_UP.equals( taxFractCategory )){
			retVal = bd.setScale(0,BigDecimal.ROUND_UP).doubleValue();
		}else{
			retVal = bd.setScale(0,BigDecimal.ROUND_HALF_UP).doubleValue();
		}
		return retVal;
	}

	/**
	 * 回収予定日を返します.
	 *
	 * @param customer 顧客マスタエンティティ
	 * @param closeDate 締日
	 * @return 回収予定日
	 * @throws ParseException
	 */
	public Date getPayBackPlanDate(Customer customer, String closeDate)
		throws ParseException {

		Calendar cal = GregorianCalendar.getInstance();

		if( CategoryTrns.PAYBACK_CYCLE_CATEGORY_ETC.equals( customer.paybackCycleCategory) ){
			return null;
		}
		Date tmpDate = DF_YMD.parse(closeDate);
		cal.setTime(tmpDate);
		Integer cycle = Integer.parseInt(customer.paybackCycleCategory);
		cal.add(Calendar.MONTH,cycle);
		Integer day = Integer.parseInt(customer.cutoffGroup);
		switch (day) {
		case 10:
			cal.set(Calendar.DAY_OF_MONTH, 10);
			break;
		case 20:
			cal.set(Calendar.DAY_OF_MONTH, 20);
			break;
		case 25:
			cal.set(Calendar.DAY_OF_MONTH, 1);
			cal.add(Calendar.MONTH,1);	// 月末
			cal.add(Calendar.DATE,-1);
			break;
		case 31:
			switch (cycle) {
			case 1:	// 翌月
				cal.set(Calendar.DAY_OF_MONTH, 1);
				cal.add(Calendar.MONTH,1);	// 月末
				cal.add(Calendar.DATE,-1);
				break;
			case 2:	// 翌々月
				cal.set(Calendar.DAY_OF_MONTH, 5);
				break;
			default:
				return null;
			}
			break;

		default:
			return null;
		}
		return cal.getTime();
	}

}
