SELECT
        TABLE_NAME
        ,ID
        ,WARNING_ID
        ,CRE_FUNC
        ,CRE_DATETM
        ,CRE_USER
        ,UPD_FUNC
        ,UPD_DATETM
        ,UPD_USER
    FROM
        SEQ_MAKER_/*$domainId*/
	WHERE
		TABLE_NAME = /*tableName*/'DEFAULT'
    FOR UPDATE