SELECT
        DEPT_ID
        ,NAME
        ,PARENT_ID
        ,CRE_FUNC
        ,CRE_DATETM
        ,CRE_USER
        ,UPD_FUNC
        ,UPD_DATETM
        ,UPD_USER
    FROM
        DEPT_MST_/*$domainId*/
    WHERE
    	DEPT_ID = /*deptId*/
    FOR UPDATE