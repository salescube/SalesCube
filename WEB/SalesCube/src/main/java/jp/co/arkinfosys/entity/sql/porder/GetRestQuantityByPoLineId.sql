SELECT
        PRODUCT_CODE
        ,REST_QUANTITY AS QUANTITY
    FROM
        PO_LINE_TRN_/*$domainId*/
WHERE
    PO_LINE_ID = /*poLineId*/

