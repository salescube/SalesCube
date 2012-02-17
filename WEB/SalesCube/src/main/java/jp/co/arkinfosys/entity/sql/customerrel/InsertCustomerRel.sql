INSERT
INTO
CUSTOMER_REL_/*$domainId*/ (
CUSTOMER_CODE
,REL_CODE
,CUST_REL_CATEGORY
,CRE_FUNC
,CRE_DATETM
,CRE_USER
,UPD_FUNC
,UPD_DATETM
,UPD_USER
) VALUES (
/*customerCode*/NULL
,/*relCode*/NULL
,/*custRelCategory*/NULL
,/*creFunc*/NULL
,now()
,/*creUser*/NULL
,/*updFunc*/NULL
,now()
,/*updUser*/NULL
)
