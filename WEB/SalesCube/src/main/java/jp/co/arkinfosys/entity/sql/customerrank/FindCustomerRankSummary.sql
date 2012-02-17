SELECT
	CUSTOMER_CODE,
	CUSTOMER_NAME,
	CUSTOMER_CRE_USER,
	NAME_KNJ,
	RANK_CODE,
	RANK_NAME,
	SALES_PRICE_6,
	SALES_PRICE_5,
	SALES_PRICE_4,
	SALES_PRICE_3,
	SALES_PRICE_2,
	SALES_PRICE_1,
	SALES_PRICE_LSM,
	FIRST_SALES_DATE,
	LAST_SALES_DATE,
	ENROLL_TERM_SPAN,
	DEFECT_TERM_SPAN,
	CRE_DATETM
FROM
	CUSTOMER_RANK_SUMMARY_/*$domainId*/
WHERE
	RANK_CODE IN (
		SELECT
			RANK_CODE
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
	)
ORDER BY
	CUSTOMER_CODE
