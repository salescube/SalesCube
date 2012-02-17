UPDATE CUSTOMER_RANK_MST_/*$domainId*/ SET
	RANK_NAME=/*rankName*/,
	RANK_RATE=/*rankRate*/,
	RO_COUNT_FROM=/*roCountFrom*/,
	RO_COUNT_TO=/*roCountTo*/,
	ENROLL_TERM_FROM=/*enrollTermFrom*/,
	ENROLL_TERM_TO=/*enrollTermTo*/,
	DEFECT_TERM_FROM=/*defectTermFrom*/,
	DEFECT_TERM_TO=/*defectTermTo*/,
	RO_MONTHLY_AVG_FROM=/*roMonthlyAvgFrom*/,
	RO_MONTHLY_AVG_TO=/*roMonthlyAvgTo*/,
	POSTAGE_TYPE=/*postageType*/,
	REMARKS=/*remarks*/,
	UPD_FUNC=/*updFunc*/,
	UPD_DATETM=now(),
	UPD_USER=/*updUser*/
WHERE
    RANK_CODE=/*rankCode*/
