SELECT
	UPD_USER,
    UPD_DATETM
FROM
    PAYMENT_SLIP_TRN_/*$domainId*/
WHERE
    PAYMENT_SLIP_ID =/*paymentSlipId*/'default'
FOR UPDATE