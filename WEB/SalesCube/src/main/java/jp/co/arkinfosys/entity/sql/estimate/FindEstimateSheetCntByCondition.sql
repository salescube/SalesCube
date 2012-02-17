SELECT COUNT(*)
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

