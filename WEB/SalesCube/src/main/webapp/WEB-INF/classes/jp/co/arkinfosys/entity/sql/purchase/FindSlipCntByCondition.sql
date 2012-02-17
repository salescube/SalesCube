SELECT
	COUNT(DISTINCT SLIP.SUPPLIER_SLIP_ID)
FROM
	SUPPLIER_SLIP_TRN_/*$domainId*/ SLIP
	INNER JOIN SUPPLIER_LINE_TRN_/*$domainId*/ LINE
		ON SLIP.SUPPLIER_SLIP_ID = LINE.SUPPLIER_SLIP_ID
	LEFT OUTER JOIN CATEGORY_TRN_/*$domainId*/ CAT1
		ON SLIP.SUPPLIER_SLIP_CATEGORY = CAT1.CATEGORY_CODE AND CAT1.CATEGORY_ID = /*supplierSlipCategory*/57
	LEFT OUTER JOIN CATEGORY_TRN_/*$domainId*/ CAT2
		ON SLIP.SUPPLIER_CM_CATEGORY = CAT2.CATEGORY_CODE AND CAT2.CATEGORY_ID = /*supplierCmCategory*/13
	LEFT OUTER JOIN CATEGORY_TRN_/*$domainId*/ CAT3
		ON LINE.SUPPLIER_DETAIL_CATEGORY = CAT3.CATEGORY_CODE AND CAT3.CATEGORY_ID = /*supplierDetailCategory*/6
	LEFT OUTER JOIN CATEGORY_TRN_/*$domainId*/ CAT4
		ON LINE.DELIVERY_PROCESS_CATEGORY = CAT4.CATEGORY_CODE AND CAT4.CATEGORY_ID = /*deliveryProcessCategoryMst*/30
	LEFT OUTER JOIN CATEGORY_TRN_/*$domainId*/ CAT5
		ON LINE.TEMP_UNIT_PRICE_CATEGORY = CAT5.CATEGORY_CODE AND CAT5.CATEGORY_ID = /*tempUnitPriceCategory*/56
	LEFT OUTER JOIN CATEGORY_TRN_/*$domainId*/ CAT6
		ON LINE.TAX_CATEGORY = CAT6.CATEGORY_CODE AND CAT6.CATEGORY_ID = /*taxCategory*/39
	LEFT OUTER JOIN PRODUCT_MST_/*$domainId*/ PROD
		ON LINE.PRODUCT_CODE = PROD.PRODUCT_CODE
/*BEGIN*/
WHERE
	/*IF supplierSlipId != null */
	AND SLIP.SUPPLIER_SLIP_ID = /*supplierSlipId*/1
	/*END*/
	/*IF poSlipId != null */
	AND SLIP.PO_SLIP_ID = /*poSlipId*/1
	/*END*/
	/*IF userName != null */
	AND SLIP.USER_NAME like /*userName*/'S%'
	/*END*/
	/*IF supplierDateFrom != null */
	AND SLIP.SUPPLIER_DATE >= CAST(/*supplierDateFrom*/'2000/01/01' AS DATE)
	/*END*/
	/*IF supplierDateTo != null */
	AND SLIP.SUPPLIER_DATE <= CAST(/*supplierDateTo*/'2000/01/01' AS DATE)
	/*END*/
	/*IF supplierCode != null */
	AND SLIP.SUPPLIER_CODE LIKE /*supplierCode*/'S%'
	/*END*/
	/*IF supplierName != null */
	AND SLIP.SUPPLIER_NAME LIKE /*supplierName*/'%S%'
	/*END*/
	/*IF remarks != null */
	AND SLIP.REMARKS LIKE /*remarks*/'%S%'
	/*END*/
	/*IF productCode != null */
	AND LINE.PRODUCT_CODE LIKE /*productCode*/'S%'
	/*END*/
	/*IF productAbstract != null */
	AND LINE.PRODUCT_ABSTRACT LIKE /*productAbstract*/'%S%'
	/*END*/
	/*IF product1 != null */
	AND PROD.PRODUCT_1 = /*product1*/'S'
	/*END*/
	/*IF product2 != null */
	AND PROD.PRODUCT_2 = /*product2*/'S'
	/*END*/
	/*IF product3 != null */
	AND PROD.PRODUCT_3 = /*product3*/'S'
	/*END*/
/*END*/
