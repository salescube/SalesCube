UPDATE RATE_TRN_/*$domainId*/
SET
	RATE=/*rate*/
	,REMARKS=/*remarks*/
	,UPD_FUNC=/*updFunc*/
	,UPD_DATETM=now()
	,UPD_USER=/*updUser*/
WHERE
	RATE_ID=/*rateId*/
	AND
	START_DATE=/*startDate*/
