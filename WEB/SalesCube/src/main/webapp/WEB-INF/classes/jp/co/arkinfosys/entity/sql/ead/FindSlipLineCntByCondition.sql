SELECT
	COUNT(*)
FROM
	EAD_SLIP_TRN_/*$domainId*/ SLIP
	INNER JOIN EAD_LINE_TRN_/*$domainId*/ LINE ON SLIP.EAD_SLIP_ID = LINE.EAD_SLIP_ID
	LEFT OUTER JOIN PRODUCT_MST_/*$domainId*/ PROD ON LINE.PRODUCT_CODE = PROD.PRODUCT_CODE
	LEFT OUTER JOIN SUPPLIER_MST_/*$domainId*/ SUPP ON PROD.SUPPLIER_CODE = SUPP.SUPPLIER_CODE
	LEFT OUTER JOIN CATEGORY_TRN_/*$domainId*/ S_CAT ON
			SLIP.EAD_SLIP_CATEGORY = S_CAT.CATEGORY_CODE AND S_CAT.CATEGORY_ID = /*eadSlipCategoryId*/
	LEFT OUTER JOIN CATEGORY_TRN_/*$domainId*/ CAT ON
			SLIP.EAD_CATEGORY = CAT.CATEGORY_CODE AND CAT.CATEGORY_ID = /*eadCategoryId*/
	LEFT OUTER JOIN EAD_SLIP_TRN_/*$domainId*/ SLIP_M ON SLIP.EAD_SLIP_ID = SLIP_M.MOVE_DEPOSIT_SLIP_ID
	LEFT OUTER JOIN EAD_LINE_TRN_/*$domainId*/ LINE_M ON SLIP_M.EAD_SLIP_ID = LINE_M.EAD_SLIP_ID AND LINE.LINE_NO = LINE_M.LINE_NO
/*BEGIN*/
WHERE
	/*IF noTarget != null */
	(NOT (SLIP.EAD_CATEGORY = /*noTargetEadSlipCategory*/'S' AND SLIP.SRC_FUNC = /*noTargetSrcFunc*/'S'))
	/*END*/
	/*IF srcSlipId != null */
	AND (
		SLIP.EAD_SLIP_ID = /*srcSlipId*/1
		OR SLIP.SALES_SLIP_ID = /*srcSlipId*/1
		OR SLIP.SUPPLIER_SLIP_ID = /*srcSlipId*/1
	)
	/*END*/
	/*IF srcFunc != null */
	AND SLIP.SRC_FUNC IN /*srcFunc*/('S')
	/*END*/
	/*IF eadSlipCategory != null */
	AND SLIP.EAD_SLIP_CATEGORY = /*eadSlipCategory*/'S'
	/*END*/
	/*IF userName != null */
	AND SLIP.USER_NAME LIKE /*userName*/'S%'
	/*END*/
	/*IF eadDateFrom != null */
	AND SLIP.EAD_DATE >= CAST(/*eadDateFrom*/'2000/01/01' AS DATE)
	/*END*/
	/*IF eadDateTo != null */
	AND SLIP.EAD_DATE <= CAST(/*eadDateTo*/'2000/01/01' AS DATE)
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
	/*IF rackCode != null */
	AND LINE.RACK_CODE LIKE /*rackCode*/'S%'
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
	/*IF salesLineId != null */
	AND LINE.SALES_LINE_ID = /*salesLineId*/'S'
	/*END*/
/*END*/