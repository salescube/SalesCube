UPDATE RATE_MST_/*$domainId*/
SET
	NAME=/*name*/
	,SIGN=/*sign*/
	,REMARKS=/*remarks*/
	,UPD_FUNC=/*updFunc*/
	,UPD_DATETM=now()
	,UPD_USER=/*updUser*/
WHERE
	RATE_ID=/*rateId*/
