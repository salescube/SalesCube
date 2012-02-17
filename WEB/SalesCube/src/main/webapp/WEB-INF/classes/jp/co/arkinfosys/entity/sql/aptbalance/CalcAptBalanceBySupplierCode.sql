SELECT
        IFNULL(SUM(PRICE_TOTAL), 0)
    FROM
        SUPPLIER_SLIP_TRN_/*$domainId*/
    WHERE
        SUPPLIER_CODE = /*supplierCode*/'S'
        AND STATUS = /*status*/'0'
