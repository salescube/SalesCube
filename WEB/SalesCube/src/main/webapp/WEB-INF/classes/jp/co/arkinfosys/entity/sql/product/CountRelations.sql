SELECT
        (
            SELECT
                    COUNT(PS.SET_PRODUCT_CODE)
                FROM
                    PRODUCT_SET_MST_/*$domainId*/ PS
                WHERE
                    PS.PRODUCT_CODE = P.PRODUCT_CODE
        ) AS SET_PRODUCT
        ,(
            SELECT
                    COUNT(STOK.PRODUCT_CODE)
                FROM
                    PRODUCT_STOCK_TRN_/*$domainId*/ STOK
                WHERE
                    STOK.PRODUCT_CODE = P.PRODUCT_CODE
        ) AS PRODUCT_STOCK
        ,(
            SELECT
                    COUNT(EL.ESTIMATE_LINE_ID)
                FROM
                    ESTIMATE_LINE_TRN_/*$domainId*/ EL
                WHERE
                    EL.PRODUCT_CODE = P.PRODUCT_CODE
        ) AS ESTIMATE_SLIP
        ,(
            SELECT
                    COUNT(RL.RO_LINE_ID)
                FROM
                    RO_LINE_TRN_/*$domainId*/ RL
                WHERE
                    RL.PRODUCT_CODE = P.PRODUCT_CODE
        ) AS RO_SLIP
        ,(
            SELECT
                    COUNT(SL.SALES_LINE_ID)
                FROM
                    SALES_LINE_TRN_/*$domainId*/ SL
                WHERE
                    SL.PRODUCT_CODE = P.PRODUCT_CODE
        ) AS SALES_SLIP
        ,(
            SELECT
                    COUNT(PL.PICKING_LINE_ID)
                FROM
                    PICKING_LINE_TRN_/*$domainId*/ PL
                WHERE
                    PL.PRODUCT_CODE = P.PRODUCT_CODE
        ) AS PICKING_LIST
        ,(
            SELECT
                    COUNT(PL.PO_LINE_ID)
                FROM
                    PO_LINE_TRN_/*$domainId*/ PL
                WHERE
                    PL.PRODUCT_CODE = P.PRODUCT_CODE
        ) AS PO_SLIP
        ,(
            SELECT
                    COUNT(SL.SUPPLIER_LINE_ID)
                FROM
                    SUPPLIER_LINE_TRN_/*$domainId*/ SL
                WHERE
                    SL.PRODUCT_CODE = P.PRODUCT_CODE
        ) AS SUPPLIER_SLIP
        ,(
            SELECT
                    COUNT(PL.PAYMENT_LINE_ID)
                FROM
                    PAYMENT_LINE_TRN_/*$domainId*/ PL
                WHERE
                    PL.PRODUCT_CODE = P.PRODUCT_CODE
        ) AS PAYMENT_SLIP
        ,(
            SELECT
                    COUNT(EL.EAD_LINE_ID)
                FROM
                    EAD_LINE_TRN_/*$domainId*/ EL
                WHERE
                    EL.PRODUCT_CODE = P.PRODUCT_CODE
        ) AS EAD_SLIP
        ,(
            SELECT
                    COUNT(APT.APT_BALANCE_ID)
                FROM
                    APT_BALANCE_TRN_/*$domainId*/ APT
                WHERE
                    APT.PRODUCT_CODE = P.PRODUCT_CODE
        ) AS APT_BALANCE
    FROM
        PRODUCT_MST_/*$domainId*/ P
    WHERE
        P.PRODUCT_CODE = /*productCode*/'S'