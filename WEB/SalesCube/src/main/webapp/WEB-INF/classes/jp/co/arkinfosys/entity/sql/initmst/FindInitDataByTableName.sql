SELECT
	COLUMN_NAME,
	TITLE,
	CATEGORY_ID,
	USE_DATA_TYPE,
	USE_STR_SIZE,
	STR_DATA,
	NUM_DATA,
	FLT_DATA
FROM
	INIT_MST_/*$domainId*/
WHERE
	TABLE_NAME=/*tableName*/
	AND
	USE_DATA_TYPE <> '0'	/* 「未使用」は検索結果から外す */
