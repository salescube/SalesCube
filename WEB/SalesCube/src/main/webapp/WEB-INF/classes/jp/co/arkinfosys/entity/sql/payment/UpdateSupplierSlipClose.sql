UPDATE
    SUPPLIER_SLIP_TRN_/*$domainId*/
SET
	PAYMENT_CUTOFF_DATE = /*supplierCutoffDate*/,
	PAYMENT_PDATE = curdate(),
	UPD_FUNC = /*updFunc*/,
	UPD_DATETM = now(),
	UPD_USER = /*updUser*/
WHERE
	SUPPLIER_CODE = /*supplierCode*/ AND
	PAYMENT_CUTOFF_DATE IS NULL AND
	SUPPLIER_DATE <= /*supplierCutoffDate*/ AND
	PO_SLIP_ID IS NOT NULL