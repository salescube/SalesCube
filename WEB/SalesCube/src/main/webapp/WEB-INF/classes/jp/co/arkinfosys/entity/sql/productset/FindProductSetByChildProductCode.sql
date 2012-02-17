SELECT
        P.PRODUCT_CODE AS SET_PRODUCT_CODE
        ,P.PRODUCT_NAME AS SET_PRODUCT_NAME
        ,P.CRE_DATETM AS SET_PRODUCT_CRE_DATETM
        ,CASE
        	WHEN PS.UPD_DATETM IS NULL THEN
        		P.UPD_DATETM
        	ELSE
        		PS.UPD_DATETM
        END AS SET_PRODUCT_UPD_DATETM
        ,PS.PRODUCT_CODE
        ,P2.PRODUCT_NAME AS PRODUCT_NAME
        ,PS.QUANTITY
        ,PS.CRE_FUNC
        ,PS.CRE_DATETM
        ,PS.CRE_USER
        ,PS.UPD_FUNC
        ,PS.UPD_DATETM
        ,PS.UPD_USER
    FROM
    	PRODUCT_MST_/*$domainId*/ P
    		INNER JOIN PRODUCT_SET_MST_/*$domainId*/ PS
                ON P.PRODUCT_CODE = PS.SET_PRODUCT_CODE
            LEFT OUTER JOIN PRODUCT_MST_/*$domainId*/ P2
                ON PS.PRODUCT_CODE = P2.PRODUCT_CODE AND P2.SET_TYPE_CATEGORY = /*setTypeCategorySingle*/'0'
    WHERE
        PS.PRODUCT_CODE = /*productCode*/'S'
        AND P.SET_TYPE_CATEGORY = /*setTypeCategorySet*/'1'
    ORDER BY
    	PS.PRODUCT_CODE ASC