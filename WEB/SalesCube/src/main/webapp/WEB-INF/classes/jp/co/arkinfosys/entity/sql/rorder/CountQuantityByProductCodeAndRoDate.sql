SELECT
	SUM(LINE.QUANTITY) AS QUANTITY
FROM
	RO_SLIP_TRN_/*$domainId*/ SLIP
	INNER JOIN RO_LINE_TRN_/*$domainId*/ LINE ON SLIP.RO_SLIP_ID = LINE.RO_SLIP_ID
/*BEGIN*/
WHERE
	/*IF productCode != null */
	AND LINE.PRODUCT_CODE = /*productCode*/''
	/*END*/
	/*IF roDate != null */
	AND SLIP.RO_DATE >= CAST(/*roDate*/ AS DATE)
	AND SLIP.RO_DATE <= CURDATE()
	/*END*/
/*END*/
