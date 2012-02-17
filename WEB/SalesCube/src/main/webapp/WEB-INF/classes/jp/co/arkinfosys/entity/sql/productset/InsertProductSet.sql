insert into PRODUCT_SET_MST_/*$domainId*/ (
		SET_PRODUCT_CODE,
		PRODUCT_CODE,
		QUANTITY,
		CRE_FUNC,
		CRE_DATETM,
		CRE_USER,
		UPD_FUNC,
		UPD_DATETM,
		UPD_USER
	)
	values (
		/*setProductCode*/
		,/*productCode*/
		,/*quantity*/
		,/*creFunc*/NULL
		,now()
		,/*creUser*/NULL
		,/*updFunc*/NULL
		,now()
		,/*updUser*/NULL
	)