SELECT
    UPD_DATETM
    ,UPD_USER
FROM
    ESTIMATE_SHEET_TRN_/*$domainId*/
WHERE
    ESTIMATE_SHEET_ID =/*estimateSheetId*/'default'
FOR UPDATE