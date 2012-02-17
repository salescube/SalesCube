INSERT INTO DISCOUNT_REL_/*$domainId*/ (
		PRODUCT_CODE
		,DISCOUNT_ID
		,CRE_FUNC
		,CRE_DATETM
		,CRE_USER
		,UPD_FUNC
		,UPD_DATETM
		,UPD_USER
	)
	VALUES (
		/*productCode*/'S',
		/*discountId*/'S',
		/*creFunc*/'S',
		/*IF creDatetm != null*/
			/*creDatetm*/
		--ELSE
			now()
		/*END*/,
		/*creUser*/'S',
		/*updFunc*/'S',
		/*IF updDatetm != null*/
			/*updDatetm*/
		--ELSE
			now()
		/*END*/,
		/*updUser*/'S'
	)