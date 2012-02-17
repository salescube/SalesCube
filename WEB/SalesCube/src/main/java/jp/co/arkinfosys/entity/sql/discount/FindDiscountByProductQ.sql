SELECT
        R.PRODUCT_CODE
        ,D.DISCOUNT_ID
        ,D.DISCOUNT_NAME
        ,D.REMARKS
        ,D.USE_FLAG
        ,DT.DISCOUNT_DATA_ID
        ,DT.LINE_NO
        ,DT.DATA_FROM
        ,DT.DATA_TO
        ,DT.DISCOUNT_RATE
    FROM
    	DISCOUNT_REL_/*$domainId*/ R
            LEFT OUTER JOIN DISCOUNT_MST_/*$domainId*/ D
            	ON R.DISCOUNT_ID = D.DISCOUNT_ID
            LEFT OUTER JOIN DISCOUNT_TRN_/*$domainId*/ DT
                ON R.DISCOUNT_ID = DT.DISCOUNT_ID
    WHERE
		D.USE_FLAG = '1'
    	AND R.PRODUCT_CODE = /*productCode*/'P%'
    	AND DT.DATA_FROM <= /*quantity*/'1'
    	AND DT.DATA_TO >= /*quantity*/'1'
