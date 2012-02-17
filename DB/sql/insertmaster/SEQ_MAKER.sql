INSERT INTO SEQ_MAKER_SALES(
    TABLE_NAME,
    ID,
    WARNING_ID,
    CRE_FUNC,
    CRE_DATETM,
    CRE_USER,
    UPD_FUNC,
    UPD_DATETM,
    UPD_USER
)
SELECT
    'DELIVERY_MST',
    IFNULL(MAX(CAST(DELIVERY_CODE AS SIGNED)), 0),
    0,
    '初期登録',
    now(),
    'ARK',
    '初期登録',
    now(),
    'ARK'
FROM
    DELIVERY_MST_SALES
;

INSERT INTO SEQ_MAKER_SALES(
    TABLE_NAME,
    ID,
    WARNING_ID,
    CRE_FUNC,
    CRE_DATETM,
    CRE_USER,
    UPD_FUNC,
    UPD_DATETM,
    UPD_USER
)
SELECT
    'BANK_MST',
    IFNULL(MAX(BANK_ID), 0),
    0,
    '初期登録',
    now(),
    'ARK',
    '初期登録',
    now(),
    'ARK'
FROM
    BANK_MST_SALES
;

INSERT INTO SEQ_MAKER_SALES(
    TABLE_NAME,
    ID,
    WARNING_ID,
    CRE_FUNC,
    CRE_DATETM,
    CRE_USER,
    UPD_FUNC,
    UPD_DATETM,
    UPD_USER
)
SELECT
    'RATE_MST',
    IFNULL(MAX(RATE_ID), 0),
    0,
    '初期登録',
    now(),
    'ARK',
    '初期登録',
    now(),
    'ARK'
FROM
    RATE_MST_SALES
;

INSERT INTO SEQ_MAKER_SALES(
    TABLE_NAME,
    ID,
    WARNING_ID,
    CRE_FUNC,
    CRE_DATETM,
    CRE_USER,
    UPD_FUNC,
    UPD_DATETM,
    UPD_USER
)
SELECT
    'RO_SLIP_TRN',
    IFNULL(MAX(RO_SLIP_ID), 0),
    0,
    '初期登録',
    now(),
    'ARK',
    '初期登録',
    now(),
    'ARK'
FROM
    RO_SLIP_TRN_SALES
;

INSERT INTO SEQ_MAKER_SALES(
    TABLE_NAME,
    ID,
    WARNING_ID,
    CRE_FUNC,
    CRE_DATETM,
    CRE_USER,
    UPD_FUNC,
    UPD_DATETM,
    UPD_USER
)
SELECT
    'RO_LINE_TRN',
    IFNULL(MAX(RO_LINE_ID), 0),
    0,
    '初期登録',
    now(),
    'ARK',
    '初期登録',
    now(),
    'ARK'
FROM
    RO_LINE_TRN_SALES
;

INSERT INTO SEQ_MAKER_SALES(
    TABLE_NAME,
    ID,
    WARNING_ID,
    CRE_FUNC,
    CRE_DATETM,
    CRE_USER,
    UPD_FUNC,
    UPD_DATETM,
    UPD_USER
)
SELECT
    'SALES_SLIP_TRN',
    IFNULL(MAX(SALES_SLIP_ID), 0),
    0,
    '初期登録',
    now(),
    'ARK',
    '初期登録',
    now(),
    'ARK'
FROM
    SALES_SLIP_TRN_SALES
;

INSERT INTO SEQ_MAKER_SALES(
    TABLE_NAME,
    ID,
    WARNING_ID,
    CRE_FUNC,
    CRE_DATETM,
    CRE_USER,
    UPD_FUNC,
    UPD_DATETM,
    UPD_USER
)
SELECT
    'SALES_LINE_TRN',
    IFNULL(MAX(SALES_LINE_ID), 0),
    0,
    '初期登録',
    now(),
    'ARK',
    '初期登録',
    now(),
    'ARK'
FROM
    SALES_LINE_TRN_SALES
;

INSERT INTO SEQ_MAKER_SALES(
    TABLE_NAME,
    ID,
    WARNING_ID,
    CRE_FUNC,
    CRE_DATETM,
    CRE_USER,
    UPD_FUNC,
    UPD_DATETM,
    UPD_USER
)
SELECT
    'BILL_TRN',
    IFNULL(MAX(BILL_ID), 0),
    0,
    '初期登録',
    now(),
    'ARK',
    '初期登録',
    now(),
    'ARK'
FROM
    BILL_TRN_SALES
;

INSERT INTO SEQ_MAKER_SALES(
    TABLE_NAME,
    ID,
    WARNING_ID,
    CRE_FUNC,
    CRE_DATETM,
    CRE_USER,
    UPD_FUNC,
    UPD_DATETM,
    UPD_USER
)
SELECT
    'DEPOSIT_SLIP_TRN',
    IFNULL(MAX(DEPOSIT_SLIP_ID), 0),
    0,
    '初期登録',
    now(),
    'ARK',
    '初期登録',
    now(),
    'ARK'
FROM
    DEPOSIT_SLIP_TRN_SALES
;

INSERT INTO SEQ_MAKER_SALES(
    TABLE_NAME,
    ID,
    WARNING_ID,
    CRE_FUNC,
    CRE_DATETM,
    CRE_USER,
    UPD_FUNC,
    UPD_DATETM,
    UPD_USER
)
SELECT
    'DEPOSIT_LINE_TRN',
    IFNULL(MAX(DEPOSIT_LINE_ID), 0),
    0,
    '初期登録',
    now(),
    'ARK',
    '初期登録',
    now(),
    'ARK'
FROM
    DEPOSIT_LINE_TRN_SALES
;

INSERT INTO SEQ_MAKER_SALES(
    TABLE_NAME,
    ID,
    WARNING_ID,
    CRE_FUNC,
    CRE_DATETM,
    CRE_USER,
    UPD_FUNC,
    UPD_DATETM,
    UPD_USER
)
SELECT
    'PO_SLIP_TRN',
    IFNULL(MAX(PO_SLIP_ID), 0),
    0,
    '初期登録',
    now(),
    'ARK',
    '初期登録',
    now(),
    'ARK'
FROM
    PO_SLIP_TRN_SALES
;

INSERT INTO SEQ_MAKER_SALES(
    TABLE_NAME,
    ID,
    WARNING_ID,
    CRE_FUNC,
    CRE_DATETM,
    CRE_USER,
    UPD_FUNC,
    UPD_DATETM,
    UPD_USER
)
SELECT
    'PO_LINE_TRN',
    IFNULL(MAX(PO_LINE_ID), 0),
    0,
    '初期登録',
    now(),
    'ARK',
    '初期登録',
    now(),
    'ARK'
FROM
    PO_LINE_TRN_SALES
;

INSERT INTO SEQ_MAKER_SALES(
    TABLE_NAME,
    ID,
    WARNING_ID,
    CRE_FUNC,
    CRE_DATETM,
    CRE_USER,
    UPD_FUNC,
    UPD_DATETM,
    UPD_USER
)
SELECT
    'SUPPLIER_SLIP_TRN',
    IFNULL(MAX(SUPPLIER_SLIP_ID), 0),
    0,
    '初期登録',
    now(),
    'ARK',
    '初期登録',
    now(),
    'ARK'
FROM
    SUPPLIER_SLIP_TRN_SALES
;

INSERT INTO SEQ_MAKER_SALES(
    TABLE_NAME,
    ID,
    WARNING_ID,
    CRE_FUNC,
    CRE_DATETM,
    CRE_USER,
    UPD_FUNC,
    UPD_DATETM,
    UPD_USER
)
SELECT
    'SUPPLIER_LINE_TRN',
    IFNULL(MAX(SUPPLIER_LINE_ID), 0),
    0,
    '初期登録',
    now(),
    'ARK',
    '初期登録',
    now(),
    'ARK'
FROM
    SUPPLIER_LINE_TRN_SALES
;

INSERT INTO SEQ_MAKER_SALES(
    TABLE_NAME,
    ID,
    WARNING_ID,
    CRE_FUNC,
    CRE_DATETM,
    CRE_USER,
    UPD_FUNC,
    UPD_DATETM,
    UPD_USER
)
SELECT
    'PAYMENT_SLIP_TRN',
    IFNULL(MAX(PAYMENT_SLIP_ID), 0),
    0,
    '初期登録',
    now(),
    'ARK',
    '初期登録',
    now(),
    'ARK'
FROM
    PAYMENT_SLIP_TRN_SALES
;

INSERT INTO SEQ_MAKER_SALES(
    TABLE_NAME,
    ID,
    WARNING_ID,
    CRE_FUNC,
    CRE_DATETM,
    CRE_USER,
    UPD_FUNC,
    UPD_DATETM,
    UPD_USER
)
SELECT
    'PAYMENT_LINE_TRN',
    IFNULL(MAX(PAYMENT_LINE_ID), 0),
    0,
    '初期登録',
    now(),
    'ARK',
    '初期登録',
    now(),
    'ARK'
FROM
    PAYMENT_LINE_TRN_SALES
;

INSERT INTO SEQ_MAKER_SALES(
    TABLE_NAME,
    ID,
    WARNING_ID,
    CRE_FUNC,
    CRE_DATETM,
    CRE_USER,
    UPD_FUNC,
    UPD_DATETM,
    UPD_USER
)
SELECT
    'ART_BALANCE_TRN',
    IFNULL(MAX(ART_BALANCE_ID), 0),
    0,
    '初期登録',
    now(),
    'ARK',
    '初期登録',
    now(),
    'ARK'
FROM
    ART_BALANCE_TRN_SALES
;

INSERT INTO SEQ_MAKER_SALES(
    TABLE_NAME,
    ID,
    WARNING_ID,
    CRE_FUNC,
    CRE_DATETM,
    CRE_USER,
    UPD_FUNC,
    UPD_DATETM,
    UPD_USER
)
SELECT
    'DISCOUNT_TRN',
    IFNULL(MAX(DISCOUNT_DATA_ID), 0),
    0,
    '初期登録',
    now(),
    'ARK',
    '初期登録',
    now(),
    'ARK'
FROM
    DISCOUNT_TRN_SALES
;

INSERT INTO SEQ_MAKER_SALES(
    TABLE_NAME,
    ID,
    WARNING_ID,
    CRE_FUNC,
    CRE_DATETM,
    CRE_USER,
    UPD_FUNC,
    UPD_DATETM,
    UPD_USER
)
SELECT
    'CUSTOMER_RANK_MST',
    IFNULL(MAX(RANK_CODE), 0),
    0,
    '初期登録',
    now(),
    'ARK',
    '初期登録',
    now(),
    'ARK'
FROM
    CUSTOMER_RANK_MST_SALES
;

