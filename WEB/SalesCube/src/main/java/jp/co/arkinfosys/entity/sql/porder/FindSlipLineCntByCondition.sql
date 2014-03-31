SELECT COUNT(*) FROM(

SELECT
	DISTINCT
	SLIP.PO_SLIP_ID
	,LINE.PO_LINE_ID
FROM
	PO_SLIP_TRN_/*$domainId*/ SLIP
	LEFT OUTER JOIN PO_LINE_TRN_/*$domainId*/ LINE ON SLIP.PO_SLIP_ID = LINE.PO_SLIP_ID
	LEFT OUTER JOIN PRODUCT_MST_/*$domainId*/ PROD ON LINE.PRODUCT_CODE = PROD.PRODUCT_CODE

	LEFT OUTER JOIN (
		SELECT
			COUNT(DISTINCT SUPPSLIP.PO_SLIP_ID) AS UNPAIDSUPPSLIPCOUNT
			,SUPPSLIP.PO_SLIP_ID AS PO_SLIP_ID
				FROM SUPPLIER_SLIP_TRN_/*$domainId*/ SUPPSLIP
			WHERE SUPPSLIP.STATUS in ( /*statusSupplierSlipUnpaid*/'0',/*statusSupplierSlipPaying*/'1') -- 未払い：Constants.STATUS_SUPPLIER_SLIP.UNPAID, 支払中：Constants.STATUS_SUPPLIER_SLIP.PAYING
			GROUP BY
				SUPPSLIP.PO_SLIP_ID
	) UNPAIDSUPPSLIP
		ON SLIP.PO_SLIP_ID = UNPAIDSUPPSLIP.PO_SLIP_ID
/*BEGIN*/
WHERE

	/*IF poSlipId != null */
	SLIP.PO_SLIP_ID = /*poSlipId*/
	/*END*/
	/*IF userName != null */
	AND SLIP.USER_NAME LIKE /*userName*/
	/*END*/
	/*IF poDateFrom != null */
	AND SLIP.PO_DATE >= CAST(/*poDateFrom*/ AS DATE)
	/*END*/
	/*IF poDateTo != null */
	AND SLIP.PO_DATE <= CAST(/*poDateTo*/ AS DATE)
	/*END*/
	/*IF deliveryDateFrom != null */
	AND LINE.DELIVERY_DATE >= CAST(/*deliveryDateFrom*/ AS DATE)
	/*END*/
	/*IF deliveryDateTo != null */
	AND LINE.DELIVERY_DATE <= CAST(/*deliveryDateTo*/ AS DATE)
	/*END*/
	/*IF remarks != null */
	AND SLIP.REMARKS LIKE /*remarks*/
	/*END*/
	/*IF transportCategory != null */
	AND SLIP.TRANSPORT_CATEGORY LIKE /*transportCategory*/
	/*END*/
	/*IF onlyRestQuantityExist != null */
	AND LINE.STATUS <> '9'
	/*END*/
	/*IF onlyUnpaid != null */
	AND COALESCE(UNPAIDSUPPSLIP.UNPAIDSUPPSLIPCOUNT, 0) > 0
	/*END*/
	/*IF supplierCode != null */
	AND SLIP.SUPPLIER_CODE LIKE /*supplierCode*/
	/*END*/
	/*IF supplierName != null */
	AND SLIP.SUPPLIER_NAME LIKE /*supplierName*/
	/*END*/
	/*IF supplierPcName != null */
	AND SLIP.SUPPLIER_PC_NAME LIKE /*supplierPcName*/
	/*END*/
	/*IF productCode != null */
	AND LINE.PRODUCT_CODE LIKE /*productCode*/
	/*END*/
	/*IF productAbstract != null */
	AND LINE.PRODUCT_ABSTRACT LIKE /*productAbstract*/
	/*END*/
	/*IF product1 != null */
	AND PROD.PRODUCT_1 = /*product1*/
	/*END*/
	/*IF product2 != null */
	AND PROD.PRODUCT_2 = /*product2*/
	/*END*/
	/*IF product3 != null */
	AND PROD.PRODUCT_3 = /*product3*/
	/*END*/
/*END*/

) TABLEFORCOUNT
