UPDATE EAD_SLIP_TRN_/*$domainId*/
SET STOCK_PDATE = CAST(/*stockPdate*/ AS DATE)
WHERE EAD_SLIP_ID = /*eadSlipId*/
