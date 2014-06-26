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

    	/*IF classCode1 != null */
    	CLASS_CODE_1 = /*classCode1*/'S'
    	/*END*/
    	/*IF classCode2 != null */
    	AND CLASS_CODE_2 = /*classCode2*/'S'
    	/*END*/

    	AND CLASS_CODE_3 <> ''
    	
    	ORDER BY
    	 CLASS_CODE_1 ASC
        ,CLASS_CODE_2 ASC
        ,CLASS_CODE_3 ASC
