SELECT
	RATE_ID,
	START_DATE,
	RATE,
	REMARKS,
	UPD_DATETM
FROM
	RATE_TRN_/*$domainId*/
/*BEGIN*/
WHERE
	/*IF rateId != null*/
	RATE_ID=/*rateId*/'0'
	/*END*/
	/*IF startDate != null*/
	AND START_DATE <= CAST(/*startDate*/'2000/01/01' AS DATE)
	/*END*/
/*END*/

ORDER BY
	START_DATE DESC
