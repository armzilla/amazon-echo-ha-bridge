#!/bin/bash
if [ -f logs/echoBridge.pid ]
then
    statme=`cat logs/echoBridge.pid`
    running=`ps -p $statme | grep -v "TIME"`
    if [ "${running}" == "" ]
    then
        echo Not Running
    else
        echo Running PID=${statme}
    fi
else
    echo Not Running
fi
