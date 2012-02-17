DELETE FROM DISCOUNT_REL_/*$domainId*/
WHERE
	PRODUCT_CODE=/*productCode*/'S'
	/*IF discountId != null */
	AND DISCOUNT_ID=/*discountId*/'S'
	/*END*/