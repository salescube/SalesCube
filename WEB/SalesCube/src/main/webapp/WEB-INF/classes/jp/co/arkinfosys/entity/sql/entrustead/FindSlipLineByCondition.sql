SELECT DISTINCT
	SLIP.ENTRUST_EAD_SLIP_ID,
	SLIP.ENTRUST_EAD_DATE,
	SLIP.ENTRUST_EAD_ANNUAL,
	SLIP.ENTRUST_EAD_MONTHLY,
	SLIP.ENTRUST_EAD_YM,
	SLIP.USER_ID,
	SLIP.USER_NAME,
	SLIP.ENTRUST_EAD_CATEGORY,
	CAT.CATEGORY_CODE_NAME AS ENTRUST_EAD_CATEGORY_NAME,
	SLIP.REMARKS,
	SLIP.PO_SLIP_ID,

	LINE.ENTRUST_EAD_LINE_ID,
	LINE.LINE_NO,
	CONCAT(LINE.ENTRUST_EAD_LINE_ID, '-', LINE.LINE_NO) AS ENTRUST_EAD_LINE_ID_NO,

	LINE.PRODUCT_CODE,
	LINE.PRODUCT_ABSTRACT,
	LINE.QUANTITY,
	LINE.REMARKS AS LINE_REMARKS,

	LINE.PO_LINE_ID,
	PLINE.LINE_NO AS PO_LINE_NO,
	CONCAT(LINE.PO_LINE_ID, '-', PLINE.LINE_NO) AS PO_LINE_ID_NO,

	LINE.REL_ENTRUST_EAD_LINE_ID,
	LINE2.ENTRUST_EAD_SLIP_ID AS REL_ENTRUST_EAD_SLIP_ID,

	LINE.ENTRUST_EAD_CATEGORY AS LINE_ENTRUST_EAD_CATEGORY,
	CAT2.CATEGORY_CODE_NAME AS LINE_ENTRUST_EAD_CATEGORY_NAME,

	LINE.CRE_FUNC,
	LINE.CRE_DATETM,
	LINE.CRE_USER,
	LINE.UPD_FUNC,
	LINE.UPD_DATETM,
	LINE.UPD_USER,
	SLIP.ENTRUST_EAD_SLIP_ID AS SORT_ENTRUST_EAD_SLIP_ID,
	LINE.LINE_NO AS SORT_ENTRUST_EAD_LINE_NO,
	SLIP.PO_SLIP_ID AS SORT_PO_SLIP_ID,
	PLINE.LINE_NO AS SORT_PO_LINE_NO

FROM
	ENTRUST_EAD_SLIP_TRN_/*$domainId*/ SLIP
	INNER JOIN ENTRUST_EAD_LINE_TRN_/*$domainId*/ LINE ON SLIP.ENTRUST_EAD_SLIP_ID = LINE.ENTRUST_EAD_SLIP_ID
	LEFT OUTER JOIN PRODUCT_MST_/*$domainId*/ PROD ON LINE.PRODUCT_CODE = PROD.PRODUCT_CODE
	LEFT OUTER JOIN SUPPLIER_MST_/*$domainId*/ SUPP ON PROD.SUPPLIER_CODE = SUPP.SUPPLIER_CODE
	LEFT OUTER JOIN CATEGORY_TRN_/*$domainId*/ CAT ON
			SLIP.ENTRUST_EAD_CATEGORY = CAT.CATEGORY_CODE AND CAT.CATEGORY_ID = /*entrustEadCategoryId*/
	LEFT OUTER JOIN CATEGORY_TRN_/*$domainId*/ CAT2 ON
			LINE.ENTRUST_EAD_CATEGORY = CAT2.CATEGORY_CODE AND CAT2.CATEGORY_ID = /*entrustEadCategoryId*/
	LEFT OUTER JOIN ENTRUST_EAD_LINE_TRN_/*$domainId*/ LINE2 ON LINE.REL_ENTRUST_EAD_LINE_ID = LINE2.ENTRUST_EAD_LINE_ID
	LEFT OUTER JOIN PO_LINE_TRN_/*$domainId*/ PLINE ON PLINE.PO_LINE_ID = LINE.PO_LINE_ID
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
/*BEGIN*/
	ORDER BY
	/*IF sortColumn != null */
		/*$sortColumn*/
		/*IF sortOrder != null*/
		/*$sortOrder*/
		-- ELSE ASC
		/*END*/
	/*END*/

	/*IF sortColumnSlip != null */
		/*$sortColumnSlip*/
		/*IF sortOrder != null*/
			/*$sortOrder*/
			-- ELSE ASC
		/*END*/
	/*END*/
	/*IF sortColumnLine != null */
		/*IF sortColumnSlip != null */,/*END*/
		/*$sortColumnLine*/
		/*IF sortOrder != null*/
		/*$sortOrder*/
		-- ELSE ASC
		/*END*/
	/*END*/
/*END*/
/*BEGIN*/
	/*IF rowCount != null && offsetRow != null */
	LIMIT /*rowCount*/ OFFSET /*offsetRow*/
	/*END*/
/*END*/
