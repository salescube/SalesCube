SELECT
        COUNT(*) AS COUNT
    FROM
        CUSTOMER_MST_/*$domainId*/ C
    /*BEGIN*/
    WHERE
		/*IF customerCode != null */
		C.CUSTOMER_CODE LIKE /*customerCode*/'S%'
		/*END*/
		/*IF customerName != null */
		AND C.CUSTOMER_NAME LIKE /*customerName*/'%S%'
		/*END*/
		/*IF customerKana != null */
		AND C.CUSTOMER_KANA LIKE /*customerKana*/'%S%'
		/*END*/
		/*IF customerOfficeName != null */
		AND C.CUSTOMER_OFFICE_NAME LIKE /*customerOfficeName*/'%S%'
		/*END*/
		/*IF customerOfficeKana != null */
		AND C.CUSTOMER_OFFICE_KANA LIKE /*customerOfficeKana*/'%S%'
		/*END*/
		/*IF customerPcName != null */
		AND C.CUSTOMER_PC_NAME LIKE /*customerPcName*/'%S%'
		/*END*/
		/*IF customerPcKana != null */
		AND C.CUSTOMER_PC_KANA LIKE /*customerPcKana*/'%S%'
		/*END*/
		/*IF customerTel != null */
		AND REPLACE(REPLACE(REPLACE(C.CUSTOMER_TEL, '-', ''), '(', ''), ')', '') = /*customerTel*/'S'
		/*END*/
		/*IF customerFax != null */
		AND REPLACE(REPLACE(REPLACE(C.CUSTOMER_FAX, '-', ''), '(', ''), ')', '') = /*customerFax*/'S'
		/*END*/
		/*IF customerRankCategory != null */
		AND C.CUSTOMER_RANK_CATEGORY = /*customerRankCategory*/'00'
		/*END*/
		/*IF paybackCycleCategory != null */
		AND C.PAYBACK_CYCLE_CATEGORY = /*paybackCycleCategory*/'00'
		/*END*/
		/*IF cutoffGroup != null */
		AND C.CUTOFF_GROUP = /*cutoffGroup*/'00'
		/*END*/
		/*IF paymentName != null */
		AND C.PAYMENT_NAME LIKE /*paymentName*/'%S%'
		/*END*/
		/*IF remarks != null */
		AND C.REMARKS LIKE /*remarks*/'%S%'
		/*END*/
	/*END*/