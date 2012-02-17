UPDATE MINE_MST_/*$domainId*/'DEFAULT' SET
         STOCK_HOLD_TERM_CALC_CATEGORY=/*stockHoldTermCalcCategory*/
        ,STOCK_HOLD_DAYS=/*stockHoldDays*/
        ,MIN_PO_LOT_CALC_DAYS=/*minPoLotCalcDays*/
        ,MIN_PO_LOT_NUM=/*minPoLotNum*/
        ,MAX_PO_NUM_CALC_DAYS=/*maxPoNumcalcDays*/
        ,MIN_PO_NUM=/*minPoNum*/
        ,DEFICIENCY_RATE=/*deficiencyRate*/
        ,MAX_ENTRUST_PO_TIMELAG=/*maxEntrustPoTimelag*/
        ,SAFETY_COEFFICIENT=/*safetyCoefficient*/
        ,UPD_FUNC=/*updFunc*/NULL
        ,UPD_DATETM=now()
        ,UPD_USER=/*updUser*/NULL

