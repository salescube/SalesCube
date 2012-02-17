UPDATE
    PAYMENT_LINE_TRN_/*$domainId*/
SET
	PAYMENT_SLIP_ID = /*paymentSlipId*/,
	LINE_NO = /*lineNo*/,
	PAYMENT_CATEGORY = /*paymentCategory*/,
	PRODUCT_CODE = /*productCode*/,
	PRODUCT_ABSTRACT=/*productAbstract*/,
	QUANTITY = /*quantity*/,
	UNIT_PRICE = /*unitPrice*/,
	PRICE = /*price*/,
	CTAX_RATE = /*ctaxRate*/,
	DOL_UNIT_PRICE = /*dolUnitPrice*/,
	DOL_PRICE = /*dolPrice*/,
	RATE = /*rate*/,
	REMARKS = /*remarks*/,
	SUPPLIER_DATE = /*supplierDate*/,
	UPD_FUNC = /*updFunc*/,
	UPD_DATETM = now(),
	UPD_USER = /*updUser*/
WHERE
	PAYMENT_LINE_ID = /*paymentLineId*/


