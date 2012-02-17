INSERT
    INTO
        SEQ_MAKER_/*$domainId*/ (
            TABLE_NAME
            ,ID
            ,WARNING_ID
            ,CRE_FUNC
            ,CRE_DATETM
            ,CRE_USER
            ,UPD_FUNC
            ,UPD_DATETM
            ,UPD_USER
        )
VALUES (
			/*tableName*/
			,/*id*/1
			,/*warningId*/0
			,/*creFunc*/NULL
			,now()
			,/*creUser*/NULL
			,/*updFunc*/NULL
			,now()
			,/*updUser*/NULL
)
