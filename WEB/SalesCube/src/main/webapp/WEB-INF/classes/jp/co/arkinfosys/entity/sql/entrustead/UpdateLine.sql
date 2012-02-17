UPDATE ENTRUST_EAD_LINE_TRN_/*$domainId*/
SET
	REMARKS = /*remarks*/,
	UPD_FUNC = /*updFunc*/,
	UPD_DATETM = now(),
	UPD_USER = /*updUser*/
WHERE
	ENTRUST_EAD_LINE_ID = /*entrustEadLineId*/
