SELECT
        TABLE_NAME
        ,COLUMN_NAME
        ,TITLE
        ,CATEGORY_ID
        ,USE_DATA_TYPE
        ,USE_STR_SIZE
        ,STR_DATA
        ,NUM_DATA
        ,FLT_DATA
        ,REMARKS
        ,CRE_FUNC
        ,CRE_DATETM
        ,CRE_USER
        ,UPD_FUNC
        ,UPD_DATETM
        ,UPD_USER
    FROM
        INIT_MST_/*$domainId*/
    WHERE
        TABLE_NAME = /*tableName*/'TABLE_NAME'
        AND COLUMN_NAME = /*columnName*/'COLUMN_NAME'
	FOR UPDATE