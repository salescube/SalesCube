UPDATE
    PO_LINE_TRN_/*$domainId*/
SET
STATUS = /*status*/0,
LINE_NO = /*lineNo*/,
PRODUCT_CODE = /*productCode*/,
SUPPLIER_PCODE = /*supplierPcode*/,
PRODUCT_ABSTRACT = /*productAbstract*/,
QUANTITY = /*quantity*/,
TEMP_UNIT_PRICE_CATEGORY = /*tempUnitPriceCategory*/,
TAX_CATEGORY = (
SELECT TAX_CATEGORY
	FROM PRODUCT_MST_/*$domainId*/
	WHERE PRODUCT_CODE = /*productCode*/''
),
SUPPLIER_CM_CATEGORY = (
SELECT SUPPLIER_CM_CATEGORY
	FROM PO_SLIP_TRN_/*$domainId*/
	WHERE PO_SLIP_ID = /*poSlipId*/''
),
UNIT_PRICE = /*unitPrice*/,
PRICE = /*price*/,
CTAX_PRICE = /*ctaxPrice*/,
CTAX_RATE = /*ctaxRate*/,
DOL_UNIT_PRICE = /*dolUnitPrice*/,
DOL_PRICE = /*dolPrice*/,
RATE = /*rate*/,
DELIVERY_DATE = /*deliveryDate*/,
REMARKS = /*remarks*/,
PRODUCT_REMARKS = /*productRemarks*/,
REST_QUANTITY = /*restQuantity*/,

UPD_FUNC = /*updFunc*/,
UPD_DATETM = now(),
UPD_USER = /*updUser*/
WHERE
	PO_LINE_ID = /*poLineId*/
	AND PO_SLIP_ID = /*poSlipId*/
