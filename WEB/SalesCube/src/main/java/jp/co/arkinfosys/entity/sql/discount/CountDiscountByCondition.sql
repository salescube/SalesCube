SELECT
	COUNT(*)
    FROM
        DISCOUNT_MST_/*$domainId*/ D
    /*BEGIN*/
    WHERE
    	/*IF discountId != null*/
    	D.DISCOUNT_ID LIKE /*discountId*/'S%'
    	/*END*/
    	/*IF discountName != null*/
    	AND D.DISCOUNT_NAME LIKE /*discountName*/'%S%'
    	/*END*/
    	/*IF useFlag != null*/
    	AND D.USE_FLAG = /*useFlag*/'0'
    	/*END*/
    	/*IF remarks != null*/
    	AND D.REMARKS LIKE /*remarks*/'%S%'
    	/*END*/
    /*END*/
