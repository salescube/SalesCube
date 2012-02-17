UPDATE CATEGORY_MST_/*$domainId*/
SET
UPD_FUNC=/*updFunc*/
,UPD_DATETM=now()
,UPD_USER=/*updUser*/
WHERE
CATEGORY_ID=/*categoryId*/
