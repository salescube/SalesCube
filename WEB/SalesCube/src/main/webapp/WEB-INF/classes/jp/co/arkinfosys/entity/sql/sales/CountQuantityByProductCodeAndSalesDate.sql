SELECT
	SUM(LINE.QUANTITY) AS QUANTITY
FROM
	SALES_SLIP_TRN_/*$domainId*/ SLIP
	INNER JOIN SALES_LINE_TRN_/*$domainId*/ LINE ON SLIP.SALES_SLIP_ID = LINE.SALES_SLIP_ID
/*BEGIN*/
WHERE
	/*IF productCode != null */
	AND LINE.PRODUCT_CODE = /*productCode*/''
	/*END*/
	/*IF salesDate != null */
	AND SLIP.SALES_DATE >= CAST(/*salesDate*/ AS DATE)
	AND SLIP.SALES_DATE <= CURDATE()
	/*END*/
/*END*/
