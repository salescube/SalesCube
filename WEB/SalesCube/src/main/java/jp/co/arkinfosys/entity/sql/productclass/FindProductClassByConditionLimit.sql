SELECT
        A.CLASS_CODE_1 CLASS_CODE_1
        ,CASE A.CLASS_CODE_1
        	WHEN '' THEN ''
        	ELSE B.CLASS_NAME
        END CLASS_NAME_1
        ,A.CLASS_CODE_2 CLASS_CODE_2
        ,CASE A.CLASS_CODE_2
        	WHEN '' THEN ''
        	ELSE C.CLASS_NAME
        END CLASS_NAME_2
        ,A.CLASS_CODE_3 CLASS_CODE_3
        ,CASE A.CLASS_CODE_3
        	WHEN '' THEN ''
        	ELSE A.CLASS_NAME
        END CLASS_NAME_3
        ,A.CRE_FUNC
        ,A.CRE_DATETM
        ,A.CRE_USER
        ,A.UPD_FUNC
        ,A.UPD_DATETM
        ,A.UPD_USER
    FROM
        PRODUCT_CLASS_MST_/*$domainId*/ A INNER JOIN PRODUCT_CLASS_MST_/*$domainId*/ B
            ON (
                A.CLASS_CODE_1 = B.CLASS_CODE_1
                AND B.CLASS_CODE_2 = ''
                AND B.CLASS_CODE_3 = ''
            ) INNER JOIN PRODUCT_CLASS_MST_/*$domainId*/ C
            ON (
                A.CLASS_CODE_1 = C.CLASS_CODE_1
                AND A.CLASS_CODE_2 = C.CLASS_CODE_2
                AND C.CLASS_CODE_3 = ''
            )
	/*BEGIN*/
    WHERE
    	/*IF classCode1 != null */
    	A.CLASS_CODE_1 = /*classCode1*/'S'
    	/*END*/
    	/*IF classCode2 != null */
    	AND A.CLASS_CODE_2 = /*classCode2*/'S'
    	/*END*/
    	/*IF classCode3 != null */
    	AND A.CLASS_CODE_3 = /*classCode3*/'S'
    	/*END*/
    	/*IF classCode != null */
    	AND ( A.CLASS_CODE_1 LIKE /*classCode*/'S%' OR A.CLASS_CODE_2 LIKE /*classCode*/'S%' OR A.CLASS_CODE_3 LIKE /*classCode*/'S%' )
    	/*END*/
    	/*IF className != null */
    	AND A.CLASS_NAME LIKE /*className*/'%S%'
    	/*END*/
    /*END*/
	/*BEGIN*/
		ORDER BY
			/*IF sortColumn != null */
				/*$sortColumn*/
				/*IF sortOrder != null*/
				/*$sortOrder*/
				-- ELSE ASC
				/*END*/
			/*END*/
	/*END*/
	/*BEGIN*/
	/*IF rowCount != null*/
	LIMIT /*rowCount*/
	/*IF offsetRow != null*/
	OFFSET /*offsetRow*/
	/*END*/
	/*END*/
	/*END*/
