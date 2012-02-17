SELECT
	COUNT(DISTINCT SLIP.ART_BALANCE_ID)
FROM
	UNIT_ART_BALANCE_TRN_/*$domainId*/ SLIP
	-- 最も新しいデータを出す
    INNER JOIN
        (
        SELECT
            MAX(ART_CUTOFF_DATE) CD
            ,CUSTOMER_CODE
        FROM
		/*IF targetDate != null */
            UNIT_ART_BALANCE_TRN_/*$domainId*/ SLIP2
		/*END*/
        WHERE
            SLIP2.ART_YM = /*targetDate*/201001
        GROUP BY CUSTOMER_CODE
        ) AS SLIP3
        ON ( SLIP.CUSTOMER_CODE = SLIP3.CUSTOMER_CODE
            AND SLIP.ART_CUTOFF_DATE = SLIP3.CD )
	LEFT OUTER JOIN CATEGORY_TRN_/*$domainId*/ CAT1 ON (SLIP.SALES_CM_CATEGORY = CAT1.CATEGORY_CODE AND CAT1.CATEGORY_ID = /*salesCmCategory*/32)
WHERE
	-- 全ての金額がゼロの場合表示しないよ
	(
		COALESCE(SLIP.LAST_ART_PRICE,0) != 0
		OR (COALESCE(SLIP.DEPOSIT_PRICE,0) + COALESCE(SLIP.ADJ_PRICE,0)) != 0
		OR COALESCE(SLIP.SALES_PRICE,0) != 0
		OR COALESCE(SLIP.RGU_PRICE,0) != 0
		OR COALESCE(SLIP.DCT_PRICE,0) != 0
		OR COALESCE(SLIP.ETC_PRICE,0) != 0
		OR COALESCE(SLIP.CTAX_PRICE,0) != 0
		OR COALESCE(SLIP.THIS_ART_PRICE,0) != 0
	)
	/*IF targetDate != null */
	AND SLIP.ART_YM = /*targetDate*/201001
	/*END*/
	/*IF customerCodeFrom != null */
	AND SLIP.CUSTOMER_CODE >= /*customerCodeFrom*/'1'
	/*END*/
	/*IF customerCodeTo != null */
	AND SLIP.CUSTOMER_CODE <= /*customerCodeTo*/'1'
	/*END*/
