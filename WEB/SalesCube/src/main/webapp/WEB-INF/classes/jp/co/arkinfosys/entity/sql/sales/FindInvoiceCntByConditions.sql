SELECT
	COUNT(SALES_SLIP_ID)
FROM
	SALES_SLIP_TRN_/*$domainId*/ SLIP
/*BEGIN*/
WHERE
	/*IF salesDate != null */
	AND SLIP.SALES_DATE = /*salesDate*/
	/*END*/
	/*IF salesCmCategory != null */
	AND SLIP.SALES_CM_CATEGORY = /*salesCmCategory*/
	/*END*/
	/*IF dcCategory != null */
	AND SLIP.DC_CATEGORY = /*dcCategory*/
	/*END*/
	/*IF excludingOutput != null */
	AND SLIP.SI_PRINT_COUNT = 0
	/*END*/
/*END*/
