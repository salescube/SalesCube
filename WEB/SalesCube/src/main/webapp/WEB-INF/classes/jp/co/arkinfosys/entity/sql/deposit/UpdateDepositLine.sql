UPDATE
        DEPOSIT_LINE_TRN_/*$domainId*/ SET
			STATUS=/*status*/NULL
			,DEPOSIT_SLIP_ID=/*depositSlipId*/NULL
			,LINE_NO=/*lineNo*/NULL
			,DEPOSIT_CATEGORY=/*depositCategory*/NULL
			,PRICE=/*price*/NULL
			,INST_DATE=/*instDate*/NULL
			,INST_NO=/*instNo*/NULL
			,BANK_ID=/*bankId*/NULL
			,BANK_INFO=/*bankInfo*/NULL
			,REMARKS=/*remarks*/NULL
			,SALES_LINE_ID=/*salesLineId*/NULL
			,UPD_FUNC=/*creFunc*/NULL
			,UPD_DATETM=now()
			,UPD_USER=/*creUser*/NULL
	WHERE
		DEPOSIT_LINE_ID=/*depositLineId*/0
