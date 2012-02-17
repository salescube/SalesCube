UPDATE PRODUCT_SET_MST_/*$domainId*/ SET
	QUANTITY = /*quantity*/
	,UPD_FUNC = /*updFunc*/
	,UPD_DATETM = now()
	,UPD_USER = /*updUser*/
WHERE
	SET_PRODUCT_CODE = /*setProductCode*/
	AND PRODUCT_CODE = /*productCode*/