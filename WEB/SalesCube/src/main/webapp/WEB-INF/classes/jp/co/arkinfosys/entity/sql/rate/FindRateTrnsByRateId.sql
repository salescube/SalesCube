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
	AND START_DATE <= /*startDate*/'0'
	/*END*/
/*END*/

ORDER BY
	START_DATE DESC
