INSERT
INTO
BANK_DEPOSIT_REL_/*$domainId*/ (
	DEPOSIT_SLIP_ID
	,LINE_NO
	,PAYMENT_DATE
	,PAYMENT_NAME
   	,CRE_FUNC
    ,CRE_DATETM
    ,CRE_USER
    ,UPD_FUNC
    ,UPD_DATETM
    ,UPD_USER
) VALUES (
	/*depositSlipId*/NULL
	,/*lineNo*/
	,/*paymentDate*/NULL
	,/*paymentName*/NULL
	,/*creFunc*/NULL
	,now()
	,/*creUser*/NULL
	,/*updFunc*/NULL
	,now()
	,/*updUser*/NULL
)