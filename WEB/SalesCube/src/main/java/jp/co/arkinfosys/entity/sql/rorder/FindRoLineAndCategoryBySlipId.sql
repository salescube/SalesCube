SELECT
    RL.RO_LINE_ID,
    RL.STATUS,
    RL.RO_SLIP_ID,
    RL.LINE_NO,
    RL.ESTIMATE_LINE_ID,
    RL.LAST_SHIP_DATE,
    RL.PRODUCT_CODE,
    RL.CUSTOMER_PCODE,
    RL.PRODUCT_ABSTRACT,
    RL.QUANTITY,
    RL.UNIT_PRICE,
    RL.UNIT_CATEGORY,
    RL.UNIT_NAME,
    RL.PACK_QUANTITY,
    RL.UNIT_RETAIL_PRICE,
    RL.RETAIL_PRICE,
    RL.UNIT_COST,
    RL.COST,
    RL.TAX_CATEGORY,
    RL.CTAX_RATE,
    RL.CTAX_PRICE,
    RL.REMARKS,
    RL.EAD_REMARKS,
    RL.PRODUCT_REMARKS,
    RL.REST_QUANTITY,
    RL.RACK_CODE_SRC,
    RL.CRE_FUNC,
    RL.CRE_DATETM,
    RL.CRE_USER,
    RL.UPD_FUNC,
    RL.UPD_DATETM,
    RL.UPD_USER,
    CT.CATEGORY_CODE_NAME STATUS_NAME
FROM
    RO_LINE_TRN_/*$domainId*/ RL,
    CATEGORY_TRN_/*$domainId*/ CT
WHERE
    RO_SLIP_ID =/*roSlipId*/'default' AND
    RL.STATUS=CT.CATEGORY_CODE AND
    CT.CATEGORY_ID=/*categoryId*/
ORDER BY RL.LINE_NO