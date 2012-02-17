SELECT
        PS.RACK_CODE
        ,PS.PRODUCT_CODE
        ,PS.STOCK_PDATE
        ,PS.STOCK_ANNUAL
        ,PS.STOCK_MONTHLY
        ,PS.STOCK_YM
        ,PS.STOCK_NUM
        ,PS.ENTER_NUM
        ,PS.DISPATCH_NUM
        ,PS.REMARKS
        ,PS.CRE_FUNC
        ,PS.CRE_DATETM
        ,PS.CRE_USER
        ,PS.UPD_FUNC
        ,PS.UPD_DATETM
        ,PS.UPD_USER
    FROM
        PRODUCT_STOCK_TRN_/*$domainId*/ PS INNER JOIN (
            SELECT
                    RACK_CODE
                    ,PRODUCT_CODE
                    ,MAX(STOCK_PDATE) AS LAST_PDATE
                FROM
                    PRODUCT_STOCK_TRN_/*$domainId*/ PS
                GROUP BY
                    RACK_CODE
                    ,PRODUCT_CODE
        ) A
            ON A.RACK_CODE = PS.RACK_CODE
            AND A.PRODUCT_CODE = PS.PRODUCT_CODE
            AND A.LAST_PDATE = PS.STOCK_PDATE
