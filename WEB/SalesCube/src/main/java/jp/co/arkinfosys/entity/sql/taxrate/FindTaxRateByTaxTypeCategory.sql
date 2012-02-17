SELECT
    TAX_TYPE_CATEGORY,
    START_DATE,
    TAX_RATE,
    CRE_FUNC,
    CRE_DATETM,
    CRE_USER,
    UPD_FUNC,
    UPD_DATETM,
    UPD_USER
FROM
    TAX_RATE_MST_/*$domainId*/ T1
WHERE
    T1.TAX_TYPE_CATEGORY = /*taxTypeCategory*/
ORDER BY
	/*$sortColumn*/
