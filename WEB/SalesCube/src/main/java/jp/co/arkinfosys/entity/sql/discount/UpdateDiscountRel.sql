UPDATE DISCOUNT_REL_/*$domainId*/ SET
	DISCOUNT_ID = /*discountId*/
	,UPD_FUNC = /*updFunc*/
	,UPD_DATETM = now()
	,UPD_USER = /*updUser*/
WHERE
	PRODUCT_CODE = /*productCode*/'S'

