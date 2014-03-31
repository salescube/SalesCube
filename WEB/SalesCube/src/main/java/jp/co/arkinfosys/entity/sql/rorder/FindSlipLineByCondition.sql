SELECT
	DISTINCT
	CONCAT(CAST(LINE.RO_SLIP_ID AS CHAR),' - ',CAST(LINE.LINE_NO AS CHAR)) AS RO_SLIP_ID,

	SLIP.ESTIMATE_SHEET_ID,
	SLIP.RO_DATE,
	SLIP.SHIP_DATE,
	SLIP.DELIVERY_DATE,
	SLIP.RECEPT_NO,
	SLIP.CUSTOMER_SLIP_NO,
	SLIP.REMARKS,
	SLIP.CUSTOMER_CODE,
	SLIP.CUSTOMER_NAME,
	SLIP.CUSTOMER_REMARKS,
	SLIP.CUSTOMER_COMMENT_DATA,
	SLIP.USER_ID,
	SLIP.USER_NAME,
	SLIP.DELIVERY_NAME,
	SLIP.DELIVERY_PC_NAME,
	CAT1.CATEGORY_CODE_NAME AS TAX_SHIFT_CATEGORY,
	CAT2.CATEGORY_CODE_NAME AS CUTOFF_GROUP,
	CAT3.CATEGORY_CODE_NAME AS SALES_CM_CATEGORY,

	LINE.PRODUCT_CODE,
	LINE.PRODUCT_ABSTRACT,
	LINE.QUANTITY,
	CAT4.CATEGORY_CODE_NAME AS STATUS,
	LINE.UNIT_COST,
	LINE.UNIT_RETAIL_PRICE,
	LINE.COST,
	LINE.RETAIL_PRICE,
	LINE.REMARKS AS LINE_REMARKS,
	LINE.PRODUCT_REMARKS,
	LINE.REST_QUANTITY,

	LINE.RETAIL_PRICE-LINE.COST AS PROFIT,
	(LINE.RETAIL_PRICE-LINE.COST)/LINE.RETAIL_PRICE AS PROFIT_RATIO,
	SLIP.RETAIL_PRICE_TOTAL,
	SLIP.CTAX_PRICE_TOTAL,
	SLIP.CTAX_RATE,
	SLIP.PRICE_TOTAL,
    SLIP.DC_NAME,
    SLIP.DC_TIMEZONE,

   	LINE.RO_SLIP_ID AS SORT_RO_SLIP_ID,
	LINE.LINE_NO AS SORT_RO_LINE_NO

FROM
	RO_SLIP_TRN_/*$domainId*/ SLIP
	INNER JOIN RO_LINE_TRN_/*$domainId*/ LINE ON SLIP.RO_SLIP_ID = LINE.RO_SLIP_ID
	LEFT OUTER JOIN PRODUCT_MST_/*$domainId*/ PROD ON LINE.PRODUCT_CODE = PROD.PRODUCT_CODE
	LEFT OUTER JOIN SUPPLIER_MST_/*$domainId*/ SUPP ON PROD.SUPPLIER_CODE = SUPP.SUPPLIER_CODE
	LEFT OUTER JOIN CATEGORY_TRN_/*$domainId*/ CAT1 ON (SLIP.TAX_SHIFT_CATEGORY = CAT1.CATEGORY_CODE AND CAT1.CATEGORY_ID = /*taxShiftCategory*/29)
	LEFT OUTER JOIN CATEGORY_TRN_/*$domainId*/ CAT2 ON (CONCAT(SLIP.CUTOFF_GROUP,SLIP.PAYBACK_CYCLE_CATEGORY) = CAT2.CATEGORY_CODE AND CAT2.CATEGORY_ID = /*cutoffCategory*/11)
	LEFT OUTER JOIN CATEGORY_TRN_/*$domainId*/ CAT3 ON (SLIP.SALES_CM_CATEGORY = CAT3.CATEGORY_CODE AND CAT3.CATEGORY_ID = /*salesCmCategory*/32)
	LEFT OUTER JOIN CATEGORY_TRN_/*$domainId*/ CAT4 ON (LINE.STATUS = CAT4.CATEGORY_CODE AND CAT4.CATEGORY_ID = /*roLineStatus*/107)

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
/*BEGIN*/
	ORDER BY

		/*IF sortColumn != null */
			/*$sortColumn*/
			/*IF sortOrderAsc != null*/
			/*$sortOrderAsc*/
			-- ELSE ASC
			/*END*/
		/*END*/

		/*IF sortColumnSlip != null */
			/*$sortColumnSlip*/
			/*IF sortOrderAsc != null*/
			/*$sortOrderAsc*/
			-- ELSE ASC
			/*END*/
		/*END*/
		/*IF sortColumnLine != null */
			/*IF sortColumnSlip != null */,/*END*/
			/*$sortColumnLine*/
			/*IF sortOrderAsc != null*/
			/*$sortOrderAsc*/
			-- ELSE ASC
			/*END*/
		/*END*/
/*END*/
/*IF rowCount != null && offsetRow != null*/
	LIMIT /*rowCount*/ OFFSET /*offsetRow*/
/*END*/