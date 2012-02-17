SELECT
        C.CUSTOMER_CODE
        ,C.CUSTOMER_NAME
        ,C.CUSTOMER_KANA
        ,C.CUSTOMER_OFFICE_NAME
        ,C.CUSTOMER_OFFICE_KANA
        ,C.CUSTOMER_ABBR
        ,C.CUSTOMER_DEPT_NAME
        ,C.CUSTOMER_ZIP_CODE
        ,C.CUSTOMER_ADDRESS_1
        ,C.CUSTOMER_ADDRESS_2
        ,C.CUSTOMER_PC_POST
        ,C.CUSTOMER_PC_NAME
        ,C.CUSTOMER_PC_KANA
        ,C.CUSTOMER_PC_PRE_CATEGORY
        ,C.CUSTOMER_TEL
        ,C.CUSTOMER_FAX
        ,C.CUSTOMER_EMAIL
        ,C.CUSTOMER_URL
        ,C.CUSTOMER_BUSINESS_CATEGORY
        ,C.CUSTOMER_JOB_CATEGORY
        ,C.CUSTOMER_RO_CATEGORY
        ,C.CUSTOMER_RANK_CATEGORY
        ,C.CUSTOMER_UPD_FLAG
        ,C.SALES_CM_CATEGORY
        ,C.TAX_SHIFT_CATEGORY
        ,C.RATE
        ,C.MAX_CREDIT_LIMIT
        ,C.LAST_CUTOFF_DATE
        ,C.CUTOFF_GROUP
        ,C.PAYBACK_TYPE_CATEGORY
        ,C.PAYBACK_CYCLE_CATEGORY
        ,C.TAX_FRACT_CATEGORY
        ,C.PRICE_FRACT_CATEGORY
        ,C.TEMP_DELIVERY_SLIP_FLAG
        ,C.PAYMENT_NAME
        ,C.REMARKS
        ,C.FIRST_SALES_DATE
        ,C.LAST_SALES_DATE
        ,C.SALES_PRICE_TOTAL
        ,C.SALES_PRICE_LSM
        ,C.COMMENT_DATA
		,C.CRE_FUNC
		,C.CRE_DATETM
		,C.CRE_USER
		,C.UPD_FUNC
		,C.UPD_DATETM
		,C.UPD_USER
		,C.LAST_SALES_CUTOFF_DATE
        ,CR.RANK_CODE
        ,CR.RANK_NAME
        ,CATT.CATEGORY_ID
        ,CATT.CATEGORY_CODE
        ,CATT.CATEGORY_CODE_NAME
        ,CATT2.CATEGORY_ID CATEGORY_ID2
        ,CATT2.CATEGORY_CODE CATEGORY_CODE2
        ,CATT2.CATEGORY_CODE_NAME CATEGORY_CODE_NAME2
        ,CATT3.CATEGORY_ID CATEGORY_ID3
        ,CATT3.CATEGORY_CODE CATEGORY_CODE3
        ,CATT3.CATEGORY_CODE_NAME CATEGORY_CODE_NAME3
    FROM
        CUSTOMER_MST_/*$domainId*/ C
            LEFT OUTER JOIN CUSTOMER_RANK_MST_/*$domainId*/ CR
                ON C.CUSTOMER_RANK_CATEGORY = CR.RANK_CODE
            LEFT OUTER JOIN CATEGORY_TRN_/*$domainId*/ CATT
            	ON CONCAT(C.CUTOFF_GROUP, C.PAYBACK_CYCLE_CATEGORY) = CATT.CATEGORY_CODE AND CATT.CATEGORY_ID=/*categoryId*/11
            LEFT OUTER JOIN CATEGORY_TRN_/*$domainId*/ CATT2
            	ON C.TAX_SHIFT_CATEGORY = CATT2.CATEGORY_CODE AND CATT2.CATEGORY_ID=/*categoryId2*/29
            LEFT OUTER JOIN CATEGORY_TRN_/*$domainId*/ CATT3
            	ON C.SALES_CM_CATEGORY = CATT3.CATEGORY_CODE AND CATT3.CATEGORY_ID=/*categoryId3*/32
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
	/*BEGIN*/
	ORDER BY
		/*IF sortColumnCustomer != null */
		C./*$sortColumnCustomer*/
		/*END*/

		/*IF sortColumnRank != null */
		CR./*sortColumnRank*/
		/*END*/

		/*IF sortOrder != null*/
		/*$sortOrder*/
		/*END*/
	/*END*/

	/*BEGIN*/
	/*IF rowCount != null*/
	LIMIT /*rowCount*/
	/*IF offsetRow != null*/
	OFFSET /*offsetRow*/
	/*END*/
	/*END*/
	/*END*/
