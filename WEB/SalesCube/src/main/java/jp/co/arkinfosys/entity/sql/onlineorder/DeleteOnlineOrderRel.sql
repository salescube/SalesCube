DELETE FROM ONLINE_ORDER_REL_/*$domainId*/
/*BEGIN*/
WHERE
	/*IF roSlipId != null */
	AND RO_SLIP_ID=/*roSlipId*/'S'
	/*END*/
	/*IF roLineId != null */
	AND RO_LINE_ID=/*roLineId*/'S'
	/*END*/
/*END*/
