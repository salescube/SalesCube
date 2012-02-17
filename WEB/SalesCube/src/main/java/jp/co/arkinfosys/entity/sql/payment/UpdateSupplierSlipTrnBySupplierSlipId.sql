UPDATE
    SUPPLIER_SLIP_TRN_/*$domainId*/
SET
	STATUS=/*status*/,
	PAYMENT_SLIP_ID=/*paymentSlipId*/,
	SUPPLIER_PAYMENT_DATE = /*paymentDate*/,
	UPD_FUNC = /*updFunc*/,
	UPD_DATETM = now(),
	UPD_USER = /*updUser*/
WHERE
    SUPPLIER_SLIP_ID =/*supplierSlipId*/
