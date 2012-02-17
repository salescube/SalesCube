SELECT
	UPD_USER,
    UPD_DATETM
FROM
    SUPPLIER_SLIP_TRN_/*$domainId*/
WHERE
    SUPPLIER_SLIP_ID =/*supplierSlipId*/'default'
FOR UPDATE