SELECT
	COUNT(*)
    FROM
        BANK_MST_/*$domainId*/ B LEFT OUTER JOIN CATEGORY_TRN_/*$domainId*/ C ON (B.DWB_TYPE=C.CATEGORY_CODE AND C.CATEGORY_ID=/*categoryId*/)
    /*BEGIN*/
    WHERE
    	/*IF bankId != null*/
    	B.BANK_ID = /*bankId*/'S%'
    	/*END*/
    	/*IF bankCode != null*/
    	B.BANK_CODE LIKE /*bankCode*/'S%'
    	/*END*/
    	/*IF bankName != null*/
    	AND B.BANK_NAME LIKE /*bankName*/'%S%'
    	/*END*/
    	/*IF storeCode != null*/
    	AND B.STORE_CODE LIKE /*storeCode*/'%S%'
    	/*END*/
    	/*IF storeName != null*/
    	AND B.STORE_NAME LIKE /*storeName*/'%S%'
    	/*END*/
    	/*IF dwbType != null*/
    	AND B.DWB_TYPE = /*dwbType*/'%S%'
    	/*END*/
    	/*IF accountNum != null*/
    	AND B.ACCOUNT_NUM = /*accountNum*/'%S%'
    	/*END*/
    	/*IF accountOwnerName != null*/
    	AND B.ACCOUNT_OWNER_NAME LIKE /*accountOwnerName*/'%S%'
    	/*END*/
    	/*IF accountOwnerKana != null*/
    	AND B.ACCOUNT_OWNER_KANA LIKE /*accountOwnerKana*/'%S%'
    	/*END*/
    /*END*/
