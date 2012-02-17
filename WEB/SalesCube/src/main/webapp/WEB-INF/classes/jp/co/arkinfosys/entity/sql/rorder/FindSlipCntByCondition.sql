SELECT
	COUNT(DISTINCT SLIP.RO_SLIP_ID)
FROM
	RO_SLIP_TRN_/*$domainId*/ SLIP
	INNER JOIN RO_LINE_TRN_/*$domainId*/ LINE ON SLIP.RO_SLIP_ID = LINE.RO_SLIP_ID
	LEFT OUTER JOIN PRODUCT_MST_/*$domainId*/ PROD ON LINE.PRODUCT_CODE = PROD.PRODUCT_CODE
	LEFT OUTER JOIN SUPPLIER_MST_/*$domainId*/ SUPP ON PROD.SUPPLIER_CODE = SUPP.SUPPLIER_CODE
/*BEGIN*/
WHERE
	/*IF roSlipId != null */
	AND SLIP.RO_SLIP_ID = /*roSlipId*/1
	/*END*/
	/*IF receptNo != null */
	AND SLIP.RECEPT_NO LIKE /*receptNo*/'S%'
	/*END*/
	/*IF razyOnly != "1" && restOnly == "1" */
	AND LINE.STATUS <> '9'
	/*END*/
	/*IF razyOnly == "1" */
	AND ( SLIP.DELIVERY_DATE <= CAST(now() AS DATE) AND LINE.STATUS <> /*roLineStatusCode*/ )
	/*END*/
	/*IF roDateFrom != null */
	AND SLIP.RO_DATE >= CAST(/*roDateFrom*/'2000/01/01' AS DATE)
	/*END*/
	/*IF roDateTo != null */
	AND SLIP.RO_DATE <= CAST(/*roDateTo*/'2000/01/01' AS DATE)
	/*END*/

	/*IF shipDateFrom != null */
	AND SLIP.SHIP_DATE >= CAST(/*shipDateFrom*/'2000/01/01' AS DATE)
	/*END*/
	/*IF shipDateTo != null */
	AND SLIP.SHIP_DATE <= CAST(/*shipDateTo*/'2000/01/01' AS DATE)
	/*END*/

	/*IF deliveryDateFrom != null */
	AND SLIP.DELIVERY_DATE >= CAST(/*deliveryDateFrom*/'2000/01/01' AS DATE)
	/*END*/
	/*IF deliveryDateTo != null */
	AND SLIP.DELIVERY_DATE <= CAST(/*deliveryDateTo*/'2000/01/01' AS DATE)
	/*END*/

	/*IF customerCode != null */
	AND SLIP.CUSTOMER_CODE LIKE /*customerCode*/'S%'
	/*END*/
	/*IF customerName != null */
	AND SLIP.CUSTOMER_NAME LIKE /*customerName*/'%S%'
	/*END*/
	/*IF deliveryPcName != null */
	AND SLIP.DELIVERY_PC_NAME LIKE /*deliveryPcName*/'%S%'
	/*END*/
	/*IF salesCmCategoryList != null*/
	AND SLIP.SALES_CM_CATEGORY IN /*salesCmCategoryList*/('S', 'S')
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

	/*IF supplierCode != null */
	AND PROD.SUPPLIER_CODE LIKE /*supplierCode*/'S%'
	/*END*/
	/*IF supplierName != null */
	AND SUPP.SUPPLIER_NAME LIKE /*supplierName*/'%S%'
	/*END*/

/*END*/
