-- メニューの調整
INSERT INTO MENU_MST_SALES VALUES ('1307','倉庫',NULL,'/master/searchWarehouse','0013','7','1','FFFFFF','339966','初期登録',now(),'ARK','初期登録',now(),'ARK',NULL,NULL,NULL);
INSERT INTO ROLE_CFG_SALES VALUES ('088','1307','1','初期登録',now(),'ARK','初期登録',now(),'ARK',NULL,NULL,NULL);
INSERT INTO ROLE_CFG_SALES VALUES ('089','1307','2','初期登録',now(),'ARK','初期登録',now(),'ARK',NULL,NULL,NULL);
UPDATE ROLE_MST_SALES SET NAME = '棚番（参照）' WHERE ROLE_ID = '017';
INSERT INTO ROLE_MST_SALES VALUES ('089','倉庫',NULL,'初期登録',now(),'ARK','初期登録',now(),'ARK',NULL,NULL,NULL);
INSERT INTO ROLE_MST_SALES VALUES ('088','倉庫（参照）',NULL,'初期登録',now(),'ARK','初期登録',now(),'ARK',NULL,NULL,NULL);


