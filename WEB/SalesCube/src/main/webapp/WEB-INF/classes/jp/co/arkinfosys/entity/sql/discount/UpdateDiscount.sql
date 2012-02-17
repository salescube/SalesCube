UPDATE DISCOUNT_MST_/*$domainId*/
SET
	DISCOUNT_NAME = /*discountName*/,
	REMARKS = /*remarks*/,
	USE_FLAG = /*useFlag*/,
	UPD_FUNC = /*updFunc*/,
	UPD_DATETM = now(),
	UPD_USER = /*updUser*/
WHERE
	DISCOUNT_ID=/*discountId*/
