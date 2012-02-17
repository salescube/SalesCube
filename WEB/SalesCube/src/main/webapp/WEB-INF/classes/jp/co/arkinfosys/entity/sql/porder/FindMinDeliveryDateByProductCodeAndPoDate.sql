SELECT
	MIN(LINE.DELIVERY_DATE) AS DELIVERY_DATE
FROM
	PO_SLIP_TRN_/*$domainId*/ SLIP
	INNER JOIN PO_LINE_TRN_/*$domainId*/ LINE ON SLIP.PO_SLIP_ID = LINE.PO_SLIP_ID
/*BEGIN*/
WHERE
	/*IF productCode != null */
	AND LINE.PRODUCT_CODE = /*productCode*/''
	/*END*/
	/*IF poDate != null */
	AND SLIP.PO_DATE >= CAST(/*poDate*/ AS DATE)
	AND SLIP.PO_DATE <= CURDATE()
	/*END*/
	/*IF restQuantity != null */
	AND REST_QUANTITY > /*restQuantity*/0
	/*END*/
/*END*/
