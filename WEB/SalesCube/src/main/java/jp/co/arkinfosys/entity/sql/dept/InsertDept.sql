INSERT
    INTO
        DEPT_MST_/*$domainId*/ (
			DEPT_ID
			,NAME
			,PARENT_ID
			,CRE_FUNC
			,CRE_DATETM
			,CRE_USER
			,UPD_FUNC
			,UPD_DATETM
			,UPD_USER
        )
        VALUES
        (
			/*deptId*/''
			,/*name*/''
			,/*parentId*/
			,/*creFunc*/NULL
			,now()
			,/*creUser*/NULL
			,/*updFunc*/NULL
			,now()
			,/*updUser*/NULL
        )

