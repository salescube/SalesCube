SELECT
        D.DISCOUNT_ID
        ,D.DISCOUNT_NAME
        ,D.REMARKS
        ,D.USE_FLAG
        ,C.CATEGORY_CODE_NAME AS USE_FLAG_NAME
        ,D.CRE_FUNC
        ,D.CRE_DATETM
        ,D.CRE_USER
        ,D.UPD_FUNC
        ,D.UPD_DATETM
        ,D.UPD_USER
    FROM
        DISCOUNT_MST_/*$domainId*/ D
            LEFT OUTER JOIN CATEGORY_TRN_/*$domainId*/ C
            	ON D.USE_FLAG = C.CATEGORY_CODE AND C.CATEGORY_ID = /*categoryId*/0
    /*BEGIN*/
    WHERE
    	/*IF discountId != null*/
    	D.DISCOUNT_ID LIKE /*discountId*/'S%'
    	/*END*/
    /*END*/