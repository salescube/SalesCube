/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.dto.master;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.persistence.Column;

import jp.co.arkinfosys.common.CategoryTrns;
import jp.co.arkinfosys.common.ConfigUtil;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.common.StringUtil;
import jp.co.arkinfosys.common.ValidateUtil;
import jp.co.arkinfosys.entity.join.ProductJoin;
import jp.co.arkinfosys.service.ProductService;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.PropertyDesc;
import org.seasar.framework.beans.factory.BeanDescFactory;
import org.seasar.struts.util.MessageResourcesUtil;

/**
 * 商品マスタ検索画面のExcel入出力データを管理するDTOクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class ProductExcelRowDto implements Serializable {

	private static final long serialVersionUID = 1L;

	public int lineNo;

	public String productCode;

	public String productName;

	public String productKana;

	public String onlinePcode;

	public String supplierPcode;

	public String supplierCode;

	public String rackCode;

	public String supplierPriceYen;

	public String supplierPriceDol;

	public String retailPrice;

	public String soRate;

	public String unitCategory;

	public String packQuantity;

	public String janPcode;

	public String width;

	public String widthUnitSizeCategory;

	public String depth;

	public String depthUnitSizeCategory;

	public String height;

	public String heightUnitSizeCategory;

	public String weight;

	public String weightUnitSizeCategory;

	public String length;

	public String lengthUnitSizeCategory;

	public String poLot;

	public String lotUpdFlag;

	public String leadTime;

	public String poNum;

	public String poUpdFlag;

	public String mineSafetyStock;

	public String mineSafetyStockUpdFlag;

	public String entrustSafetyStock;

	public String salesStandardDeviation;

	public String avgShipCount;

	public String maxStockNum;

	public String stockUpdFlag;

	public String termShipNum;

	public String maxPoNum;

	public String maxPoUpdFlag;

	public String fractCategory;

	public String taxCategory;

	public String stockCtlCategory;

	public String stockAssesCategory;

	public String productCategory;

	public String product1;

	public String product2;

	public String product3;

	public String roMaxNum;

	public String productRank;

	public String setTypeCategory;

	public String productStatusCategory;

	public String productStockCategory;

	public String productPurvayCategory;

	public String productStandardCategory;

	public String coreNum;

	public String num1;

	public String num2;

	public String num3;

	public String num4;

	public String num5;

	public String dec1;

	public String dec2;

	public String dec3;

	public String dec4;

	public String dec5;

	public String discardDate;

	public String remarks;

	public String eadRemarks;

	public String commentData;

	public String lastRoDate;

	public String discountId;

	private static final String prefix = "labels.product.csv.";

	private static final String STR_STYLE = "@";

	private static HashMap<String, Class<?>> propertyClassMap = new HashMap<String, Class<?>>();

	private static String[] outputPropertyNames = ProductExcelRowDto
			.getEntityPropertyNames();

	/**
	 * プロパティ名の文字列配列を返します.
	 * @return　 プロパティ名の文字列配列
	 */
	private static String[] getEntityPropertyNames() {
		String val = (String) ConfigUtil
				.getConfigValue(ConfigUtil.KEY.PRODUCT_CSV_COLUMNS);
		if (!StringUtil.hasLength(val)) {
			return new String[0];
		}

		String[] columns = val.split(",");
		if (columns == null || columns.length == 0) {
			return new String[0];
		}

		// 空白を除去する
		for (int i = 0; i < columns.length; i++) {
			columns[i] = StringUtil.trimBlank(columns[i]);
		}

		// エンティティクラスのプロパティ名に変換して返却する
		BeanDesc beanDesc = BeanDescFactory.getBeanDesc(ProductJoin.class);
		List<String> propertyNameList = new ArrayList<String>();
		for (int i = 0; i < columns.length; i++) {
			PropertyDesc propertyDesc = beanDesc.getPropertyDesc(columns[i]
					.replace("_", ""));
			if (propertyDesc == null) {
				// 変換できない場合は除外
				continue;
			}
			propertyNameList.add(propertyDesc.getPropertyName());
			propertyClassMap.put(propertyDesc.getPropertyName(), propertyDesc
					.getField().getType());
		}

		return (String[]) propertyNameList.toArray(new String[0]);
	}

	/**
	 * Excelにヘッダ文字列を書き込みます.
	 * @param　row　Excel行
	 */
	public static HSSFRow writeHeaderLine(HSSFRow row) {
		if (row == null) {
			return null;
		}

		String[] propertyNames = ProductExcelRowDto.outputPropertyNames;
		if (propertyNames == null) {
			return row;
		}

		for (short i = 0; i < propertyNames.length; i++) {
			HSSFCell c = row.createCell(i);
			c.setCellType(HSSFCell.CELL_TYPE_STRING);
			c.getCellStyle().setDataFormat(
					HSSFDataFormat.getBuiltinFormat(STR_STYLE));

			c.setCellValue(new HSSFRichTextString(
					getHeaderLabel(propertyNames[i])));
		}

		return row;
	}

	/**
	 * 接頭語「商品」を付けたプロパティ名を返します.
	 * @param　propertyName　プロパティ名
	 * @return　メッセージ　接頭語「商品」を付けたプロパティ名
	 */
	private static String getHeaderLabel(String propertyName) {
		return MessageResourcesUtil.getMessage(prefix + propertyName);
	}

	/**
	 * DTOの内容をExcelの行に出力します.
	 * @param　row　Excel行
	 * @return　Excel行
	 */
	public HSSFRow writeRow(HSSFRow row) {
		if (row == null) {
			return null;
		}

		String[] propertyNames = ProductExcelRowDto.outputPropertyNames;
		if (propertyNames == null) {
			return row;
		}

		for (short i = 0; i < propertyNames.length; i++) {
			HSSFCell c = row.createCell(i);
			c.setCellType(HSSFCell.CELL_TYPE_STRING);
			c.getCellStyle().setDataFormat(
					HSSFDataFormat.getBuiltinFormat(STR_STYLE));
			try {
				Field f = this.getClass().getField(propertyNames[i]);
				if (f == null) {
					continue;
				}
				HSSFRichTextString str = new HSSFRichTextString((String) f
						.get(this));

				c.setCellValue(str);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return row;
	}

	/**
	 * Excelの行データをProductExcelDtoに設定します.
	 * @param row
	 * @throws IllegalAccessException
	 */
	public void loadRow(HSSFRow row) throws IllegalAccessException {
		if (row == null) {
			return;
		}

		String[] propertyNames = ProductExcelRowDto.outputPropertyNames;
		if (propertyNames == null) {
			return;
		}

		for (short i = 0; i < propertyNames.length; i++) {
			HSSFCell c = row.getCell(i);
			if (c == null) {
				continue;
			}

			Class<?> targetClass = propertyClassMap.get(propertyNames[i]);

			Field dtoField = null;
			try {
				dtoField = this.getClass().getField(propertyNames[i]);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (dtoField == null) {
				continue;
			}

			// Excel値を文字列として取得する
			String value = null;
			int type = c.getCellType();
			if (type == HSSFCell.CELL_TYPE_STRING) {
				value = c.getRichStringCellValue().getString();
			} else if (type == HSSFCell.CELL_TYPE_NUMERIC) {
				if (HSSFDateUtil.isCellDateFormatted(c)) {
					dtoField.set(this, c.getDateCellValue());
				} else {
					double dValue = c.getNumericCellValue();
					if (Double.compare(dValue, Math.floor(dValue)) == 0) {
						// 小数桁に値がない場合は整数として扱う(フラグなどが1.0として取得される場合の対処)
						value = String.valueOf((long) dValue);
					} else {
						value = String.valueOf(c.getNumericCellValue());
					}
				}
			} else if (type == HSSFCell.CELL_TYPE_FORMULA) {
				value = c.getRichStringCellValue().getString();
				if (!StringUtil.hasLength(value)) {
					value = String.valueOf(c.getNumericCellValue());
				}
			} else if (type == HSSFCell.CELL_TYPE_BOOLEAN) {
				value = String.valueOf(c.getBooleanCellValue());
			}

			if (!StringUtil.hasLength(value)) {
				// 空白値の場合、エンティティが文字型であれば空文字とする
				if (targetClass == String.class) {
					value = "";
				} else {
					continue;
				}
			}

			dtoField.set(this, value);
		}
	}

	/**
	 * 読み込んだExcelデータのバリデートを行います.
	 * @return　表示するメッセージ
	 * @throws IllegalAccessException
	 */
	public ActionMessages validate() throws IllegalAccessException {
		ActionMessages messages = new ActionMessages();

		String[] propertyNames = ProductExcelRowDto.outputPropertyNames;
		if (propertyNames == null) {
			return messages;
		}

		// 必須入力チェック
		this.validateRequired(messages, this.productCode,
				ProductService.Param.PRODUCT_CODE);
		this.validateRequired(messages, this.productName,
				ProductService.Param.PRODUCT_NAME);
		this.validateRequired(messages, this.stockCtlCategory,
				ProductService.Param.STOCK_CTL_CATEGORY);

		// 単品の場合の必須チェック
		if (CategoryTrns.PRODUCT_SET_TYPE_SINGLE.equals(this.setTypeCategory)) {
			this.validateRequired(messages, this.supplierCode,
					ProductService.Param.SUPPLIER_CODE);
			this.validateRequired(messages, this.rackCode,
					ProductService.Param.RACK_CODE);
		}

		// 在庫管理区分
		if (CategoryTrns.PRODUCT_STOCK_CTL_YES.equals(this.stockCtlCategory)) {
			if (CategoryTrns.PRODUCT_SET_TYPE_SET.equals(this.setTypeCategory)) {
				// 在庫管理するセット商品は許可しない
				messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.line.product.stockctl.set", this.lineNo + 1));
			} else {
				this.validateRequired(messages, this.leadTime,
						ProductService.Param.LEAD_TIME);
				this.validateRequired(messages, this.poNum,
						ProductService.Param.PO_NUM);
				this.validateRequired(messages, this.mineSafetyStock,
						ProductService.Param.MINE_SAFETY_STOCK);
				this.validateRequired(messages, this.poLot,
						ProductService.Param.PO_LOT);
				this.validateRequired(messages, this.maxPoNum,
						ProductService.Param.MAX_PO_NUM);
				this.validateRequired(messages, this.maxStockNum,
						ProductService.Param.MAX_STOCK_NUM);
			}
		}

		Field dtoField = null;
		Field entityField = null;
		for (int i = 0; i < propertyNames.length; i++) {
			if (messages.size() > 10) {
				return messages;
			}

			Class<?> targetClass = propertyClassMap.get(propertyNames[i]);
			try {
				dtoField = this.getClass().getField(propertyNames[i]);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (dtoField == null) {
				continue;
			}

			try {
				entityField = ProductJoin.class.getField(propertyNames[i]);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (entityField == null) {
				continue;
			}

			String value = (String) dtoField.get(this);
			if (!StringUtil.hasLength(value)) {
				// 空の場合は不要
				continue;
			}

			if (targetClass == String.class) {
				// サイズチェック
				Column column = entityField.getAnnotation(Column.class);
				this.validateMaxlength(messages, value, column.length(),
						propertyNames[i]);
			} else if (targetClass == BigDecimal.class) {
				this.validateBigDecimal(messages, value, propertyNames[i]);
			} else if (targetClass == Short.class) {
				this.validateShort(messages, value, propertyNames[i]);
			} else if (targetClass == Float.class) {
				this.validateFloat(messages, value, propertyNames[i]);
			} else if (targetClass == Integer.class) {
				this.validateInteger(messages, value, propertyNames[i]);
			} else if (targetClass == Date.class) {
				this.validateDate(messages, value, propertyNames[i]);
			}
		}

		return messages;
	}

	/**
	 * 必須チェックを行います.
	 * @para　messages　メッセージオブジェクト
	 * @param value　検査する値
	 * @param propertyName 項目名称
	 */
	private void validateRequired(ActionMessages messages, String value,
			String propertyName) {
		ActionMessage message = ValidateUtil.required(value,
				"errors.line.required", new Object[] {
						this.lineNo + 1,
						MessageResourcesUtil.getMessage("labels.product.csv."
								+ propertyName) });
		if (message != null) {
			messages.add(ActionMessages.GLOBAL_MESSAGE, message);
		}
	}

	/**
	 * 文字列長さチェックを行います.
	 * @param　messages　メッセージオブジェクト
	 * @param value　検査する値
	 * @param length 長さ
	 * @param propertyName 項目名称
	 */
	private void validateMaxlength(ActionMessages messages, String value,
			int length, String propertyName) {
		ActionMessage message = ValidateUtil.maxlength(value, length,
				"errors.line.maxlength", new Object[] {
						this.lineNo + 1,
						MessageResourcesUtil.getMessage("labels.product.csv."
								+ propertyName), length });
		if (message != null) {
			messages.add(ActionMessages.GLOBAL_MESSAGE, message);
		}
	}

	/**
	 * 日付型チェックを行います.
	 * @param　messages　メッセージオブジェクト
	 * @param value　検査する値
	 * @param @param propertyName 項目名称
	 */
	private void validateDate(ActionMessages messages, String value,
			String propertyName) {
		ActionMessage message = ValidateUtil.dateType(value,
				Constants.FORMAT.DATE, true, "errors.line.date", new Object[] {
						this.lineNo + 1,
						MessageResourcesUtil.getMessage("labels.product.csv."
								+ propertyName) });
		if (message != null) {
			messages.add(ActionMessages.GLOBAL_MESSAGE, message);
		}
	}

	/**
	 * 整数チェックを行います.
	 * @param　messages　メッセージオブジェクト
	 * @param value　検査する値
	 * @param propertyName 項目名称
	 */
	private void validateInteger(ActionMessages messages, String value,
			String propertyName) {
		ActionMessage message = null;
		try {
			Integer.parseInt(value);
		} catch (NumberFormatException e) {
			message = new ActionMessage("errors.line.integer", new Object[] {
					this.lineNo + 1,
					MessageResourcesUtil.getMessage("labels.product.csv."
							+ propertyName) });
		}
		if (message != null) {
			messages.add(ActionMessages.GLOBAL_MESSAGE, message);
		}
	}

	/**
	 * Short型チェックを行います.
	 * @param　messages　メッセージオブジェクト
	 * @param value　検査する値
	 * @param propertyName 項目名称
	 */
	private void validateShort(ActionMessages messages, String value,
			String propertyName) {
		ActionMessage message = null;
		try {
			Short.parseShort(value);
		} catch (NumberFormatException e) {
			message = new ActionMessage("errors.line.short", new Object[] {
					this.lineNo + 1,
					MessageResourcesUtil.getMessage("labels.product.csv."
							+ propertyName) });
		}
		if (message != null) {
			messages.add(ActionMessages.GLOBAL_MESSAGE, message);
		}
	}

	/**
	 * 実数チェックを行います.
	 * @param　messages　メッセージオブジェクト
	 * @param value　検査する値
	 * @param propertyName 項目名称
	 */
	private void validateBigDecimal(ActionMessages messages, String value,
			String propertyName) {
		ActionMessage message = null;
		try {
			new BigDecimal(value);
		} catch (NumberFormatException e) {
			message = new ActionMessage("errors.line.double", new Object[] {
					this.lineNo + 1,
					MessageResourcesUtil.getMessage("labels.product.csv."
							+ propertyName) });
		}
		if (message != null) {
			messages.add(ActionMessages.GLOBAL_MESSAGE, message);
		}
	}

	/**
	 * 浮動小数点型チェックを行います.
	 * @param　messages　メッセージオブジェクト
	 * @param value　検査する値
	 * @param propertyName 項目名称
	 */
	private void validateFloat(ActionMessages messages, String value,
			String propertyName) {
		ActionMessage message = null;
		try {
			Float.parseFloat(value);
		} catch (NumberFormatException e) {
			message = new ActionMessage("errors.line.float", new Object[] {
					this.lineNo + 1,
					MessageResourcesUtil.getMessage("labels.product.csv."
							+ propertyName) });
		}
		if (message != null) {
			messages.add(ActionMessages.GLOBAL_MESSAGE, message);
		}
	}

	/**
	 * 空行かどうか判定します.
	 * @return true:空行　false：空行でない
	 */
	public boolean isEmptyRow() {

		if (StringUtil.hasLength(this.productCode)
				|| StringUtil.hasLength(this.productName)
				|| StringUtil.hasLength(this.productKana)
				|| StringUtil.hasLength(this.onlinePcode)
				|| StringUtil.hasLength(this.supplierPcode)
				|| StringUtil.hasLength(this.supplierCode)
				|| StringUtil.hasLength(this.rackCode)
				|| StringUtil.hasLength(this.supplierPriceYen)
				|| StringUtil.hasLength(this.supplierPriceDol)
				|| StringUtil.hasLength(this.retailPrice)
				|| StringUtil.hasLength(this.soRate)
				|| StringUtil.hasLength(this.unitCategory)
				|| StringUtil.hasLength(this.packQuantity)
				|| StringUtil.hasLength(this.janPcode)
				|| StringUtil.hasLength(this.width)
				|| StringUtil.hasLength(this.widthUnitSizeCategory)
				|| StringUtil.hasLength(this.depth)
				|| StringUtil.hasLength(this.depthUnitSizeCategory)
				|| StringUtil.hasLength(this.height)
				|| StringUtil.hasLength(this.heightUnitSizeCategory)
				|| StringUtil.hasLength(this.weight)
				|| StringUtil.hasLength(this.weightUnitSizeCategory)
				|| StringUtil.hasLength(this.length)
				|| StringUtil.hasLength(this.lengthUnitSizeCategory)
				|| StringUtil.hasLength(this.poLot)
				|| StringUtil.hasLength(this.lotUpdFlag)
				|| StringUtil.hasLength(this.leadTime)
				|| StringUtil.hasLength(this.poNum)
				|| StringUtil.hasLength(this.poUpdFlag)
				|| StringUtil.hasLength(this.mineSafetyStock)
				|| StringUtil.hasLength(this.mineSafetyStockUpdFlag)
				|| StringUtil.hasLength(this.entrustSafetyStock)
				|| StringUtil.hasLength(this.salesStandardDeviation)
				|| StringUtil.hasLength(this.avgShipCount)
				|| StringUtil.hasLength(this.maxStockNum)
				|| StringUtil.hasLength(this.stockUpdFlag)
				|| StringUtil.hasLength(this.termShipNum)
				|| StringUtil.hasLength(this.maxPoNum)
				|| StringUtil.hasLength(this.maxPoUpdFlag)
				|| StringUtil.hasLength(this.fractCategory)
				|| StringUtil.hasLength(this.taxCategory)
				|| StringUtil.hasLength(this.stockCtlCategory)
				|| StringUtil.hasLength(this.stockAssesCategory)
				|| StringUtil.hasLength(this.productCategory)
				|| StringUtil.hasLength(this.product1)
				|| StringUtil.hasLength(this.product2)
				|| StringUtil.hasLength(this.product3)
				|| StringUtil.hasLength(this.roMaxNum)
				|| StringUtil.hasLength(this.productRank)
				|| StringUtil.hasLength(this.setTypeCategory)
				|| StringUtil.hasLength(this.productStatusCategory)
				|| StringUtil.hasLength(this.productStockCategory)
				|| StringUtil.hasLength(this.productPurvayCategory)
				|| StringUtil.hasLength(this.productStandardCategory)
				|| StringUtil.hasLength(this.coreNum)
				|| StringUtil.hasLength(this.num1)
				|| StringUtil.hasLength(this.num2)
				|| StringUtil.hasLength(this.num3)
				|| StringUtil.hasLength(this.num4)
				|| StringUtil.hasLength(this.num5)
				|| StringUtil.hasLength(this.dec1)
				|| StringUtil.hasLength(this.dec2)
				|| StringUtil.hasLength(this.dec3)
				|| StringUtil.hasLength(this.dec4)
				|| StringUtil.hasLength(this.dec5)
				|| StringUtil.hasLength(this.discardDate)
				|| StringUtil.hasLength(this.remarks)
				|| StringUtil.hasLength(this.eadRemarks)
				|| StringUtil.hasLength(this.commentData)
				|| StringUtil.hasLength(this.lastRoDate)
				|| StringUtil.hasLength(this.discountId)) {
			return false;
		}
		return true;

	}
}
