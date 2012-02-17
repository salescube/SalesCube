#!/bin/sh

# 各処理用シェル呼び出し

DOMAIN=SALES

LOG_FILE=/home/$DOMAIN/$BATCH_NAME.log

DATE_TIME=`date '+%Y/%m/%d %H:%M:%S'`
echo "$DATE_TIME $BATCH_NAME BEGIN;"  > $LOG_FILE

cat $BATCH_NAME.sql | sed -e s/XXXXX/$DOMAIN/ | /usr/bin/mysql salesweb -u salesweb --password=salesweb >> $LOG_FILE 2>&1
RET=$?
if [ $RET -ne 0 ]
then
	echo FAILURE
fi

DATE_TIME=`date '+%Y/%m/%d %H:%M:%S'`
echo "$DATE_TIME $BATCH_NAME END (exit status=$RET);" >> $LOG_FILE

exit $RET
