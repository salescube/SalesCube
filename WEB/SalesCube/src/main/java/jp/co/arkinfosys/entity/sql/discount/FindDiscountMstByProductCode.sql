SELECT
        R.PRODUCT_CODE
        ,D.DISCOUNT_ID
        ,D.DISCOUNT_NAME
        ,D.REMARKS
        ,D.USE_FLAG
    FROM
    	DISCOUNT_REL_/*$domainId*/ R
            LEFT OUTER JOIN DISCOUNT_MST_/*$domainId*/ D
            	ON R.DISCOUNT_ID = D.DISCOUNT_ID
    WHERE
		D.USE_FLAG = '1'
    	AND R.PRODUCT_CODE = /*productCode*/'P%'
