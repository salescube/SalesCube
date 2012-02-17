SELECT
        CLASS_CODE_1
        ,CLASS_CODE_2
        ,CLASS_CODE_3
        ,CLASS_NAME
        ,CRE_FUNC
        ,CRE_DATETM
        ,CRE_USER
        ,UPD_FUNC
        ,UPD_DATETM
        ,UPD_USER
    FROM
        PRODUCT_CLASS_MST_/*$domainId*/
    WHERE
    	CLASS_CODE_1 = /*classCode1*/
    	AND CLASS_CODE_2 <> ''
    	AND CLASS_CODE_3 = ''
    ORDER BY
    	 CLASS_CODE_2 ASC