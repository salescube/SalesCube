UPDATE RACK_MST_/*$domainId*/'DEFAULT' SET
WAREHOUSE_CODE=/*warehouseCode*/NULL
,RACK_NAME=/*rackName*/NULL
,RACK_CATEGORY='1'				-- 棚分類という考え方が無くなったので、「自社棚」固定とする
,MULTI_FLAG=/*multiFlag*/'0'
,ZIP_CODE=/*zipCode*/NULL
,ADDRESS_1=/*address1*/NULL
,ADDRESS_2=/*address2*/NULL
,RACK_PC_NAME=/*rackPcName*/NULL
,RACK_TEL=/*rackTel*/NULL
,RACK_FAX=/*rackFax*/NULL
,RACK_EMAIL=/*rackEmail*/NULL
,UPD_FUNC=/*updFunc*/NULL
,UPD_DATETM=now()
,UPD_USER=/*updUser*/NULL
WHERE
RACK_CODE=/*rackCode*/NULL
