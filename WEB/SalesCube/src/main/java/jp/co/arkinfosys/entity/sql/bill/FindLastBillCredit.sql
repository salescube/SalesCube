SELECT
	C.CUSTOMER_CODE
	,C.CUSTOMER_NAME
    ,D.BILL_CUTOFF_DATE
FROM
    CUSTOMER_MST_/*$domainId*/ C
       LEFT OUTER JOIN
		(
			SELECT
				C2.CUSTOMER_CODE
    			,MAX(B.BILL_CUTOFF_DATE) BILL_CUTOFF_DATE
    		FROM
        		CUSTOMER_MST_/*$domainId*/ C2
					LEFT OUTER JOIN BILL_TRN_/*$domainId*/ B
						ON C2.CUSTOMER_CODE = B.CUSTOMER_CODE
			WHERE
				C2.SALES_CM_CATEGORY = /*salesCmCategory*/'0'
				/*IF cutoffGroup != null */
				AND
				C2.CUTOFF_GROUP = /*cutoffGroup*/'10'
				/*END*/
				/*IF paybackCycleCategory != null */
				AND
				C2.PAYBACK_CYCLE_CATEGORY = /*paybackCycleCategory*/'1'
				/*END*/
				/*IF customerCode != null */
				AND
				C2.CUSTOMER_CODE LIKE /*customerCode*/'S%'
				/*END*/
				/*IF customerName != null */
				AND
				C2.CUSTOMER_NAME LIKE /*customerName*/'%hoge%'
				/*END*/
				/*IF billCrtCategory != null */
				AND
				B.BILL_CRT_CATEGORY = /*billCrtCategory*/'2'
				/*END*/
			GROUP BY
				C2.CUSTOMER_CODE
		) D
		ON C.CUSTOMER_CODE = D.CUSTOMER_CODE
WHERE
	C.SALES_CM_CATEGORY = /*salesCmCategory*/'0'
	/*IF cutoffGroup != null */
	AND
	C.CUTOFF_GROUP = /*cutoffGroup*/'10'
	/*END*/
	/*IF paybackCycleCategory != null */
	AND
	C.PAYBACK_CYCLE_CATEGORY = /*paybackCycleCategory*/'1'
	/*END*/
	/*IF customerCode != null */
	AND
	C.CUSTOMER_CODE LIKE /*customerCode*/'S%'
	/*END*/
	/*IF customerName != null */
	AND
	C.CUSTOMER_NAME LIKE /*customerName*/'%hoge%'
	/*END*/
/*BEGIN*/
ORDER BY
	/*IF sortColumnCustomerCode != null */
	C./*$sortColumnCustomerCode*/
	/*END*/
	/*IF sortColumnBillCutoffDate != null */
	D./*$sortColumnBillCutoffDate*/
	/*END*/

	/*IF sortOrder != null*/
	/*$sortOrder*/
	/*END*/
/*END*/
/*BEGIN*/
	LIMIT /*rowCount*/ OFFSET /*offsetRow*/
/*END*/
