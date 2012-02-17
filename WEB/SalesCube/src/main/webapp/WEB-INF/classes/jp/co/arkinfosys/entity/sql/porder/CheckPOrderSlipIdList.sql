SELECT
	PO_SLIP_ID
	FROM PO_SLIP_TRN_/*$domainId*/
	WHERE PO_SLIP_ID IN /*checkPoSlipIdList*/('1','2')