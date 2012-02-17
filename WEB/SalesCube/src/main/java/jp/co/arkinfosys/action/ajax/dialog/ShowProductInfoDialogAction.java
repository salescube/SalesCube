/*
 *  Copyright 2009-2010 Ark Information Systems.
 */

package jp.co.arkinfosys.action.ajax.dialog;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import javax.annotation.Resource;

import jp.co.arkinfosys.action.ajax.CommonAjaxResources;
import jp.co.arkinfosys.common.CategoryTrns;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.common.MessageResourcesUtil;
import jp.co.arkinfosys.common.NumberUtil;
import jp.co.arkinfosys.common.StringUtil;
import jp.co.arkinfosys.dto.master.ProductInfosWithNameDto;
import jp.co.arkinfosys.entity.join.ProductJoin;
import jp.co.arkinfosys.entity.join.SupplierJoin;
import jp.co.arkinfosys.form.ajax.dialog.ShowProductInfoDialogForm;
import jp.co.arkinfosys.service.ProductService;
import jp.co.arkinfosys.service.SupplierService;

import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;

/**
 * 商品情報ダイアログを表示するアクションクラスです.<br>
 * 発注入力画面の「情報」ボタンから呼び出され、商品の内容を一覧表示します.
 *
 * @author Ark Information Systems
 *
 */
public class ShowProductInfoDialogAction extends CommonAjaxResources {

	/**
	 * アクションフォームです.
	 */
	@ActionForm
	@Resource
	public ShowProductInfoDialogForm showProductInfoDialogForm;

	/**
	 * 商品マスタに対するサービスクラスです.
	 */
	@Resource
	public ProductService productService;

	/**
	 * 仕入マスタに対するサービスクラスです.
	 */
	@Resource
	public SupplierService supplierService;

	/**
	 * 商品情報を格納するDTOクラスです.
	 */
	public ProductInfosWithNameDto productInfosWithNameDto;

	/**
	 * 数値文字列を￥記号付きの金額表示文字列に変換します.<br>
	 * 3桁毎にカンマが挿入され、文字列の先頭には￥記号が付与されます.
	 *
	 * @param l_string 数値文字列
	 * @return ￥記号付き金額文字列
	 */
	private String convertToYenNotation(String l_string) {
		String temp = "";
		
		DecimalFormat df = NumberUtil.createDecimalFormat(
				super.mineDto.priceFractCategory, 0, true);
		try {
			temp = df.format(new BigDecimal(l_string));
			if (temp.length() != 0) {
				temp = MessageResourcesUtil
						.getMessage("words.unit.japaneseYen")
						+ " " + temp;
			}
		} catch (Exception e) {
		}
		return temp;
	}

	/**
	 * 数値文字列を外貨金額表示文字列に変換します.<br>
	 * 数値は自社マスタで設定する単価小数桁数の桁まで、同じく自社マスタで設定する単価端数処理に従って丸められます.また、整数部には3桁毎にカンマが挿入されます.
	 *
	 * @param l_string 数値文字列
	 * @param l_cunit 外貨記号
	 * @return 外貨金額表示文字列
	 */
	private String convertToDolNotation(String l_string, String l_cunit) {
		String temp = "";
		
		DecimalFormat df = NumberUtil.createDecimalFormat(
				super.mineDto.priceFractCategory,
				super.mineDto.unitPriceDecAlignment, true);
		try {
			temp = df.format(new BigDecimal(l_string));
			if (temp.length() != 0) {
				temp = l_cunit + " " + temp;
			}
		} catch (Exception e) {
		}
		return temp;
	}

	/**
	 * 数値文字列を数量形式の数値文字列に変換します.<br>
	 * 数値は自社マスタで設定する数量小数桁数の桁まで、同じく自社マスタで設定する数量端数処理に従って丸められます.また、整数部には3桁毎にカンマが挿入されます.
	 *
	 * @param l_string 数値文字列
	 * @return 数量形式の数値文字列
	 */
	private String convertToQuaNotation(String l_string) {
		String temp = "";
		
		DecimalFormat df = NumberUtil.createDecimalFormat(
				super.mineDto.productFractCategory,
				super.mineDto.numDecAlignment, true);
		try {
			temp = df.format(new BigDecimal(l_string));
		} catch (Exception e) {
		}
		return temp;
	}

	/**
	 * 数値文字列を統計形式の数値文字列に変換します.<br>
	 * 数値は自社マスタで設定する統計小数桁数の桁まで四捨五入されます.また、整数部には3桁毎にカンマが挿入されます.
	 *
	 * @param l_string 数値文字列
	 * @return 統計形式の数値文字列
	 */
	private String convertToStaNotation(String l_string) {
		String temp = "";
		
		DecimalFormat df = NumberUtil.createDecimalFormat(
				CategoryTrns.FLACT_CATEGORY_HALF_UP,
				super.mineDto.statsDecAlignment, true);
		try {
			temp = df.format(new BigDecimal(l_string));
		} catch (Exception e) {
		}
		return temp;
	}

	/**
	 * 整数値文字列を3桁毎のカンマ付き整数値文字列に変換します.
	 *
	 * @param l_string 整数値文字列
	 * @return カンマ付き整数値
	 */
	private String convertToIntNotation(String l_string) {
		String temp = "";
		
		DecimalFormat df = NumberUtil.createDecimalFormat(
				CategoryTrns.FLACT_CATEGORY_HALF_UP, 0, true);
		try {
			temp = df.format(new BigDecimal(l_string));
		} catch (Exception e) {
		}
		return temp;
	}

	/**
	 * 数値文字列を、記号を含む全体の桁数を制限してカンマ付き数値文字列に変換します.<br>
	 * 例えばl_stringが"30"、l_digitが6の場合戻り値は"30.000"となります.また、l_stringが"3000"、l_digitが6の場合戻り値は"3,000"となります.<br>
	 * 整数部へのカンマ付与の方が小数部の追加よりも優先度が高いため、必ずしも指定した桁数の文字列が戻されるとは限らない事に注意が必要です.
	 *
	 * @param l_string 数値文字列
	 * @param l_digit 制限桁数
	 * @return カンマ付き数値文字列
	 */
	private String convertToLimitedDigitIntNotation(String l_string, int l_digit) {
		String temp = "";
		
		DecimalFormat df = NumberUtil.createDecimalFormat(
				CategoryTrns.FLACT_CATEGORY_HALF_UP, 0, true);
		try {
			int intLength = df.format(new BigDecimal(l_string)).length();

			if (intLength < (l_digit - 1)) {
				df = NumberUtil.createDecimalFormat(
						CategoryTrns.FLACT_CATEGORY_HALF_UP,
						(l_digit - 1 - intLength), true);
			}

			temp = df.format(new BigDecimal(l_string));
		} catch (Exception e) {
		}
		return temp;
	}

	/**
	 * 商品情報ダイアログの表示処理メソッドです.
	 *
	 * @return 商品情報ダイアログのJSPパス
	 * @throws Exception 例外発生時
	 */
	@Execute(validator = false, urlPattern = "showDialog/{dialogId}")
	public String showDialog() throws Exception {

		productInfosWithNameDto = new ProductInfosWithNameDto();
		ProductJoin product;

		if (!StringUtil.hasLength(showProductInfoDialogForm.productCode)) {
			return null;
		}

		try {
			product = this.productService
					.findProductInfosWithNamesByCode(showProductInfoDialogForm.productCode);
			if (product == null) {
				return null;
			}
			Beans.copy(product, productInfosWithNameDto).dateConverter(
					Constants.FORMAT.DATE).execute();

			
			SupplierJoin supplier = this.supplierService
					.findById(product.supplierCode);
			String cUnitStr = (supplier == null ? ""
					: (supplier.cUnitSign == null ? "" : supplier.cUnitSign));

			
			productInfosWithNameDto.supplierPriceYen = convertToYenNotation(productInfosWithNameDto.supplierPriceYen);
			
			productInfosWithNameDto.supplierPriceDol = convertToDolNotation(
					productInfosWithNameDto.supplierPriceDol, cUnitStr);
			
			productInfosWithNameDto.retailPrice = convertToYenNotation(productInfosWithNameDto.retailPrice);

			
			productInfosWithNameDto.packQuantity = convertToQuaNotation(productInfosWithNameDto.packQuantity);
			
			productInfosWithNameDto.avgShipCount = convertToQuaNotation(productInfosWithNameDto.avgShipCount);
			
			productInfosWithNameDto.poNum = convertToQuaNotation(productInfosWithNameDto.poNum);
			
			productInfosWithNameDto.poLot = convertToQuaNotation(productInfosWithNameDto.poLot);
			
			productInfosWithNameDto.maxStockNum = convertToQuaNotation(productInfosWithNameDto.maxStockNum);
			
			productInfosWithNameDto.maxPoNum = convertToQuaNotation(productInfosWithNameDto.maxPoNum);
			
			productInfosWithNameDto.roMaxNum = convertToQuaNotation(productInfosWithNameDto.roMaxNum);

			
			productInfosWithNameDto.soRate = convertToStaNotation(productInfosWithNameDto.soRate);

			
			productInfosWithNameDto.leadTime = convertToIntNotation(productInfosWithNameDto.leadTime);
			
			productInfosWithNameDto.coreNum = convertToIntNotation(productInfosWithNameDto.coreNum);

			
			productInfosWithNameDto.weight = convertToLimitedDigitIntNotation(
					productInfosWithNameDto.weight, 6);
			
			productInfosWithNameDto.length = convertToLimitedDigitIntNotation(
					productInfosWithNameDto.length, 6);
			
			productInfosWithNameDto.width = convertToLimitedDigitIntNotation(
					productInfosWithNameDto.width, 6);
			
			productInfosWithNameDto.depth = convertToLimitedDigitIntNotation(
					productInfosWithNameDto.depth, 6);
			
			productInfosWithNameDto.height = convertToLimitedDigitIntNotation(
					productInfosWithNameDto.height, 6);

			return "dialog.jsp";
		} catch (Exception e) {
			super.errorLog(e);
			super.writeSystemErrorToResponse();
			return null;
		}

	}

}
