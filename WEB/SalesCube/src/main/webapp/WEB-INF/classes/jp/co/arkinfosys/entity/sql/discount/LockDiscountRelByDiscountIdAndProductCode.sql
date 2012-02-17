SELECT
	PRODUCT_CODE
	,DISCOUNT_ID
	,UPD_DATETM
	,UPD_USER
FROM
	DISCOUNT_REL_/*$domainId*/
WHERE
	PRODUCT_CODE=/*productCode*/
	/*IF discountId != null*/
	AND DISCOUNT_ID=/*discountId*/
	/*END*/
FOR UPDATE
