#!/bin/sh

if [ $# -ne 1 ]; then
	echo "引数に [実行モード] を指定してください。"
	exit 1
fi

# シェル配置ディレクトリ
ROOT_DIR="$(cd $(dirname $0);pwd)"

# ログファイルパス
LOG="$ROOT_DIR/salescube.log"

function install() {

	echo "##### $(date +"%y/%m/%d %k:%M:%S") SalesCube DB Install Start #####" > $LOG

	echo "データベースは作成済みですか？(y/N)"
	read _CREATEDDB

	if [ $_CREATEDDB != "y" -a $_CREATEDDB != "Y" ]; then
		echo "*** MySQL Database Create Start ***" >> $LOG
		echo "データベースを作成します。MySQLのrootユーザパスワードを入力してください。"
		cd $ROOT_DIR/sql
		mysql -u root -p < CREATE_DATABASE.sql >> $LOG 2>&1
		echo "*** MySQL Database Create End ***" >> $LOG
	fi

	echo "*** SalesCube Batch Entry Start ***" >> $LOG
	echo "定期実行バッチを配置します。MySQLのrootユーザパスワードを入力してください。"
	cd $ROOT_DIR/batch
	mysql -u root -p salescube < ENTRY_PROCEDURE.sql >> $LOG 2>&1
	cp -r ./salescube_batch /usr/local/bin/
	chmod u+x /usr/local/bin/salescube_batch/*.sh
	echo "*** SalesCube Batch Entry End ***" >> $LOG

	echo "##### $(date +"%y/%m/%d %k:%M:%S") SalesCube DB Install End #####" >> $LOG
}


case "$1" in
	install)
		install
		;;
	*)
	echo "オプションを指定してください。[install]"
	exit 1
esac
