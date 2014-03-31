SELECT
	DISTINCT
	CONCAT(CAST(LINE.SALES_SLIP_ID AS CHAR),' - ',CAST(LINE.LINE_NO AS CHAR)) AS SALES_SLIP_ID,
	CONCAT(CAST(RO_LINE.RO_SLIP_ID AS CHAR),' - ',CAST(RO_LINE.LINE_NO AS CHAR)) AS RO_SLIP_ID,
	LINE.PRODUCT_CODE,
	LINE.PRODUCT_ABSTRACT,
	LINE.QUANTITY,
	CATA.CATEGORY_CODE_NAME AS DELIVERY_PROCESS_CATEGORY,
	LINE.UNIT_COST,
	LINE.UNIT_RETAIL_PRICE,
	LINE.COST,
	LINE.RETAIL_PRICE,
	LINE.REMARKS AS LINE_REMARKS,
	LINE.PRODUCT_REMARKS,
	LINE.EAD_REMARKS,
	RO_SLIP.ESTIMATE_SHEET_ID,
	SLIP.SALES_DATE,
	SLIP.DELIVERY_DATE,
	SLIP.RECEPT_NO,
	SLIP.USER_ID,
	SLIP.USER_NAME,
	SLIP.DC_NAME,
	SLIP.DC_TIMEZONE,
	SLIP.REMARKS,
	SLIP.PICKING_REMARKS,
	SLIP.CUSTOMER_CODE,
	SLIP.CUSTOMER_NAME,
	SLIP.CUSTOMER_REMARKS,
	SLIP.CUSTOMER_COMMENT_DATA,
	SLIP.DELIVERY_NAME,
	SLIP.DELIVERY_PC_NAME,
	CAT1.CATEGORY_CODE_NAME AS TAX_SHIFT_CATEGORY,
	CAT2.CATEGORY_CODE_NAME AS CUTOFF_CATEGORY,
	CAT3.CATEGORY_CODE_NAME AS SALES_CM_CATEGORY,
	LINE.GM AS GM_TOTAL,
	LINE.GM/LINE.RETAIL_PRICE AS GM_RATE,
	SLIP.PRICE_TOTAL,
	SLIP.CTAX_PRICE_TOTAL,
	SLIP.CTAX_RATE,
	SLIP.PRICE_TOTAL + SLIP.CTAX_PRICE_TOTAL AS SLIP_TOTAL,
	LINE.SALES_SLIP_ID AS SORT_SALES_SLIP_ID,
	LINE.LINE_NO AS SORT_SALES_LINE_NO,
	RO_LINE.RO_SLIP_ID AS SORT_RO_SLIP_ID,
	RO_LINE.LINE_NO AS SORT_RO_LINE_NO
FROM
	SALES_SLIP_TRN_/*$domainId*/ SLIP
	INNER JOIN SALES_LINE_TRN_/*$domainId*/ LINE ON SLIP.SALES_SLIP_ID = LINE.SALES_SLIP_ID
	LEFT OUTER JOIN PRODUCT_MST_/*$domainId*/ PROD ON LINE.PRODUCT_CODE = PROD.PRODUCT_CODE
	LEFT OUTER JOIN SUPPLIER_MST_/*$domainId*/ SUPP ON PROD.SUPPLIER_CODE = SUPP.SUPPLIER_CODE
	LEFT OUTER JOIN CATEGORY_TRN_/*$domainId*/ CAT1 ON (SLIP.TAX_SHIFT_CATEGORY = CAT1.CATEGORY_CODE AND CAT1.CATEGORY_ID = /*taxShiftCategory*/29)
	LEFT OUTER JOIN CATEGORY_TRN_/*$domainId*/ CAT2 ON (CONCAT(SLIP.BILL_CUTOFF_GROUP,SLIP.PAYBACK_CYCLE_CATEGORY) = CAT2.CATEGORY_CODE AND CAT2.CATEGORY_ID = /*cutoffCategory*/11)
	LEFT OUTER JOIN CATEGORY_TRN_/*$domainId*/ CAT3 ON (SLIP.SALES_CM_CATEGORY = CAT3.CATEGORY_CODE AND CAT3.CATEGORY_ID = /*salesCmCategory*/32)
	LEFT OUTER JOIN CATEGORY_TRN_/*$domainId*/ CATA ON (LINE.DELIVERY_PROCESS_CATEGORY = CATA.CATEGORY_CODE AND CATA.CATEGORY_ID = /*deliveryProcessCategory*/30)
	LEFT OUTER JOIN RO_SLIP_TRN_/*$domainId*/ RO_SLIP ON SLIP.RO_SLIP_ID = RO_SLIP.RO_SLIP_ID
	LEFT OUTER JOIN RO_LINE_TRN_/*$domainId*/ RO_LINE ON LINE.RO_LINE_ID = RO_LINE.RO_LINE_ID
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
/*IF rowCount != null && offsetRow != null */
LIMIT /*rowCount*/ OFFSET /*offsetRow*/
/*END*/