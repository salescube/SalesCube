SELECT COUNT(DISTINCT PO_SLIP_ID)
FROM
	PO_SLIP_TRN_/*$domainId*/
/*BEGIN*/
WHERE
	/*IF poSlipIdFrom != null */
	    PO_SLIP_ID >= /*poSlipIdFrom*/
	/*END*/
	/*IF poSlipIdTo != null */
	AND PO_SLIP_ID <= /*poSlipIdTo*/
	/*END*/
	/*IF poDateFrom != null */
	AND PO_DATE >= CAST(/*poDateFrom*/ AS DATE)
	/*END*/
	/*IF poDateTo != null */
	AND PO_DATE <= CAST(/*poDateTo*/ AS DATE)
	/*END*/
	/*IF supplierCode != null */
	AND SUPPLIER_CODE LIKE /*supplierCode*/
	/*END*/
	/*IF supplierName != null */
	AND SUPPLIER_NAME LIKE /*supplierName*/
	/*END*/
	/*IF userName != null */
	AND USER_NAME LIKE /*userName*/
	/*END*/
	/*IF exceptAlreadyOutput != null */
	AND PRINT_COUNT = 0
	/*END*/
/*END*/
