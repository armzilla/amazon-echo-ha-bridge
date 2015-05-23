if test -f logs/echoBridge.pid
then
    killme=`cat logs/echoBridge.pid`
    sudo kill $killme
    ts=`date`
    echo "Stopped $ts" >> logs/echoBridgeStartStopLog.log
    sudo rm logs/echoBridge.pid
else
    echo "Could not stop process, pid file not found"
fi
