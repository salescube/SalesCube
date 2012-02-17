SELECT
    COUNT(*) as allCnt
    ,SUM(IF(STATUS=/*condOrderd*/,1,0)) as  orderdCnt
    ,SUM(IF(STATUS=/*condNowPurchasing*/,1,0)) as  nowpurchasedCnt
    ,SUM(IF(STATUS=/*condPurchased*/,1,0)) as  purchasedCnt
FROM
	PO_LINE_TRN_/*$domainId*/ SLIP
WHERE
	PO_SLIP_ID=/*poSlipId*/0