SELECT
ESTIMATE_SHEET_ID
,ESTIMATE_DATE
,DELIVERY_INFO
,VALID_DATE
,USER_ID
,USER_NAME
,REMARKS
,TITLE
,ESTIMATE_CONDITION
,DELIVERY_NAME
,SUBMIT_NAME
,SUBMIT_PRE
,CUSTOMER_CODE
,CUSTOMER_NAME
,RETAIL_PRICE_TOTAL-COST_TOTAL AS GROSS_MARGIN
,(RETAIL_PRICE_TOTAL-COST_TOTAL)/RETAIL_PRICE_TOTAL AS GROSS_MARGIN_RATE
,RETAIL_PRICE_TOTAL
,CTAX_PRICE_TOTAL
,CTAX_RATE
,ESTIMATE_TOTAL
    FROM
        ESTIMATE_SHEET_TRN_/*$domainId*/'DEFAULT'
    /*BEGIN*/
	WHERE
		/*IF estimateSheetId != null */
		ESTIMATE_SHEET_ID LIKE /*estimateSheetId*/'0'
		/*END*/
		/*IF estimateDateFrom != null */
		AND ESTIMATE_DATE >= CAST(/*estimateDateFrom*/ AS DATE)
		/*END*/
		/*IF estimateDateTo != null */
		AND ESTIMATE_DATE <= CAST(/*estimateDateTo*/ AS DATE)
		/*END*/
		/*IF validDateFrom != null */
		AND VALID_DATE >= CAST(/*validDateFrom*/ AS DATE)
		/*END*/
		/*IF validDateTo != null */
		AND VALID_DATE <= CAST(/*validDateTo*/ AS DATE)
		/*END*/
		/*IF userId != null */
		AND USER_ID LIKE /*userId*/'S%'
		/*END*/
		/*IF userName != null */
		AND USER_NAME LIKE /*userName*/'%S%'
		/*END*/
		/*IF title != null */
		AND TITLE LIKE /*title*/'%S%'
		/*END*/
		/*IF remarks != null */
		AND REMARKS LIKE /*remarks*/'%S%'
		/*END*/
		/*IF submitName != null */
		AND SUBMIT_NAME LIKE /*submitName*/'%S%'
		/*END*/
		/*IF customerCode != null */
		AND CUSTOMER_CODE LIKE /*customerCode*/'S%'
		/*END*/
		/*IF customerName != null */
		AND CUSTOMER_NAME LIKE /*customerName*/'%S%'
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
	/*END*/
	/*IF rowCount != null && offsetRow != null */
	LIMIT /*rowCount*/ OFFSET /*offsetRow*/
	/*END*/


