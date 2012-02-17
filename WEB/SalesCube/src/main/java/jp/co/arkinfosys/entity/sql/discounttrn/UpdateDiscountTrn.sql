UPDATE DISCOUNT_TRN_/*$domainId*/
SET
	DISCOUNT_ID = /*discountId*/,
	LINE_NO = /*lineNo*/,
	DATA_FROM = /*dataFrom*/,
	DATA_TO = /*dataTo*/,
	DISCOUNT_RATE = /*discountRate*/,
	UPD_FUNC = /*updFunc*/,
	UPD_DATETM = now(),
	UPD_USER = /*updUser*/
WHERE
	DISCOUNT_DATA_ID = /*discountDataId*/
