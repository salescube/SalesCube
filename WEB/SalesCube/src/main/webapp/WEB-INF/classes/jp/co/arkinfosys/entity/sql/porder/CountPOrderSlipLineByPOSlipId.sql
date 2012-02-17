SELECT
        COUNT(DISTINCT PO_LINE_ID)
    FROM
        PO_LINE_TRN_/*$domainId*/
    WHERE
        PO_SLIP_ID = /*poSlipId*/
    GROUP BY
        PO_SLIP_ID
