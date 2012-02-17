UPDATE
    PAYMENT_SLIP_TRN_/*$domainId*/
SET
	STATUS=/*status*/NULL,
	PAYMENT_CUTOFF_DATE=NULL,
	PAYMENT_PDATE=NULL,
	UPD_FUNC = /*updFunc*/NULL,
	UPD_DATETM = now(),
	UPD_USER = /*updUser*/NULL
WHERE
    PAYMENT_CUTOFF_DATE =/*paymentCutoffDate*/
