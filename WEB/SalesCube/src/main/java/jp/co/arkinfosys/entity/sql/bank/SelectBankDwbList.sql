SELECT
		B.BANK_ID
		,B.BANK_CODE
		,B.BANK_NAME
		,B.STORE_CODE
		,B.STORE_NAME
		,B.DWB_TYPE
		,B.ACCOUNT_NUM
		,B.ACCOUNT_OWNER_NAME
		,B.ACCOUNT_OWNER_KANA
		,B.REMARKS
		,B.VALID
		,B.CRE_FUNC
		,B.CRE_DATETM
		,B.CRE_USER
		,B.UPD_FUNC
		,B.UPD_DATETM
		,B.UPD_USER
		,CT.CATEGORY_CODE_NAME
    FROM
        BANK_MST_/*$domainId*/ B
        	LEFT OUTER JOIN CATEGORY_TRN_/*$domainId*/ CT
        ON ( B.DWB_TYPE=CT.CATEGORY_CODE
        AND CT.CATEGORY_ID =/*categoryId*/ )
    /*BEGIN*/
    WHERE
    /*IF selBankId != null*/
        BANK_ID=/*selBankId*/
    /*END*/
    /*END*/
    ORDER BY
    	BANK_ID ASC