SELECT
	RANK_CODE
	,RANK_NAME
	,RANK_RATE
	,RO_COUNT_FROM
	,RO_COUNT_TO
	,ENROLL_TERM_FROM
	,ENROLL_TERM_TO
	,DEFECT_TERM_FROM
	,DEFECT_TERM_TO
	,RO_MONTHLY_AVG_FROM
	,RO_MONTHLY_AVG_TO
	,POSTAGE_TYPE
	,REMARKS
    ,CR.CRE_FUNC
    ,CR.CRE_DATETM
    ,CR.CRE_USER
    ,CR.UPD_FUNC
    ,CR.UPD_DATETM
    ,CR.UPD_USER
    FROM
        CUSTOMER_RANK_MST_/*$domainId*/ CR
    /*BEGIN*/
    WHERE
    	/*IF rankCode != null*/
    	CR.RANK_CODE LIKE /*rankCode*/'S%'
    	/*END*/
    	/*IF rankName != null*/
    	AND CR.RANK_NAME LIKE /*rankName*/'%S%'
    	/*END*/
    	/*IF rankRate1 != null*/
    	AND CR.RANK_RATE >= /*rankRate1*/0
    	/*END*/
    	/*IF rankRate2 != null*/
    	AND CR.RANK_RATE <= /*rankRate2*/
    	/*END*/
    	/*IF postageType != null*/
    	AND CR.POSTAGE_TYPE = /*postageType*/
    	/*END*/
    /*END*/
   	/*BEGIN*/
    ORDER BY
		/*IF sortColumnRank != null */
    	/*$sortColumnRank*/
		/*END*/

		/*IF sortOrder != null*/
		/*$sortOrder*/
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
