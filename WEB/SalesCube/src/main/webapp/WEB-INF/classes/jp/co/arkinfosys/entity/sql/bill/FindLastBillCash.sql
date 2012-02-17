SELECT
	MAX(B.BILL_CUTOFF_DATE) BILL_CUTOFF_DATE
FROM
	CUSTOMER_MST_/*$domainId*/ C
		LEFT OUTER JOIN BILL_TRN_/*$domainId*/ B
			ON C.CUSTOMER_CODE = B.CUSTOMER_CODE
WHERE
	C.SALES_CM_CATEGORY <> /*salesCmCategory*/'0'
	/*IF billCrtCategory != null */
	and B.BILL_CRT_CATEGORY = /*billCrtCategory*/'2'
	/*END*/
