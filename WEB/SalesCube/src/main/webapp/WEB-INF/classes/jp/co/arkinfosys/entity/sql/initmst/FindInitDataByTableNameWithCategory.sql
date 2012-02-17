SELECT
        I.TABLE_NAME
        ,I.COLUMN_NAME
        ,I.TITLE
        ,I.CATEGORY_ID
        ,I.USE_DATA_TYPE
        ,I.USE_STR_SIZE
        ,I.STR_DATA
        ,I.NUM_DATA
        ,I.FLT_DATA
        ,I.REMARKS
        ,I.CRE_FUNC
        ,I.CRE_DATETM
        ,I.CRE_USER
        ,I.UPD_FUNC
        ,I.UPD_DATETM
        ,I.UPD_USER
        ,CT.CATEGORY_CODE
        ,CT.CATEGORY_CODE_NAME
        ,C.CATEGORY_DATA_TYPE
        ,CT.CATEGORY_STR
        ,CT.CATEGORY_NUM
        ,CT.CATEGORY_FLT
        ,CT.CATEGORY_BOOL
    FROM
        INIT_MST_/*$domainId*/ I
            LEFT OUTER JOIN CATEGORY_TRN_/*$domainId*/ CT
                ON I.CATEGORY_ID = CT.CATEGORY_ID
                AND CT.CATEGORY_DSP <> '0'
            LEFT OUTER JOIN CATEGORY_MST_/*$domainId*/ C
                ON CT.CATEGORY_ID = C.CATEGORY_ID
    WHERE
        I.TABLE_NAME = /*tableName*/'TABLE_NAME'
        AND USE_DATA_TYPE <> '0'
    ORDER BY
        I.CATEGORY_ID