-- 親メニューを取得
SELECT
        M1.MENU_ID
        ,M1.CAPTION
        ,M1.DESCRIPTION
        ,M1.URL
        ,M1.PARENT_ID
        ,M1.SEQ
        ,M1.VALID_TYPE
        ,M1.FONT_COLOR
        ,M1.BG_COLOR
        ,NULL AS VALID_FLAG
    FROM
        MENU_MST_/*$domainId*/ M1
    WHERE
        M1.PARENT_ID IS NULL AND  M1.MENU_ID NOT IN ('1407','1500') -- ファイル参照画面，商品ダウンロード/アップロードを除く
UNION ALL
-- 子メニューを取得
SELECT
        M2.MENU_ID
        ,M2.CAPTION
        ,M2.DESCRIPTION
        ,M2.URL
        ,M2.PARENT_ID
        ,M2.SEQ
        ,M2.VALID_TYPE
        ,M2.FONT_COLOR
        ,M2.BG_COLOR
        ,MAX(RC.VALID_FLAG) AS VALID_FLAG
    FROM
        MENU_MST_/*$domainId*/ M2
            LEFT OUTER JOIN ROLE_CFG_/*$domainId*/ RC
                ON M2.MENU_ID = RC.MENU_ID
            LEFT OUTER JOIN GRANT_ROLE_/*$domainId*/ GR
                ON RC.ROLE_ID = GR.ROLE_ID
    WHERE
        GR.USER_ID = /*userId*/''
        AND RC.VALID_FLAG != '0'
    GROUP BY
        M2.MENU_ID
        ,M2.CAPTION
        ,M2.DESCRIPTION
        ,M2.URL
        ,M2.PARENT_ID
        ,M2.SEQ
        ,M2.VALID_TYPE
        ,M2.FONT_COLOR
        ,M2.BG_COLOR
    ORDER BY
        MENU_ID
