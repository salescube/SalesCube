INSERT INTO STDDEV_AGGREGATE_YM_WORK_/*$domainId*/ (
	SESSION_ID,
	YM,
	PRODUCT_CODE,
	QUANTITY
)
(
	SELECT DISTINCT
		/*sessionId*/ AS SESSION_ID,
		W.YM,
		A.PRODUCT_CODE,
		0
	FROM
		STDDEV_YM_WORK_/*$domainId*/ W CROSS JOIN (
			SELECT DISTINCT
				P.PRODUCT_CODE
			FROM
				PRODUCT_MST_/*$domainId*/ P
				INNER JOIN SALES_LINE_TRN_/*$domainId*/ SL ON P.PRODUCT_CODE = SL.PRODUCT_CODE
				INNER JOIN SALES_SLIP_TRN_/*$domainId*/ SS ON SL.SALES_SLIP_ID = SS.SALES_SLIP_ID
				LEFT OUTER JOIN RACK_MST_/*$domainId*/ R ON P.RACK_CODE = R.RACK_CODE
			/*BEGIN*/
			WHERE
				/*IF aggregateMonthsRange != null*/
				SS.SALES_DATE BETWEEN DATE_FORMAT(DATE_SUB(now(), interval /*aggregateMonthsRange*/ month), '%Y-%m-01') AND now()
				/*END*/

				/*IF roExists == true*/
				AND EXISTS (
					SELECT 1 FROM RO_LINE_TRN_/*$domainId*/ RL WHERE RL.PRODUCT_CODE=P.PRODUCT_CODE
				)
				/*END*/

				/*IF productCode != null*/
				AND P.PRODUCT_CODE = /*productCode*/
				/*END*/

				/*IF supplierCode != null*/
				AND P.SUPPLIER_CODE = /*supplierCode*/
				/*END*/

				/*IF setTypeCategory != null*/
				AND P.SET_TYPE_CATEGORY = /*setTypeCategory*/
				/*END*/

				/*IF productStatusCategory != null*/
				AND P.PRODUCT_STATUS_CATEGORY = /*productStatusCategory*/
				/*END*/

				/*IF stockCtlCategory != null*/
				AND P.STOCK_CTL_CATEGORY = /*stockCtlCategory*/
				/*END*/

				/*IF rackMultiFlag != null*/
				AND R.MULTI_FLAG = /*rackMultiFlag*/
				/*END*/
			/*END*/
		) A
)