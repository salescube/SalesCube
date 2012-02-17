SELECT
        C.CUSTOMER_CODE
        ,C.CUSTOMER_NAME
        ,MAX(B.BILL_CUTOFF_DATE) LAST_CUTOFF_DATE
    FROM
        CUSTOMER_MST_/*$domainId*/ C
            LEFT OUTER JOIN BILL_TRN_/*$domainId*/ B
                ON C.CUSTOMER_CODE = B.CUSTOMER_CODE
	WHERE
		( C.BILL_PRINT_UNIT = '1' )
		AND
		(
			C.SALES_CM_CATEGORY = /*salesCmCategory*/
			/*IF notYetRequestedCheck == false */
			OR
			EXISTS (
				SELECT 1
				FROM SALES_SLIP_TRN_/*$domainId*/ S
				WHERE
					S.CUSTOMER_CODE = C.CUSTOMER_CODE
					AND ( S.SALES_CM_CATEGORY = '0' OR S.SALES_CM_CATEGORY = '1' )
					AND S.STATUS = '0'
			)
			OR
			EXISTS (
				SELECT 1
				FROM DEPOSIT_SLIP_TRN_/*$domainId*/ D
				WHERE
					D.CUSTOMER_CODE = C.CUSTOMER_CODE
					AND ( D.DEPOSIT_CATEGORY = '03' OR D.DEPOSIT_CATEGORY = '01' OR D.DEPOSIT_CATEGORY = '09' OR D.DEPOSIT_CATEGORY = '06' )
					AND D.STATUS = '0'
			)
			/*END*/
		)
		/*IF customerCode != null */
		AND C.CUSTOMER_CODE LIKE /*customerCode*/'S%'
		/*END*/
		/*IF customerName != null */
		AND C.CUSTOMER_NAME LIKE /*customerName*/'%S%'
		/*END*/
		/*IF paybackCycleCategory != null */
		AND C.PAYBACK_CYCLE_CATEGORY = /*paybackCycleCategory*/'00'
		/*END*/
		/*IF cutoffGroup != null */
		AND C.CUTOFF_GROUP = /*cutoffGroup*/'00'
		/*END*/
		/*IF notYetRequestedCheck == true */
		AND
		(
			EXISTS (
						SELECT 1
						FROM SALES_SLIP_TRN_/*$domainId*/ S
						WHERE
							S.CUSTOMER_CODE = C.CUSTOMER_CODE
							AND ( S.SALES_CM_CATEGORY = '0' OR S.SALES_CM_CATEGORY = '1' )
							AND S.STATUS = '0'
							AND S.SALES_DATE <=
								if(C.CUTOFF_GROUP<>'31',
									STR_TO_DATE( concat(DATE_FORMAT( C.LAST_CUTOFF_DATE , '%Y/%m/'),C.CUTOFF_GROUP) ,'%Y/%m/%d'),
									LAST_DAY( C.LAST_CUTOFF_DATE ) )
					)
			OR
			EXISTS (
						SELECT 1
						FROM DEPOSIT_SLIP_TRN_/*$domainId*/ D
						WHERE
							D.CUSTOMER_CODE = C.CUSTOMER_CODE
							AND ( D.DEPOSIT_CATEGORY = '03' OR D.DEPOSIT_CATEGORY = '01' OR D.DEPOSIT_CATEGORY = '09' OR D.DEPOSIT_CATEGORY = '06' )
							AND D.STATUS = '0'
							AND D.DEPOSIT_DATE <=
									if(C.CUTOFF_GROUP<>'31',
									STR_TO_DATE( concat(DATE_FORMAT( C.LAST_CUTOFF_DATE , '%Y/%m/'),C.CUTOFF_GROUP) ,'%Y/%m/%d'),
									LAST_DAY( C.LAST_CUTOFF_DATE ) )
					)
		)
		/*END*/
    GROUP BY
        C.CUSTOMER_CODE
        ,C.CUSTOMER_NAME
	ORDER BY
		C.CUSTOMER_CODE ASC
