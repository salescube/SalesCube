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
    T1.TAX_TYPE_CATEGORY = /*taxTypeCategory*/'default' AND
    T1.START_DATE = (SELECT
                     	MAX(START_DATE)
                     FROM
                     	TAX_RATE_MST_/*$domainId*/ T2
                     WHERE
                     	CAST(/*startDate*/'2000/01/01' AS DATE) >= T2.START_DATE AND
						T2.TAX_TYPE_CATEGORY = /*taxTypeCategory*/'default'
                     )
