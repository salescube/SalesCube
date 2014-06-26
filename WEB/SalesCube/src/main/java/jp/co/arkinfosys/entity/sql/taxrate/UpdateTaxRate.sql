UPDATE
	TAX_RATE_MST_/*$domainId*/ SET
		TAX_RATE=/*taxRate*/NULL
		,UPD_FUNC=/*updFunc*/NULL
		,UPD_DATETM=now()
		,UPD_USER=/*updUser*/NULL
WHERE
	TAX_TYPE_CATEGORY=/*taxTypeCategory*/0
	AND
	START_DATE=CAST(/*startDate*/'2000/01/01' AS DATE)
