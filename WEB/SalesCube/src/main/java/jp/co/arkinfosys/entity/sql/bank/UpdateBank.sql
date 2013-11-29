UPDATE BANK_MST_/*$domainId*/
SET
	BANK_CODE=/*bankCode*/,
	BANK_NAME=/*bankName*/,
	STORE_CODE=/*storeCode*/,
	STORE_NAME=/*storeName*/,
	DWB_TYPE=/*dwbType*/,
	ACCOUNT_NUM=/*accountNum*/,
	REMARKS=/*remarks*/,
	VALID=/*valid*/,
	UPD_FUNC=/*updFunc*/,
	UPD_DATETM=now(),
	UPD_USER=/*updUser*/
WHERE
	BANK_ID=/*bankId*/
