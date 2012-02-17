UPDATE
	TAX_RATE_MST_/*$domainId*/ SET
		TAX_RATE=/*taxRate*/NULL
		,UPD_FUNC=/*updFunc*/NULL
		,UPD_DATETM=now()
		,UPD_USER=/*updUser*/NULL
WHERE
	TAX_TYPE_CATEGORY=/*taxTypeCategory*/0
	AND
	START_DATE=/*startDate*/NULL
