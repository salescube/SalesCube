UPDATE
        DEPT_MST_/*$domainId*/
    SET
        NAME = /*name*/NULL
        ,PARENT_ID = /*parentId*/NULL
        ,UPD_FUNC = /*updFunc*/NULL
        ,UPD_DATETM = now()
        ,UPD_USER = /*updUser*/NULL
    WHERE
        DEPT_ID = /*deptId*/