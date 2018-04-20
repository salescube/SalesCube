SELECT
        FILE_ID
        ,TITLE
        ,FILE_NAME
        ,REAL_FILE_NAME
        ,FILE_SIZE
        ,OPEN_LEVEL
        ,CRE_FUNC
        ,CRE_DATETM
        ,CRE_USER
        ,UPD_FUNC
        ,UPD_DATETM
        ,UPD_USER
    FROM
        FILE_INFO_/*$domainId*/
    WHERE
    	FILE_ID = /*fileId*/''
    	AND OPEN_LEVEL <= /*openLevel*/'0'
