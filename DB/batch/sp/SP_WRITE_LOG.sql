DELIMITER //

DROP PROCEDURE IF EXISTS SP_WRITE_LOG
//

CREATE PROCEDURE SP_WRITE_LOG (
    domainId VARCHAR(5),
    funcName VARCHAR(255),
    statusCode INT,
    discription VARCHAR(120))
BEGIN

    -- バッチログ挿入
    INSERT INTO BATCH_LOG (
        DOMAIN_ID, 
        FUNC_NAME, 
        DATETM, 
        STATUS_CODE, 
        DISCRIPTION
    ) VALUES (
        domainId,
        funcName,
        now(),
        statusCode,
        discription
    );
    
END
//

DELIMITER ;
