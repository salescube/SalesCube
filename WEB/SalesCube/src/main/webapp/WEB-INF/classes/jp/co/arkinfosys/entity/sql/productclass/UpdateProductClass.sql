UPDATE PRODUCT_CLASS_MST_/*$domainId*/
SET
	CLASS_NAME = /*className*/,
	UPD_FUNC=/*updFunc*/,
	UPD_DATETM=/*updDatetm*/,
	UPD_USER=/*updUser*/
WHERE
	CLASS_CODE_1 = /*classCode1*/ AND
	CLASS_CODE_2 = /*classCode2*/ AND
	CLASS_CODE_3 = /*classCode3*/

