UPDATE
	PO_LINE_TRN_/*$domainId*/'DEFAULT' SET
		STATUS=/*status*/NULL
		,REST_QUANTITY=/*restQuantity*/NULL
		,UPD_FUNC=/*updFunc*/NULL
		,UPD_DATETM=now()
		,UPD_USER=/*updUser*/NULL
	WHERE
		PO_LINE_ID=/*poLineId*/0
