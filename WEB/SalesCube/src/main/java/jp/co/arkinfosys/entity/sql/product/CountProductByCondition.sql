SELECT
		COUNT(P.PRODUCT_CODE)
    FROM
        PRODUCT_MST_/*$domainId*/ P
        LEFT OUTER JOIN SUPPLIER_MST_/*$domainId*/ S ON P.SUPPLIER_CODE = S.SUPPLIER_CODE
    /*BEGIN*/
	WHERE
		/*IF productCode != null */
		P.PRODUCT_CODE LIKE /*productCode*/'S%'
		/*END*/
		/*IF supplierPcode != null */
		AND P.SUPPLIER_PCODE LIKE /*supplierPcode*/'S%'
		/*END*/
		/*IF janPcode != null */
		AND P.JAN_PCODE LIKE /*janPcode*/'S%'
		/*END*/
		/*IF productName != null */
		AND P.PRODUCT_NAME LIKE /*productName*/'%S%'
		/*END*/
		/*IF productKana != null */
		AND P.PRODUCT_KANA LIKE /*productKana*/'%S%'
		/*END*/
		/*IF supplierCode != null */
		AND S.SUPPLIER_CODE LIKE /*supplierCode*/'S%'
		/*END*/
		/*IF supplierName != null */
		AND S.SUPPLIER_NAME LIKE /*supplierName*/'%S%'
		/*END*/
		/*IF setTypeCategory != null */
		AND P.SET_TYPE_CATEGORY = /*setTypeCategory*/'0'
		/*END*/
		/*IF productStandardCategory != null */
		AND P.PRODUCT_STANDARD_CATEGORY = /*productStandardCategory*/'00'
		/*END*/
		/*IF productStatusCategory != null */
		AND P.PRODUCT_STATUS_CATEGORY = /*productStatusCategory*/'00'
		/*END*/
		/*IF productStockCategory != null */
		AND P.PRODUCT_STOCK_CATEGORY = /*productStockCategory*/'00'
		/*END*/
		/*IF remarks != null */
		AND P.REMARKS LIKE /*remarks*/'%S%'
		/*END*/
		/*IF product1 != null */
		AND P.PRODUCT_1 = /*product1*/'0000'
		/*END*/
		/*IF product2 != null */
		AND P.PRODUCT_2 = /*product2*/'0000'
		/*END*/
		/*IF product3 != null */
		AND P.PRODUCT_3 = /*product3*/'0000'
		/*END*/
	/*END*/

