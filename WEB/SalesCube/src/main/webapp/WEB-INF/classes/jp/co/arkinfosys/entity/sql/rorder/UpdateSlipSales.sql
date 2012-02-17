UPDATE
    RO_SLIP_TRN_/*$domainId*/
SET
	STATUS=/*status*/,
	UPD_FUNC=/*updFunc*/,
	UPD_DATETM=now(),
	UPD_USER=/*updUser*/
WHERE
	RO_SLIP_ID = /*roSlipId*/
