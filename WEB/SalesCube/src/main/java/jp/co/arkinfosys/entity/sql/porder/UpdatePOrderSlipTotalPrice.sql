UPDATE
	PO_SLIP_TRN_/*$domainId*/
	SET
		PRICE_TOTAL = (
			SELECT
				SUM(PRICE)
				FROM
					PO_LINE_TRN_/*$domainId*/
				WHERE
					PO_SLIP_ID = /*poSlipId*/
		)
		,CTAX_TOTAL =
			(SELECT SUM(PTOTAL.CTAX_PTOTAL) FROM (
				SELECT
					SUM(PRICE) * CTAX_RATE / 100.0 AS CTAX_PTOTAL
				FROM
					PO_LINE_TRN_/*$domainId*/
				WHERE
					PO_SLIP_ID = /*poSlipId*/
				GROUP BY
					CTAX_RATE
			) PTOTAL)
		,CTAX_TOTAL = /*ctaxTotal*/
		,FE_PRICE_TOTAL = (
			SELECT
				SUM(DOL_PRICE)
				FROM
					PO_LINE_TRN_/*$domainId*/
				WHERE
					PO_SLIP_ID = /*poSlipId*/
		)
	WHERE
		PO_SLIP_ID = /*poSlipId*/