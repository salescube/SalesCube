SELECT
	COUNT(*)
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
    	AND CR.POSTAGE_TYPE=/*postageType*/
    	/*END*/
    /*END*/
