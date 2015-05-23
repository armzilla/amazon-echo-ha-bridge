#!/bin/bash
statme=`cat logs/echoBridge.pid`
running=`ps -p $statme | grep -v "TIME"`
if [ "${running}" == "" ]
then
    echo Not Running
else
    echo Running PID=${statme}
fi

