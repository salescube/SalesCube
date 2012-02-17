UPDATE ESTIMATE_LINE_TRN_/*$domainId*/'DEFAULT' SET
		ESTIMATE_SHEET_ID=/*estimateSheetId*/NULL
		,LINE_NO=/*lineNo*/NULL
		,PRODUCT_CODE=/*productCode*/NULL
		,CUSTOMER_PCODE=/*customerPcode*/NULL
		,PRODUCT_ABSTRACT=/*productAbstract*/NULL
		,QUANTITY=/*quantity*/NULL
		,UNIT_COST=/*unitCost*/NULL
		,UNIT_RETAIL_PRICE=/*unitRetailPrice*/NULL
		,COST=/*cost*/NULL
		,RETAIL_PRICE=/*retailPrice*/NULL
		,REMARKS=/*remarks*/NULL
        ,UPD_FUNC=/*updFunc*/NULL
        ,UPD_DATETM=now()
        ,UPD_USER=/*updUser*/NULL
	WHERE
		ESTIMATE_LINE_ID=/*estimateLineId*/'default'
