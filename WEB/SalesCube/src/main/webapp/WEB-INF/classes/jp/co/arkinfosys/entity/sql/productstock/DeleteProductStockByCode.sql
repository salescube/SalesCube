DELETE FROM PRODUCT_STOCK_TRN_/*$domainId*/
WHERE
	RACK_CODE = /*rackCode*/
	AND PRODUCT_CODE = /*productCode*/
	AND STOCK_ANNUAL = /*stockAnnual*/
	AND STOCK_MONTHLY = /*stockMonthly*/
