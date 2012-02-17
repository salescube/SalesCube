SELECT
    PAYMENT_SLIP_ID
FROM
    PAYMENT_SLIP_TRN_/*$domainId*/
WHERE
    PAYMENT_CUTOFF_DATE =/*paymentCutoffDate*/
