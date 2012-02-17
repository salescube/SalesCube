SELECT
	V.CUSTOMER_CODE
	,V.CUSTOMER_NAME
	,V.COV_PRICE
    ,V.BILL_CUTOFF_DATE
FROM
	(
		SELECT
			C.CUSTOMER_CODE,
			C.CUSTOMER_NAME,
        	(
          		SELECT
		  	  		A.COV_PRICE
  		  		FROM
	  		  		ART_BALANCE_TRN_/*$domainId*/ A
          		WHERE
		        	C.CUSTOMER_CODE = A.CUSTOMER_CODE
			      	AND A.ART_CUTOFF_DATE = /*lastCutoffDate*/'2010/01/10'
		    ) AS COV_PRICE
        	,(
          		SELECT
		  	  		A2.ART_CUTOFF_DATE
  		  		FROM
	  		  		ART_BALANCE_TRN_/*$domainId*/ A2
          		WHERE
		        	C.CUSTOMER_CODE = A2.CUSTOMER_CODE
			      	AND A2.ART_CUTOFF_DATE = /*lastCutoffDate*/'2010/01/10'
		    ) AS BILL_CUTOFF_DATE
      	FROM
      		CUSTOMER_MST_/*$domainId*/ C
      ) V
WHERE
    ( V.COV_PRICE is not null or V.BILL_CUTOFF_DATE is not null )
ORDER BY
	V.CUSTOMER_CODE ASC
