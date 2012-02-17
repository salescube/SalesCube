SELECT
  RATE_ID, RATE
FROM
  RATE_TRN_/*$domainId*/
/*BEGIN*/
WHERE
	/*IF rateId != null*/
	RATE_ID=/*rateId*/'0'
	/*END*/
  AND START_DATE <= DATE_FORMAT(now(), '%Y-%m-%d')
/*END*/
ORDER BY START_DATE DESC LIMIT 1