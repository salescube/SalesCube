INSERT
INTO
DELIVERY_DEPOSIT_REL_/*$domainId*/ (
	SALES_SLIP_ID
	,DEPOSIT_SLIP_ID
	,DELIVERY_SLIP_ID
	,DATA_CATEGORY
   	,CRE_FUNC
    ,CRE_DATETM
    ,CRE_USER
    ,UPD_FUNC
    ,UPD_DATETM
    ,UPD_USER
) VALUES (
	/*salesSlipId*/NULL
	,/*depositSlipId*/NULL
	,/*deliverySlipId*/NULL
	,/*dataCategory*/NULL
	,/*creFunc*/NULL
	,now()
	,/*creUser*/NULL
	,/*updFunc*/NULL
	,now()
	,/*updUser*/NULL
)