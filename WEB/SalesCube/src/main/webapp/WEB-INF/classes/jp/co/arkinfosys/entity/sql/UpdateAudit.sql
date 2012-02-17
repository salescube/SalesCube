UPDATE /*$tableName*/
SET
	UPD_USER=/*updUser*/'NOLOG'
	,DEL_FUNC=/*delFunc*/NULL
	,DEL_DATETM=now()
	,DEL_USER=/*delUser*/NULL
/*BEGIN*/
WHERE
	/*IF columnName1 != null && keyValue1 != null*/
	/*$columnName1*/ = /*keyValue1*/
	/*END*/

	/*IF columnName2 != null && keyValue2 != null*/
	AND /*$columnName2*/ = /*keyValue2*/
	/*END*/

	/*IF columnName3 != null && keyValue3 != null*/
	AND /*$columnName3*/ = /*keyValue3*/
	/*END*/

	/*IF columnName4 != null && keyValue4 != null*/
	AND /*$columnName4*/ = /*keyValue4*/
	/*END*/

	/*IF columnName5 != null && keyValue5 != null*/
	AND /*$columnName5*/ = /*keyValue5*/
	/*END*/
/*END*/