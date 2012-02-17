SELECT
	COUNT(DISTINCT SLIP.SALES_SLIP_ID)
FROM
	SALES_SLIP_TRN_/*$domainId*/ SLIP
	INNER JOIN SALES_LINE_TRN_/*$domainId*/ LINE ON SLIP.SALES_SLIP_ID = LINE.SALES_SLIP_ID
	LEFT OUTER JOIN PRODUCT_MST_/*$domainId*/ PROD ON LINE.PRODUCT_CODE = PROD.PRODUCT_CODE
	LEFT OUTER JOIN SUPPLIER_MST_/*$domainId*/ SUPP ON PROD.SUPPLIER_CODE = SUPP.SUPPLIER_CODE
	LEFT OUTER JOIN CATEGORY_TRN_/*$domainId*/ CAT1 ON (SLIP.TAX_SHIFT_CATEGORY = CAT1.CATEGORY_CODE AND CAT1.CATEGORY_ID = /*taxShiftCategory*/29)
	LEFT OUTER JOIN CATEGORY_TRN_/*$domainId*/ CAT2 ON (CONCAT(SLIP.BILL_CUTOFF_GROUP,SLIP.PAYBACK_CYCLE_CATEGORY) = CAT2.CATEGORY_CODE AND CAT2.CATEGORY_ID = /*cutoffCategory*/11)
	LEFT OUTER JOIN CATEGORY_TRN_/*$domainId*/ CAT3 ON (SLIP.SALES_CM_CATEGORY = CAT3.CATEGORY_CODE AND CAT3.CATEGORY_ID = /*salesCmCategory*/32)
	LEFT OUTER JOIN RO_SLIP_TRN_/*$domainId*/ RO_SLIP ON SLIP.RO_SLIP_ID = RO_SLIP.RO_SLIP_ID
/*BEGIN*/
WHERE
	/*IF salesSlipId != null */
	AND SLIP.SALES_SLIP_ID = /*salesSlipId*/1
	/*END*/
	/*IF roSlipId != null */
	AND SLIP.RO_SLIP_ID = /*roSlipId*/1
	/*END*/
	/*IF receptNo != null */
	AND SLIP.RECEPT_NO LIKE /*receptNo*/'1%'
	/*END*/
	/*IF salesDateFrom != null */
	AND SLIP.SALES_DATE >= CAST(/*salesDateFrom*/'2000/01/01' AS DATE)
	/*END*/
	/*IF salesDateTo != null */
	AND SLIP.SALES_DATE <= CAST(/*salesDateTo*/'2000/01/01' AS DATE)
	/*END*/
	/*IF deliveryDateFrom != null */
	AND SLIP.DELIVERY_DATE >= CAST(/*deliveryDateFrom*/'2000/01/01' AS DATE)
	/*END*/
	/*IF deliveryDateTo != null */
	AND SLIP.DELIVERY_DATE <= CAST(/*deliveryDateTo*/'2000/01/01' AS DATE)
	/*END*/
	/*IF dcCategory != null */
	AND SLIP.DC_CATEGORY = /*dcCategory*/'1'
	/*END*/
	/*IF dcTimezoneCategory != null */
	AND SLIP.DC_TIMEZONE_CATEGORY = /*dcTimezoneCategory*/'1'
	/*END*/
	/*IF pickingRemarks != null */
	AND SLIP.PICKING_REMARKS LIKE /*pickingRemarks*/'%1%'
	/*END*/
	/*IF customerCode != null */
	AND SLIP.CUSTOMER_CODE LIKE /*customerCode*/'1%'
	/*END*/
	/*IF customerName != null */
	AND SLIP.CUSTOMER_NAME LIKE /*customerName*/'%1%'
	/*END*/
	/*IF customerPcName != null */
	AND SLIP.DELIVERY_PC_NAME LIKE /*customerPcName*/'%1%'
	/*END*/
	/*IF salesCmCategoryList != null*/
	AND SLIP.SALES_CM_CATEGORY IN /*salesCmCategoryList*/('S', 'S')
	/*END*/
	/*IF productCode != null */
	AND LINE.PRODUCT_CODE LIKE /*productCode*/'1%'
	/*END*/
	/*IF productAbstract != null */
	AND LINE.PRODUCT_ABSTRACT LIKE /*productAbstract*/'%1%'
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
	/*IF supplierCode != null */
	AND SUPP.SUPPLIER_CODE LIKE /*supplierCode*/'S%'
	/*END*/
	/*IF supplierName != null */
	AND SUPP.SUPPLIER_NAME LIKE /*supplierName*/'%S%'
	/*END*/
/*END*/
