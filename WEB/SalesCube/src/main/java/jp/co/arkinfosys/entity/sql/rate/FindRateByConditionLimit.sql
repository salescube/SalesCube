SELECT
        A.RATE_ID RATE_ID
        ,A.NAME NAME
        ,E.RATE RATE
        ,DATE_FORMAT(D.START_DATE, '%Y/%m/%d') START_DATE
        ,A.REMARKS REMARKS
        ,A.UPD_DATETM UPD_DATETM
    FROM
        RATE_MST_/*$domainId*/ A
        LEFT OUTER JOIN (
            SELECT
                    B.RATE_ID
                    ,B.START_DATE
                    ,B.RATE
                FROM
                    RATE_TRN_/*$domainId*/ B
                WHERE
                    B.START_DATE >= ALL (
                        SELECT
                                START_DATE
                            FROM
                                RATE_TRN_/*$domainId*/ C
                            WHERE
                                B.RATE_ID = C.RATE_ID
                    )
        ) D
            ON (A.RATE_ID = D.RATE_ID)
        LEFT OUTER JOIN (
            SELECT
            	EA.RATE_ID
            	,EA.RATE
            FROM
              RATE_TRN_/*$domainId*/ EA,
	            (SELECT
                    RATE_ID
                    ,MAX(START_DATE) START_DATE
                FROM
                    RATE_TRN_/*$domainId*/
                WHERE
                    START_DATE <= DATE_FORMAT(now(), '%Y-%m-%d')
                GROUP BY
                    RATE_ID) EB
           WHERE
             EA.RATE_ID = EB.RATE_ID
             AND EA.START_DATE = EB.START_DATE
       ) E
            ON (A.RATE_ID = E.RATE_ID)
    /*BEGIN*/
    WHERE
    	/*IF rateId != null */
    	A.RATE_ID=/*rateId*/0
    	/*END*/
    	/*IF name != null*/
    	AND NAME LIKE /*name*/'S%'
    	/*END*/
    	/*IF remarks != null*/
    	AND REMARKS LIKE /*remarks*/'%S%'
    	/*END*/
    	/*IF startDate1 != null*/
    	AND START_DATE >= CAST(/*startDate1*/'2000/01/01' AS DATE)
    	/*END*/
    	/*IF startDate2 != null*/
    	AND START_DATE <= CAST(/*startDate2*/'2000/01/01' AS DATE)
    	/*END*/
    /*END*/
   	/*BEGIN*/
    ORDER BY
		/*IF sortColumnRate != null */
    	/*$sortColumnRate*/
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
