SELECT
	SLIP.PO_SLIP_ID 		AS PO_SLIP_ID
	,SLIP.STATUS			AS STATUS
	,CASE WHEN ( SLIP.STATUS <> /*statusPorderSlipPurchased*/'9' -- 仕入完了：Constants.STATUS_PORDER_SLIP.PURCHASED じゃない。
					OR COALESCE(UNPAIDSUPPSLIP.UNPAIDSUPPSLIPCOUNT, 0) > 0 )
			THEN /*slipPaymentStatusUnpaid*/
			ELSE /*slipPaymentStatusPaid*/ END	AS PAYMENT_STATUS
	,SLIP.PO_DATE 			AS PO_DATE
	,SLIP.DELIVERY_DATE 	AS DELIVERY_DATE
	,SLIP.USER_ID			AS USER_ID
	,SLIP.USER_NAME 		AS USER_NAME
	,SLIP.REMARKS 			AS REMARKS
	,SLIP.SUPPLIER_CODE 	AS SUPPLIER_CODE
	,SLIP.SUPPLIER_NAME 	AS SUPPLIER_NAME
	,SLIP.TRANSPORT_CATEGORY 			AS TRANSPORT_CATEGORY
	,TRANSPORTCAT.CATEGORY_CODE_NAME 	AS TRANSPORT_CATEGORY_STRING
	,CASE WHEN SLIP.CTAX_TOTAL IS NOT NULL
			THEN SLIP.PRICE_TOTAL - SLIP.CTAX_TOTAL
			ELSE SLIP.PRICE_TOTAL END			AS PURE_PRICE_TOTAL
	,SLIP.PRICE_TOTAL 		AS PRICE_TOTAL
	,SLIP.CTAX_TOTAL 		AS CTAX_TOTAL
	,SLIP.CTAX_RATE			AS CTAX_RATE
	,SLIP.FE_PRICE_TOTAL 	AS FE_PRICE_TOTAL

FROM
	PO_SLIP_TRN_/*$domainId*/ SLIP
	LEFT OUTER JOIN PO_LINE_TRN_/*$domainId*/ LINE
		ON SLIP.PO_SLIP_ID = LINE.PO_SLIP_ID

	LEFT OUTER JOIN PRODUCT_MST_/*$domainId*/ PROD
		ON LINE.PRODUCT_CODE = PROD.PRODUCT_CODE

	LEFT OUTER JOIN CATEGORY_TRN_/*$domainId*/ TRANSPORTCAT
		ON TRANSPORTCAT.CATEGORY_ID = /*transportCategoryCode*/
		AND TRANSPORTCAT.CATEGORY_CODE = SLIP.TRANSPORT_CATEGORY

	LEFT OUTER JOIN CATEGORY_TRN_/*$domainId*/ STATUSCAT
		ON STATUSCAT.CATEGORY_ID = /*poLineStatusCategory*/
		AND STATUSCAT.CATEGORY_CODE = LINE.STATUS

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
	AND SLIP.DELIVERY_DATE >= CAST(/*deliveryDateFrom*/ AS DATE)
	/*END*/
	/*IF deliveryDateTo != null */
	AND SLIP.DELIVERY_DATE <= CAST(/*deliveryDateTo*/ AS DATE)
	/*END*/
	/*IF remarks != null */
	AND SLIP.REMARKS LIKE /*remarks*/
	/*END*/
	/*IF transportCategory != null */
	AND SLIP.TRANSPORT_CATEGORY = /*transportCategory*/
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
	/*IF entrustPoRest == true */
	AND LINE.STATUS = '0'
	AND SLIP.TRANSPORT_CATEGORY = '3'
	/*END*/
	/*IF entrustPoMaked == true */
	AND LINE.STATUS = '2'
	AND SLIP.TRANSPORT_CATEGORY = '3'
	/*END*/
	/*IF entrustPoDelivered == true */
	AND LINE.STATUS = '3'
	AND SLIP.TRANSPORT_CATEGORY = '3'
	/*END*/
	/*IF normalPoRest == true */
	AND SLIP.TRANSPORT_CATEGORY <> '3'
	/*END*/
/*END*/


GROUP BY
	SLIP.PO_SLIP_ID
	,SLIP.STATUS
	,SLIP.PO_DATE
	,SLIP.DELIVERY_DATE
	,SLIP.USER_ID
	,SLIP.USER_NAME
	,SLIP.REMARKS
	,SLIP.SUPPLIER_CODE
	,SLIP.SUPPLIER_NAME
	,SLIP.TRANSPORT_CATEGORY
	,TRANSPORTCAT.CATEGORY_CODE_NAME
	,SLIP.PRICE_TOTAL
	,SLIP.CTAX_TOTAL
	,SLIP.FE_PRICE_TOTAL
	,UNPAIDSUPPSLIP.UNPAIDSUPPSLIPCOUNT

/*BEGIN*/
ORDER BY
	/*IF sortColumnSlip != null */
		/*$sortColumnSlip*/
	/*END*/
	/*IF sortColumnLine != null */
		/*$sortColumnLine*/
	/*END*/
	/*IF (sortColumnSlip != null || sortColumnLine != null) && sortOrder != null*/
	/*$sortOrder*/
	/*END*/
/*END*/
/*IF rowCount != null && offsetRow != null*/
LIMIT /*rowCount*/ OFFSET /*offsetRow*/
/*END*/