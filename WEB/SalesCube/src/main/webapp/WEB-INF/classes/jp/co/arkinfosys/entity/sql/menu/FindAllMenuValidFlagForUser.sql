SELECT
        M.MENU_ID
        ,M.CAPTION
        ,M.DESCRIPTION
        ,M.URL
        ,M.PARENT_ID
        ,M.SEQ
        ,M.VALID_TYPE
        ,M.FONT_COLOR
        ,M.BG_COLOR
        ,M.CRE_FUNC
        ,M.CRE_DATETM
        ,M.CRE_USER
        ,M.UPD_FUNC
        ,M.UPD_DATETM
        ,M.UPD_USER
        ,'0' AS VALID_FLAG
    FROM
        MENU_MST_/*$domainId*/ M
    WHERE
        NOT EXISTS (
            SELECT
                    1
                FROM
                    MENU_MST_/*$domainId*/ M2
                        LEFT OUTER JOIN ROLE_CFG_/*$domainId*/ RC
                            ON M2.MENU_ID = RC.MENU_ID
                        LEFT OUTER JOIN GRANT_ROLE_/*$domainId*/ GR
                            ON RC.ROLE_ID = GR.ROLE_ID
                WHERE
                    GR.USER_ID = /*userId*/''
                    AND RC.VALID_FLAG != '0'
                    AND M2.MENU_ID = M.MENU_ID
        )
        AND (
            M.PARENT_ID IS NOT NULL
            OR M.MENU_ID IN ('1407','1500')
        )
UNION ALL
SELECT
        M.MENU_ID
        ,M.CAPTION
        ,M.DESCRIPTION
        ,M.URL
        ,M.PARENT_ID
        ,M.SEQ
        ,M.VALID_TYPE
        ,M.FONT_COLOR
        ,M.BG_COLOR
        ,M.CRE_FUNC
        ,M.CRE_DATETM
        ,M.CRE_USER
        ,M.UPD_FUNC
        ,M.UPD_DATETM
        ,M.UPD_USER
        ,MAX(RC.VALID_FLAG) AS VALID_FLAG
    FROM
        MENU_MST_/*$domainId*/ M
            LEFT OUTER JOIN ROLE_CFG_/*$domainId*/ RC
                ON M.MENU_ID = RC.MENU_ID
            LEFT OUTER JOIN GRANT_ROLE_/*$domainId*/ GR
                ON RC.ROLE_ID = GR.ROLE_ID
    WHERE
        GR.USER_ID = /*userId*/''
        AND RC.VALID_FLAG != '0'
    GROUP BY
        M.MENU_ID
        ,M.CAPTION
        ,M.DESCRIPTION
        ,M.URL
        ,M.PARENT_ID
        ,M.SEQ
        ,M.VALID_TYPE
        ,M.FONT_COLOR
        ,M.BG_COLOR
        ,M.CRE_FUNC
        ,M.CRE_DATETM
        ,M.CRE_USER
        ,M.UPD_FUNC
        ,M.UPD_DATETM
        ,M.UPD_USER
    ORDER BY
        MENU_ID
