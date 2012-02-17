UPDATE
    PAYMENT_SLIP_TRN_/*$domainId*/
SET
	STATUS = /*status*/,
	PAYMENT_DATE = /*paymentDate*/,
	SUPPLIER_CODE = /*supplierCode*/,
	SUPPLIER_NAME = /*supplierName*/,
	RATE_ID = /*rateId*/,
	TAX_SHIFT_CATEGORY = /*taxShiftCategory*/,
	TAX_FRACT_CATEGORY = /*taxFractCategory*/,
	PRICE_FRACT_CATEGORY = /*priceFractCategory*/,
	PRICE_TOTAL = /*priceTotal*/,
	FE_PRICE_TOTAL = /*fePriceTotal*/,
	REMARKS = /*remarks*/,
	PAYMENT_ANNUAL = /*paymentAnnual*/,
	PAYMENT_MONTHLY = /*paymentMonthly*/,
	PAYMENT_YM = /*paymentYm*/,
	APT_BALANCE_ID = /*aptBalanceId*/,
	UPD_FUNC = /*updFunc*/,
	UPD_DATETM = now(),
	UPD_USER = /*updUser*/
WHERE
	PAYMENT_SLIP_ID = /*paymentSlipId*/

