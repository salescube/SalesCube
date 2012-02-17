SELECT
	CUST.CUSTOMER_CODE
FROM
	CUSTOMER_MST_/*$domainId*/ CUST LEFT OUTER JOIN ART_BALANCE_TRN_/*$domainId*/ LAST_ART
		ON CUST.CUSTOMER_CODE = LAST_ART.CUSTOMER_CODE
		AND LAST_ART.ART_CUTOFF_DATE =
			(
				SELECT MAX(ART_CUTOFF_DATE)
				FROM ART_BALANCE_TRN_/*$domainId*/ IN_ART
				WHERE CUST.CUSTOMER_CODE = IN_ART.CUSTOMER_CODE
				AND IN_ART.ART_CUTOFF_DATE <= /*artBalanceCheckDate*/'2010/06/30'
			)
/*BEGIN*/
WHERE
	/*IF customerCodeFrom != "" */
	CUST.CUSTOMER_CODE >= /*customerCodeFrom*/'0010008071'
	/*END*/
	/*IF customerCodeTo != "" */
	AND CUST.CUSTOMER_CODE <= /*customerCodeTo*/'0210008071'
	/*END*/
	/*IF artBalanceCheckDate != "" */
	AND (
			( LAST_ART.THIS_ART_PRICE IS NOT NULL AND LAST_ART.THIS_ART_PRICE <> 0 )
			OR
			exists (
					SELECT 1
					FROM SALES_SLIP_TRN_/*$domainId*/ SALES
					WHERE CUST.CUSTOMER_CODE = SALES.CUSTOMER_CODE
						AND (
								(	-- 最終の売掛締めの翌日から調査日までの間に売上・入金がある場合売掛残高出力対象とする
									SALES_DATE BETWEEN ( DATE_ADD(LAST_ART.ART_CUTOFF_DATE, INTERVAL 1 DAY) ) AND /*artBalanceCheckDate*/'2010/06/30'
								)
								OR
								(	-- 同一月の売掛残高を軸に調べる場合は、売掛締め日から調査日までの間の売上入金だけでは取りこぼす場合があるので、月初めから調査日までに範囲を拡張する
									MONTH(/*artBalanceCheckDate*/'2010/06/30') = MONTH(LAST_ART.ART_CUTOFF_DATE)
									AND SALES_DATE BETWEEN DATE_FORMAT( /*artBalanceCheckDate*/'2010/06/30' ,'%Y/%m/01') AND /*artBalanceCheckDate*/'2010/06/30'
								)
								OR
								(	-- 最終の売掛締めの際に請求漏れがあった場合取りこぼすので、請求漏れデータも検索する
									SALES_DATE <= LAST_ART.ART_CUTOFF_DATE AND (BILL_CUTOFF_DATE IS NULL OR BILL_CUTOFF_DATE > LAST_ART.ART_CUTOFF_DATE )
								)
								OR
								(	-- 売掛締めがされていない場合、全ての売上伝票・入金伝票を対象とする
									LAST_ART.THIS_ART_PRICE IS NULL
								)
							)
			) OR
			exists (
					SELECT 1
					FROM DEPOSIT_SLIP_TRN_/*$domainId*/ DEPOSIT
					WHERE CUST.CUSTOMER_CODE = DEPOSIT.CUSTOMER_CODE
						AND (
								(	-- 最終の売掛締めの翌日から調査日までの間に売上・入金がある場合売掛残高出力対象とする
									DEPOSIT_DATE BETWEEN ( DATE_ADD(LAST_ART.ART_CUTOFF_DATE, INTERVAL 1 DAY) ) AND /*artBalanceCheckDate*/'2010/06/30'
								)
								OR
								(	-- 同一月の売掛残高を軸に調べる場合は、売掛締め日から調査日までの間の売上入金だけでは取りこぼす場合があるので、月初めから調査日までに範囲を拡張する
									MONTH(/*artBalanceCheckDate*/'2010/06/30') = MONTH(LAST_ART.ART_CUTOFF_DATE)
									AND DEPOSIT_DATE BETWEEN DATE_FORMAT( /*artBalanceCheckDate*/'2010/06/30' ,'%Y/%m/01') AND /*artBalanceCheckDate*/'2010/06/30'
								)
								OR
								(	-- 最終の売掛締めの際に請求漏れがあった場合取りこぼすので、請求漏れデータも検索する
									DEPOSIT_DATE <= LAST_ART.ART_CUTOFF_DATE AND (BILL_CUTOFF_DATE IS NULL OR BILL_CUTOFF_DATE > LAST_ART.ART_CUTOFF_DATE ) )
								OR
								(	-- 売掛締めがされていない場合、全ての売上伝票・入金伝票を対象とする
									LAST_ART.THIS_ART_PRICE IS NULL
								)
							)
			)
	)
	/*END*/
/*END*/
ORDER BY CUST.CUSTOMER_CODE