SELECT
	PRODUCT_CODE,
	DISCOUNT_ID,
	CRE_FUNC,
	CRE_DATETM,
	CRE_USER,
	UPD_FUNC,
	UPD_DATETM,
	UPD_USER
FROM
	DISCOUNT_REL_/*$domainId*/
WHERE
	PRODUCT_CODE=/*productCode*/'S'
	/*IF discountId != null */
	AND DISCOUNT_ID=/*discountId*/'S'
	/*END*/