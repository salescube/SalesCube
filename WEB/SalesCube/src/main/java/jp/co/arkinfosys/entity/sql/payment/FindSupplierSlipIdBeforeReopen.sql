SELECT
    SUPPLIER_SLIP_ID
FROM
    SUPPLIER_SLIP_TRN_/*$domainId*/
WHERE
    PAYMENT_CUTOFF_DATE =/*supplierCutoffDate*/
