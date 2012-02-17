SELECT
        (
            SELECT
                    COUNT(E.ESTIMATE_SHEET_ID)
                FROM
                    ESTIMATE_SHEET_TRN_/*$domainId*/ E
                WHERE
                    E.CUSTOMER_CODE = C.CUSTOMER_CODE
        ) AS ESTIMATE_SLIP
        ,(
            SELECT
                    COUNT(RO.RO_SLIP_ID)
                FROM
                    RO_SLIP_TRN_/*$domainId*/ RO
                WHERE
                    RO.CUSTOMER_CODE = C.CUSTOMER_CODE
        ) AS RO_SLIP
        ,(
            SELECT
                    COUNT(S.SALES_SLIP_ID)
                FROM
                    SALES_SLIP_TRN_/*$domainId*/ S
                WHERE
                    S.CUSTOMER_CODE = C.CUSTOMER_CODE
        ) AS SALES_SLIP
        ,(
            SELECT
                    COUNT(P.PICKING_LIST_ID)
                FROM
                    PICKING_LIST_TRN_/*$domainId*/ P
                WHERE
                    P.CUSTOMER_CODE = C.CUSTOMER_CODE
        ) AS PICKING_LIST
        ,(
            SELECT
                    COUNT(B.BILL_ID)
                FROM
                    BILL_TRN_/*$domainId*/ B
                WHERE
                    B.CUSTOMER_CODE = C.CUSTOMER_CODE
        ) AS BILL
        ,(
            SELECT
                    COUNT(D.DEPOSIT_SLIP_ID)
                FROM
                    DEPOSIT_SLIP_TRN_/*$domainId*/ D
                WHERE
                    D.CUSTOMER_CODE = C.CUSTOMER_CODE
        ) AS DEPOSIT_SLIP
        ,(
            SELECT
                    COUNT(ART.ART_BALANCE_ID)
                FROM
                    ART_BALANCE_TRN_/*$domainId*/ ART
                WHERE
                    ART.CUSTOMER_CODE = C.CUSTOMER_CODE
        ) AS ART_BALANCE
    FROM
        CUSTOMER_MST_/*$domainId*/ C
    WHERE
        C.CUSTOMER_CODE = /*customerCode*/
