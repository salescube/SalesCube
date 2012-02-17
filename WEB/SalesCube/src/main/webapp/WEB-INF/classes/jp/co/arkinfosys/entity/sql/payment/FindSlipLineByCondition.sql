SELECT
	DISTINCT
	CONCAT(CAST(LINE.PAYMENT_SLIP_ID AS CHAR),' - ',CAST(LINE.LINE_NO AS CHAR)) AS PAYMENT_SLIP_ID,
	SLIP_STAT.CATEGORY_CODE_NAME AS STATUS,
	SLIP.PAYMENT_DATE,
	SLIP.USER_ID,
	SLIP.USER_NAME,
	SLIP.REMARKS,
	SLIP.SUPPLIER_CODE,
	SLIP.SUPPLIER_NAME,
	SLIP.PRICE_TOTAL,
	SLIP.PO_SLIP_ID,
	SLIP.PAYMENT_CUTOFF_DATE,
	SLIP.PAYMENT_PDATE,
	LINE_STAT.CATEGORY_CODE_NAME AS LINE_STATUS,
	PAY_CAT.CATEGORY_CODE_NAME AS PAYMENT_CATEGORY,
	LINE.PRODUCT_CODE,
	LINE.PRODUCT_ABSTRACT,
	LINE.QUANTITY,
	LINE.UNIT_PRICE,
	LINE.PRICE,
	LINE.DOL_UNIT_PRICE,
	LINE.DOL_PRICE,
	CONCAT(CAST(PO_LINE.PO_SLIP_ID AS CHAR),' - ',CAST(PO_LINE.LINE_NO AS CHAR)) AS PO_LINE_ID,
	CONCAT(CAST(SUP_LINE.SUPPLIER_SLIP_ID AS CHAR),' - ',CAST(SUP_LINE.LINE_NO AS CHAR)) AS SUPPLIER_LINE_ID,
	LINE.REMARKS AS LINE_REMARKS,
	LINE.RATE,
	LINE.PAYMENT_SLIP_ID AS SORT_PAYMENT_SLIP_ID,
	LINE.LINE_NO AS SORT_PAYMENT_LINE_NO,
	PO_LINE.PO_SLIP_ID AS SORT_PO_SLIP_ID,
	PO_LINE.LINE_NO AS SORT_PO_LINE_NO,
	SUP_LINE.SUPPLIER_SLIP_ID AS SORT_SUPPLIER_SLIP_ID,
	SUP_LINE.LINE_NO AS SORT_SUPPLIER_LINE_NO
FROM
	PAYMENT_SLIP_TRN_/*$domainId*/ SLIP
	INNER JOIN PAYMENT_LINE_TRN_/*$domainId*/ LINE ON SLIP.PAYMENT_SLIP_ID = LINE.PAYMENT_SLIP_ID
	LEFT OUTER JOIN SUPPLIER_MST_/*$domainId*/ SUPP ON SLIP.SUPPLIER_CODE = SUPP.SUPPLIER_CODE
	LEFT OUTER JOIN PO_LINE_TRN_/*$domainId*/ PO_LINE ON LINE.PO_LINE_ID = PO_LINE.PO_LINE_ID
	LEFT OUTER JOIN SUPPLIER_LINE_TRN_/*$domainId*/ SUP_LINE ON LINE.SUPPLIER_LINE_ID = SUP_LINE.SUPPLIER_LINE_ID
	LEFT OUTER JOIN CATEGORY_TRN_/*$domainId*/ SLIP_STAT ON (SLIP.STATUS = SLIP_STAT.CATEGORY_CODE AND SLIP_STAT.CATEGORY_ID = /*paymentSlipStatus*/104)
	LEFT OUTER JOIN CATEGORY_TRN_/*$domainId*/ PAY_CAT ON (LINE.PAYMENT_CATEGORY = PAY_CAT.CATEGORY_CODE AND PAY_CAT.CATEGORY_ID = /*paymentDetail*/5)
	LEFT OUTER JOIN CATEGORY_TRN_/*$domainId*/ LINE_STAT ON (LINE.STATUS = LINE_STAT.CATEGORY_CODE AND LINE_STAT.CATEGORY_ID = /*paymentLineStatus*/105)
	LEFT OUTER JOIN PRODUCT_MST_/*$domainId*/ PROD ON LINE.PRODUCT_CODE = PROD.PRODUCT_CODE
/*BEGIN*/
WHERE
	/*IF paymentSlipId != null */
	AND SLIP.PAYMENT_SLIP_ID = /*paymentSlipId*/1
	/*END*/
	/*IF poSlipId != null */
	AND SLIP.PO_SLIP_ID = /*poSlipId*/1
	/*END*/
	/*IF supplierSlipId != null */
	AND SUP_LINE.SUPPLIER_SLIP_ID = /*supplierSlipId*/1
	/*END*/
	/*IF paymentDateFrom != null */
	AND SLIP.PAYMENT_DATE >= CAST(/*paymentDateFrom*/'2000/01/01' AS DATE)
	/*END*/
	/*IF paymentDateTo != null */
	AND SLIP.PAYMENT_DATE <= CAST(/*paymentDateTo*/'2000/01/01' AS DATE)
	/*END*/
	/*IF supplierCode != null */
	AND SLIP.SUPPLIER_CODE LIKE /*supplierCode*/'S%'
	/*END*/
	/*IF supplierName != null */
	AND SLIP.SUPPLIER_NAME LIKE /*supplierName*/'%S%'
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