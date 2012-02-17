UPDATE CATEGORY_TRN_/*$domainId*/
SET
UPD_FUNC=/*updFunc*/
,UPD_DATETM=now()
,UPD_USER=/*updUser*/
WHERE
CATEGORY_ID=/*categoryId*/
AND
CATEGORY_CODE=/*categoryCode*/
