DELETE FROM CATEGORY_TRN_/*$domainId*/
/*BEGIN*/
WHERE
	/*IF categoryId != null*/
	CATEGORY_ID=/*categoryId*/
	/*END*/

	/*IF categoryCode != null*/
	AND CATEGORY_CODE=/*categoryCode*/
	/*END*/
/*END*/
