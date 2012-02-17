SELECT
        MAX(A.ART_CUTOFF_DATE) LAST_CUTOFF_DATE
    FROM
        ART_BALANCE_TRN_/*$domainId*/ A
	WHERE
        A.SALES_CM_CATEGORY <> /*salesCmCategory*/'0'
