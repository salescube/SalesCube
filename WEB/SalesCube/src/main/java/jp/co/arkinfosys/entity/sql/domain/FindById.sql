SELECT
        DOMAIN_ID
        ,MANAGER_NAME
        ,TELNO
        ,EMAIL
        ,REMARKS
        ,CRE_FUNC
        ,CRE_DATETM
        ,CRE_USER
        ,UPD_FUNC
        ,UPD_DATETM
        ,UPD_USER
    FROM
        DOMAIN_MST
    WHERE
        DOMAIN_ID =/*domainId*/'default'