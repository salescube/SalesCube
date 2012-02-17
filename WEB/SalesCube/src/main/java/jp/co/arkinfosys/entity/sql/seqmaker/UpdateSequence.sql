UPDATE
        SEQ_MAKER_/*$domainId*/
    SET
        ID = /*id*/
        ,UPD_FUNC =/*updFunc*/NULL
        ,UPD_DATETM =now()
        ,UPD_USER =/*updUser*/NULL
    WHERE
        TABLE_NAME =/*tableName*/'DEFAULT'
