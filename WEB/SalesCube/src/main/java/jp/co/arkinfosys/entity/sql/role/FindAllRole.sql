SELECT
        ROLE_ID
        ,NAME
        ,REMARKS
        ,CRE_FUNC
        ,CRE_DATETM
        ,CRE_USER
        ,UPD_FUNC
        ,UPD_DATETM
        ,UPD_USER
    FROM
        ROLE_MST_/*$domainId*/
    ORDER BY
        ROLE_ID