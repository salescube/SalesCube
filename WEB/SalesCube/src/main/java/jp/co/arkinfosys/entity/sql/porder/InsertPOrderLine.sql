INSERT
	INTO
		PO_LINE_TRN_/*$domainId*/ (
PO_LINE_ID
,STATUS
,PO_SLIP_ID
,LINE_NO
,PRODUCT_CODE
,SUPPLIER_PCODE
,PRODUCT_ABSTRACT
,QUANTITY
,TEMP_UNIT_PRICE_CATEGORY
,TAX_CATEGORY
,SUPPLIER_CM_CATEGORY
,UNIT_PRICE
,PRICE
,CTAX_PRICE
,CTAX_RATE
,DOL_UNIT_PRICE
,DOL_PRICE
,RATE
,DELIVERY_DATE
,REMARKS
,PRODUCT_REMARKS
,REST_QUANTITY
,CRE_FUNC
,CRE_DATETM
,CRE_USER
,UPD_FUNC
,UPD_DATETM
,UPD_USER
)
SELECT
/*poLineId*/
,0
,/*poSlipId*/
,/*lineNo*/
,/*productCode*/
,/*supplierPcode*/
,/*productAbstract*/
,/*quantity*/
,NULL
,PRODMST.TAX_CATEGORY
,SLIP.SUPPLIER_CM_CATEGORY
,/*unitPrice*/
,/*price*/
,/*ctaxPrice*/
,/*ctaxRate*/
,/*dolUnitPrice*/
,/*dolPrice*/
,/*rate*/
,/*deliveryDate*/
,/*remarks*/
,/*productRemarks*/
,/*restQuantity*/
,/*creFunc*/NULL
,now()
,/*creUser*/NULL
,/*updFunc*/NULL
,now()
,/*updUser*/NULL
FROM PRODUCT_MST_/*$domainId*/ PRODMST
	INNER JOIN PO_SLIP_TRN_/*$domainId*/ SLIP
		ON SLIP.PO_SLIP_ID = /*poSlipId*/''
	WHERE PRODMST.PRODUCT_CODE = /*productCode*/''

