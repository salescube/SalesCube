SELECT
        SL.SUPPLIER_SLIP_ID
        ,SL.SUPPLIER_LINE_ID
        ,SL.QUANTITY
        ,SL.UNIT_PRICE
        ,SL.PRICE
        ,SL.DOL_UNIT_PRICE
        ,SL.DOL_PRICE
        ,SL.PO_LINE_ID
        ,SS.SUPPLIER_DATE
        ,SS.SUPPLIER_CODE
    FROM
        SUPPLIER_LINE_TRN_/*$domainId*/ SL
            LEFT OUTER JOIN SUPPLIER_SLIP_TRN_/*$domainId*/ SS
                ON SL.SUPPLIER_SLIP_ID = SS.SUPPLIER_SLIP_ID
    WHERE
        SS.SUPPLIER_CODE = /*supplierCode*/
        AND SS.SUPPLIER_DATE <= /*supplierCutoffDate*/
        AND SL.PO_LINE_ID IS NOT NULL
        AND (
            SL.STATUS = /*unPaid*/
            OR (	-- 支払済ステータスでも締日以降に支払いがある場合は、締日時点で未払いだったと見なす
                SL.STATUS = /*paid*/
                AND EXISTS (
                    SELECT
                            1
                        FROM
                            PAYMENT_LINE_TRN_/*$domainId*/ PL INNER JOIN PAYMENT_SLIP_TRN_/*$domainId*/ PS
                                ON PS.PAYMENT_SLIP_ID = PL.PAYMENT_SLIP_ID
                    WHERE
                        PS.PAYMENT_DATE > /*supplierCutoffDate*/
                        AND PL.SUPPLIER_LINE_ID = SL.SUPPLIER_LINE_ID
                )
            )
        )