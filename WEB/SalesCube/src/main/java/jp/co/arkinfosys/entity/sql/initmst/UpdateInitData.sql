UPDATE
        INIT_MST_/*$domainId*/
    SET
        UPD_FUNC = /*updFunc*/''
        ,UPD_DATETM = now()
        ,UPD_USER = /*updUser*/''

    	/*IF strData != null */
        ,STR_DATA = /*strData*/''
        /*END*/

        /*IF numData != null */
        ,NUM_DATA = /*numData*/0
        /*END*/

        /*IF fltData != null*/
        ,FLT_DATA = /*fltData*/0.0
        /*END*/
    WHERE
        TABLE_NAME = /*tableName*/
        AND COLUMN_NAME = /*columnName*/