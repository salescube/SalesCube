SELECT COUNT(*)
    FROM
        RATE_MST_/*$domainId*/ A LEFT OUTER JOIN (
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
    	/*IF name != null*/
    	NAME LIKE /*name*/'S%'
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
