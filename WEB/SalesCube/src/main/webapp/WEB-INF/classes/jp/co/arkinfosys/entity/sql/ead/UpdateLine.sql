UPDATE EAD_LINE_TRN_/*$domainId*/ SET
	EAD_LINE_ID=/*eadLineId*/,
	EAD_SLIP_ID=/*eadSlipId*/,
	LINE_NO=/*lineNo*/,
	PRODUCT_CODE=/*productCode*/,
	PRODUCT_ABSTRACT=/*productAbstract*/,
	RACK_CODE=/*rackCode*/,
	RACK_NAME=/*rackName*/,
	QUANTITY=/*quantity*/,
	REMARKS=/*remarks*/,
	SALES_LINE_ID=/*salesLineId*/,
	SUPPLIER_LINE_ID=/*supplierLineId*/,
	UPD_FUNC=/*updFunc*/NULL,
	UPD_DATETM=now(),
	UPD_USER=/*updUser*/NULL
WHERE
	EAD_LINE_ID=/*eadLineId*/
