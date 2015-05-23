killme=`cat logs/echoBridge.pid`
kill $killme
ts=`date`
echo "Stopped $ts" >> logs/echoBridgeStartStopLog.log
