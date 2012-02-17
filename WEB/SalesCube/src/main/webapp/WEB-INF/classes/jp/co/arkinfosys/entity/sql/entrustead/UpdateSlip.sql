UPDATE ENTRUST_EAD_SLIP_TRN_/*$domainId*/
SET
	ENTRUST_EAD_DATE = /*entrustEadDate*/,
	ENTRUST_EAD_ANNUAL = /*entrustEadAnnual*/,
	ENTRUST_EAD_MONTHLY = /*entrustEadMonthly*/,
	ENTRUST_EAD_YM = /*entrustEadYm*/,
	REMARKS = /*remarks*/,
	DISPATCH_ORDER_PRINT_COUNT = /*dispatchOrderPrintCount*/,
	UPD_FUNC = /*updFunc*/,
	UPD_DATETM = now(),
	UPD_USER = /*updUser*/
WHERE
	ENTRUST_EAD_SLIP_ID = /*entrustEadSlipId*/
