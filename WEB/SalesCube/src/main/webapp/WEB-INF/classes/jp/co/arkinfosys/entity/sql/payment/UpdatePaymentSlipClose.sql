UPDATE
    PAYMENT_SLIP_TRN_/*$domainId*/
SET
	STATUS = /*status*/,
	PAYMENT_CUTOFF_DATE = /*paymentCutoffDate*/,
	PAYMENT_PDATE = curdate(),
	UPD_FUNC = /*updFunc*/,
	UPD_DATETM = now(),
	UPD_USER = /*updUser*/
WHERE
	SUPPLIER_CODE = /*supplierCode*/ AND
	PAYMENT_CUTOFF_DATE IS NULL AND
	PAYMENT_DATE <= /*paymentCutoffDate*/
