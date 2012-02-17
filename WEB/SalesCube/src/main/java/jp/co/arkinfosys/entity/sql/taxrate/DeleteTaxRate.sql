DELETE FROM TAX_RATE_MST_/*$domainId*/
WHERE
	TAX_TYPE_CATEGORY=/*taxTypeCategory*/
	AND
	START_DATE=/*startDate*/
