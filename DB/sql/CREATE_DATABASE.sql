-- データベース作成
CREATE DATABASE salescube DEFAULT CHARACTER SET utf8;
USE salescube;

GRANT ALL PRIVILEGES ON salescube.* TO salescube@"%" IDENTIFIED BY 'salescube';
GRANT ALL PRIVILEGES ON salescube.* TO salescube@localhost IDENTIFIED BY 'salescube';
FLUSH PRIVILEGES;

-- テーブル作成
SOURCE ./createtable/GET_NEXT_VAL_HIST.sql
SOURCE ./createtable/CREATE.sql

-- マスタデータ挿入
SOURCE ./insertmaster/BANK_MST.sql
SOURCE ./insertmaster/CATEGORY_MST.sql
SOURCE ./insertmaster/CATEGORY_TRN.sql
SOURCE ./insertmaster/DETAIL_DISP_ITEM_MST.sql
SOURCE ./insertmaster/DOMAIN_MST.sql
SOURCE ./insertmaster/GRANT_ROLE.sql
SOURCE ./insertmaster/INIT_MST.sql
SOURCE ./insertmaster/MENU_MST.sql
SOURCE ./insertmaster/MINE_MST.sql
SOURCE ./insertmaster/PRODUCT_MST.sql
SOURCE ./insertmaster/RATE_MST.sql
SOURCE ./insertmaster/RATE_TRN.sql
SOURCE ./insertmaster/REPORT_TEMPLATE_MST.sql
SOURCE ./insertmaster/ROLE_CFG.sql
SOURCE ./insertmaster/ROLE_MST.sql
SOURCE ./insertmaster/SEQ_MAKER.sql
SOURCE ./insertmaster/TAX_RATE_MST.sql
SOURCE ./insertmaster/USER_MST.sql
