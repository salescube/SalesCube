UPDATE
    SUPPLIER_LINE_TRN_/*$domainId*/
SET
	STATUS=/*status*/,
	PAYMENT_LINE_ID=/*paymentLineId*/,
	UPD_FUNC = /*updFunc*/,
	UPD_DATETM = now(),
	UPD_USER = /*updUser*/
WHERE
    SUPPLIER_LINE_ID =/*supplierLineId*/
