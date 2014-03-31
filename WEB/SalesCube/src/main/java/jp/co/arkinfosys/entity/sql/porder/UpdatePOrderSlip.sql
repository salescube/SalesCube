UPDATE
    PO_SLIP_TRN_/*$domainId*/
SET
STATUS = /*status*/,
PO_DATE = /*poDate*/,
PO_ANNUAL = /*poAnnual*/,
PO_MONTHLY = /*poMonthly*/,
PO_YM = /*poYm*/,
DELIVERY_DATE = /*deliveryDate*/,

REMARKS = /*remarks*/,
SUPPLIER_CODE = /*supplierCode*/,
SUPPLIER_NAME = /*supplierName*/,
SUPPLIER_KANA = /*supplierKana*/,
SUPPLIER_ZIP_CODE = /*supplierZipCode*/,
SUPPLIER_ADDRESS_1 = /*supplierAddress1*/,
SUPPLIER_ADDRESS_2 = /*supplierAddress2*/,
SUPPLIER_PC_NAME = /*supplierPcName*/,
SUPPLIER_PC_KANA = /*supplierPcKana*/,
SUPPLIER_PC_PRE_CATEGORY = /*supplierPcPreCategory*/,
SUPPLIER_PC_POST = /*supplierPcPost*/,
SUPPLIER_TEL = /*supplierTel*/,
SUPPLIER_FAX = /*supplierFax*/,
SUPPLIER_EMAIL = /*supplierEmail*/,
SUPPLIER_URL = (
SELECT SUPPLIER_URL
	FROM SUPPLIER_MST_/*$domainId*/
	WHERE SUPPLIER_CODE = /*supplierCode*/''
),
TRANSPORT_CATEGORY = /*transportCategory*/,
TAX_SHIFT_CATEGORY = (
SELECT TAX_SHIFT_CATEGORY
	FROM SUPPLIER_MST_/*$domainId*/
	WHERE SUPPLIER_CODE = /*supplierCode*/''
),
SUPPLIER_CM_CATEGORY = (
SELECT SUPPLIER_CM_CATEGORY
	FROM SUPPLIER_MST_/*$domainId*/
	WHERE SUPPLIER_CODE = /*supplierCode*/''
),
RATE_ID = /*rateId*/,

PRICE_TOTAL = /*priceTotal*/,
CTAX_TOTAL = /*ctaxTotal*/,
CTAX_RATE = /*ctaxRate*/,
FE_PRICE_TOTAL = /*fePriceTotal*/,
PRINT_COUNT = /*printCount*/,

UPD_FUNC = /*updFunc*/,
UPD_DATETM = now(),
UPD_USER = /*updUser*/

,SUPPLIER_ABBR = /*supplierAbbr*/
,SUPPLIER_DEPT_NAME = /*supplierDeptName*/
,SUPPLIER_PC_PRE = /*supplierPcPre*/

,TAX_FRACT_CATEGORY = /*taxFractCategory*/
,PRICE_FRACT_CATEGORY = /*priceFractCategory*/

WHERE
	PO_SLIP_ID = /*poSlipId*/
