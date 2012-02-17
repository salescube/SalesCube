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
        ,DT.DISCOUNT_DATA_ID
        ,DT.LINE_NO
        ,DT.DATA_FROM
        ,DT.DATA_TO
        ,DT.DISCOUNT_RATE
    FROM
        DISCOUNT_MST_/*$domainId*/ D
            LEFT OUTER JOIN DISCOUNT_TRN_/*$domainId*/ DT
                ON D.DISCOUNT_ID = DT.DISCOUNT_ID
            LEFT OUTER JOIN CATEGORY_TRN_/*$domainId*/ C
            	ON D.USE_FLAG = C.CATEGORY_CODE AND C.CATEGORY_ID = /*categoryId*/0
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
   	/*BEGIN*/
    ORDER BY
		/*IF sortColumnDiscount != null */
    	D./*$sortColumnDiscount*/
		/*END*/

		/*IF sortOrder != null*/
		/*$sortOrder*/
		/*END*/
   	/*END*/