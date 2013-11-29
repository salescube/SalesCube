SELECT
        R.CUSTOMER_CODE
        ,R.CUST_REL_CATEGORY
        ,D.DELIVERY_CODE
        ,D.DELIVERY_NAME
        ,D.DELIVERY_KANA
        ,D.DELIVERY_OFFICE_NAME
        ,D.DELIVERY_OFFICE_KANA
        ,D.DELIVERY_DEPT_NAME
        ,D.DELIVERY_ZIP_CODE
        ,D.DELIVERY_ADDRESS_1
        ,D.DELIVERY_ADDRESS_2
        ,D.DELIVERY_PC_NAME
        ,D.DELIVERY_PC_KANA
        ,D.DELIVERY_PC_PRE_CATEGORY
        ,D.DELIVERY_TEL
        ,D.DELIVERY_FAX
        ,D.DELIVERY_EMAIL
        ,D.DELIVERY_URL
        ,D.REMARKS
		,D.CRE_FUNC
		,D.CRE_DATETM
		,D.CRE_USER
		,D.UPD_FUNC
		,D.UPD_DATETM
		,D.UPD_USER
		,C.CUSTOMER_NAME
		,C.SALES_CM_CATEGORY
		,C.PRICE_FRACT_CATEGORY
		,C.TAX_FRACT_CATEGORY
		,C.TAX_SHIFT_CATEGORY
		,C.PAYBACK_CYCLE_CATEGORY
		,C.CUTOFF_GROUP
		,C.CUSTOMER_RO_CATEGORY
		,C.BILL_PRINT_UNIT
		,C.REMARKS AS CUSTOMER_REMARKS
		,C.COMMENT_DATA AS CUSTOMER_COMMENT_DATA
        ,CATT.CATEGORY_CODE_NAME
        ,CATT2.CATEGORY_CODE_NAME SALES_CM_CATEGORY_NAME
        ,CATT3.CATEGORY_CODE_NAME CUSTOMER_PC_PRE_CATEGORY_NAME
    FROM
        CUSTOMER_REL_/*$domainId*/ R
            INNER JOIN DELIVERY_MST_/*$domainId*/ D
                ON R.REL_CODE = D.DELIVERY_CODE
            INNER JOIN CUSTOMER_MST_/*$domainId*/ C
                ON R.CUSTOMER_CODE = C.CUSTOMER_CODE
            LEFT OUTER JOIN CATEGORY_TRN_/*$domainId*/ CATT
            	ON D.DELIVERY_PC_PRE_CATEGORY = CATT.CATEGORY_CODE AND CATT.CATEGORY_ID=/*categoryId*/10
            LEFT OUTER JOIN CATEGORY_TRN_/*$domainId*/ CATT2
            	ON C.SALES_CM_CATEGORY = CATT2.CATEGORY_CODE AND CATT2.CATEGORY_ID=/*categoryId2*/35
            LEFT OUTER JOIN CATEGORY_TRN_/*$domainId*/ CATT3
            	ON C.CUSTOMER_PC_PRE_CATEGORY = CATT3.CATEGORY_CODE AND CATT3.CATEGORY_ID=/*categoryId*/10
    /*BEGIN*/
	WHERE
		/*IF customerCode != null */
		R.CUSTOMER_CODE LIKE /*customerCode*/'S%'
		/*END*/
		/*IF custRelCategory != null */
		AND R.CUST_REL_CATEGORY = /*custRelCategory*/'01'
		/*END*/
		/*IF deliveryCode != null */
		AND D.DELIVERY_CODE LIKE /*deliveryCode*/'D%'
		/*END*/
		/*IF deliveryName != null */
		AND D.DELIVERY_NAME = /*deliveryName*/'%D%'
		/*END*/
		/*IF deliveryKana != null */
		AND D.DELIVERY_KANA = /*deliveryKana*/'%D%'
		/*END*/
		/*IF deliveryOfficeName != null */
		AND D.DELIVERY_OFFICE_NAME = /*deliveryOfficeName*/'%D%'
		/*END*/
		/*IF deliveryOfficeKana != null */
		AND D.DELIVERY_OFFICE_KANA LIKE /*deliveryOfficeKana*/'%D%'
		/*END*/
		/*IF deliveryDeptName != null */
		AND D.DELIVERY_DEPT_NAME LIKE /*deliveryDeptName*/'%D%'
		/*END*/
		/*IF deliveryZipCode != null */
		AND D.DELIVERY_ZIP_CODE LIKE /*deliveryZipCode*/'0%'
		/*END*/
		/*IF deliveryAddress1 != null */
		AND D.DELIVERY_ADDRESS_1 LIKE /*deliveryAddress1*/'%D%'
		/*END*/
		/*IF deliveryAddress2 != null */
		AND D.DELIVERY_ADDRESS_2 LIKE /*deliveryAddress2*/'%D%'
		/*END*/
		/*IF deliveryPcName != null */
		AND D.DELIVERY_PC_NAME LIKE /*deliveryPcName*/'%D%'
		/*END*/
		/*IF deliveryPcKana != null */
		AND D.DELIVERY_PC_KANA LIKE /*deliveryPcKana*/'%S%'
		/*END*/
		/*IF deliveryPcPreCategory != null */
		AND D.DELIVERY_PC_PRE_CATEGORY = /*deliveryPcPreCategory*/'1'
		/*END*/
		/*IF deliveryTel != null */
		AND D.DELIVERY_TEL LIKE /*deliveryTel*/'0%'
		/*END*/
		/*IF deliveryFax != null */
		AND D.DELIVERY_FAX LIKE /*deliveryFax*/'0%'
		/*END*/
		/*IF deliveryEmail != null */
		AND D.DELIVERY_EMAIL LIKE /*deliveryEmail*/'D%'
		/*END*/
		/*IF deliveryUrl != null */
		AND D.DELIVERY_URL LIKE /*deliveryUrl*/'D%'
		/*END*/
		/*IF remarks != null */
		AND D.REMARKS LIKE /*remarks*/'%S%'
		/*END*/
	/*END*/
	/*BEGIN*/
	ORDER BY
		/*IF sortColumnDeleveryCode != null */
		D./*$sortColumnDeleveryCode*/
		/*END*/
		/*IF sortColumnDeleveryName != null */
		D./*$sortColumnDeleveryName*/
		/*END*/
		/*IF sortColumnCreDate != null */
		D./*$sortColumnCreDate*/
		/*END*/
		/*IF sortColumnUpdDate != null */
		D./*$sortColumnUpdDate*/
		/*END*/


		/*IF sortOrder != null*/
		/*$sortOrder*/
		/*END*/
	/*END*/