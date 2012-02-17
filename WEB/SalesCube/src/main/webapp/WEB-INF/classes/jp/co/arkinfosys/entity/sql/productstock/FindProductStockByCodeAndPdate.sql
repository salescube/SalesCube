SELECT
	RACK_CODE,
	PRODUCT_CODE,
	STOCK_PDATE,
	STOCK_ANNUAL,
	STOCK_MONTHLY,
	STOCK_YM,
	STOCK_NUM,
	ENTER_NUM,
	DISPATCH_NUM,
	REMARKS,
	CRE_FUNC,
	CRE_DATETM,
	CRE_USER,
	UPD_FUNC,
	UPD_DATETM,
	UPD_USER
FROM
	PRODUCT_STOCK_TRN_/*$domainId*/
/*BEGIN*/
WHERE
	/*IF rackCode != null */
	AND RACK_CODE= /*rackCode*/
	/*END*/
	/*IF productCode != null */
	AND PRODUCT_CODE= /*productCode*/
	/*END*/
	/*IF stockPdate != null */
	AND STOCK_PDATE= CAST(/*stockPdate*/ AS DATE)
	/*END*/
/*END*/
