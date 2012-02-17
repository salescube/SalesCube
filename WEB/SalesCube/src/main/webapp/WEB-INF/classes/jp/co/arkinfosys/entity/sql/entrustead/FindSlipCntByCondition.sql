SELECT
	COUNT(DISTINCT SLIP.ENTRUST_EAD_SLIP_ID)
FROM
	ENTRUST_EAD_SLIP_TRN_/*$domainId*/ SLIP
	INNER JOIN ENTRUST_EAD_LINE_TRN_/*$domainId*/ LINE ON SLIP.ENTRUST_EAD_SLIP_ID = LINE.ENTRUST_EAD_SLIP_ID
	LEFT OUTER JOIN PRODUCT_MST_/*$domainId*/ PROD ON LINE.PRODUCT_CODE = PROD.PRODUCT_CODE
	LEFT OUTER JOIN SUPPLIER_MST_/*$domainId*/ SUPP ON PROD.SUPPLIER_CODE = SUPP.SUPPLIER_CODE
	LEFT OUTER JOIN CATEGORY_TRN_/*$domainId*/ CAT ON
			SLIP.ENTRUST_EAD_CATEGORY = CAT.CATEGORY_CODE AND CAT.CATEGORY_ID = /*entrustEadCategoryId*/
/*BEGIN*/
WHERE
	/*IF entrustEadSlipId != null */
	SLIP.ENTRUST_EAD_SLIP_ID = /*entrustEadSlipId*/0
	/*END*/
	/*IF entrustEadCategory != null || entrustEadCategoryDispatchNoPrint != null */
	AND (
		/*IF entrustEadCategory != null */
		SLIP.ENTRUST_EAD_CATEGORY IN /*entrustEadCategory*/('', '')
		/*END*/
		/*IF entrustEadCategoryDispatchNoPrint != null */
		OR (SLIP.DISPATCH_ORDER_PRINT_COUNT = 0 AND SLIP.ENTRUST_EAD_CATEGORY = /*entrustEadCategoryDispatch*/)
		/*END*/
	)
	/*END*/
	/*IF poSlipId != null */
	AND SLIP.PO_SLIP_ID = /*poSlipId*/0
	/*END*/
	/*IF userName != null */
	AND SLIP.USER_NAME LIKE /*userName*/'S%'
	/*END*/
	/*IF entrustEadDateFrom != null */
	AND SLIP.ENTRUST_EAD_DATE >= CAST(/*entrustEadDateFrom*/'2000/01/01' AS DATE)
	/*END*/
	/*IF entrustEadDateTo != null */
	AND SLIP.ENTRUST_EAD_DATE <= CAST(/*entrustEadDateTo*/'2000/01/01' AS DATE)
	/*END*/
	/*IF remarks != null */
	AND SLIP.REMARKS LIKE /*remarks*/'%S%'
	/*END*/
	/*IF supplierCode != null */
	AND PROD.SUPPLIER_CODE LIKE /*supplierCode*/'S%'
	/*END*/
	/*IF supplierName != null */
	AND SUPP.SUPPLIER_NAME LIKE /*supplierName*/'%S%'
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
