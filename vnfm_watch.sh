#!/bin/bash
#
# watchdog
#
# Run as a cron job to keep an eye on what_to_monitor which should always
# be running. Restart what_to_monitor and send notification as needed.
#
# This needs to be run as root or a user that can start system services.
#
# Revisions: 0.1 (20100506), 0.2 (20100507)
DMNAME="c-vnfm-1.0.0"
DAEMON="$DMNAME".jar
DARG="--spring.config.location=file:///home/vnfm/cfg/application.properties"
DMLOG="/home/vnfm/log/vnfm_$(date +%Y%m%d).log" 
LOGNAME="/home/vnfm/log/vnfm_watch_$(date +%Y%m%d).log"
GREP=/bin/grep
PS=/bin/ps
NETSTAT=/bin/netstat
NOP=/bin/true
DATE=/bin/date
TRUE=1
FALSE=0
ALIVECHECK=$FALSE
RESULT=0
LASTSEC=0
DB_CHECK_AGAIN=0
DBSERVER_CHECK=1
DBSERVER=3306
DB_RETRY_MAX=1
DB_RETRY_COUNT=0
ARG=$1
APIPORT=$2
MYHOST=`hostname`
CLUSTER_STAT="-"
VNFM_STOP=0

# LOG_LEV : DEBUG | INFO
LOG_LEV="INFO"

echo " ">> $LOGNAME
echo " ">> $LOGNAME
echo " ">> $LOGNAME
echo " ">> $LOGNAME
echo "`date +'%Y-%m-%d %H:%M:%S.%3N'` > ($$)======================================================" >> $LOGNAME
echo "`date +'%Y-%m-%d %H:%M:%S.%3N'` > ($$)#                                                    #" >> $LOGNAME
echo "`date +'%Y-%m-%d %H:%M:%S.%3N'` > ($$)#                RUNNING VNFM_WATCH                  #" >> $LOGNAME
echo "`date +'%Y-%m-%d %H:%M:%S.%3N'` > ($$)#                                                    #" >> $LOGNAME
echo "`date +'%Y-%m-%d %H:%M:%S.%3N'` > ($$)======================================================" >> $LOGNAME
echo "argument count : $#"

if [ $# -eq 0 ] ; then
	ARG=3
	APIPORT=8080
elif [ $# -ne 2 ] ; then
	echo "invalid argument count : $#"
	exit
fi

function check_ps()
{
	$PS -ef|$GREP -v grep| $GREP $DAEMON  >/dev/null 2>&1
	RESULT=$?
    case "$RESULT" in
        0)
        # Daemon is running in this case so we do nothing.
        echo "`date +'%Y-%m-%d %H:%M:%S.%3N'` > $1 is running now"
        $NOP
        ;;
            1)
            echo "`date +'%Y-%m-%d %H:%M:%S.%3N'` > $1 is NOT RUNNING."
        ;;
    esac
    return $RESULT
	
}

function check_netstat()
{
	if [ $1 = "DBSERVER" ] ; then 
		echo "$NETSTAT -an | $GREP LISTEN | $GREP $DBSERVER >/dev/null 2>&1"
		$NETSTAT -an | $GREP LISTEN | $GREP $DBSERVER >/dev/null 2>&1
#	elif [ $1 = "APISERVER" ] ; then 
#		echo "$NETSTAT -an | $GREP $APIPORT >/dev/null 2>&1"
#		$NETSTAT -an | $GREP $APIPORT >/dev/null 2>&1
	fi
	RESULT=$?
    case "$RESULT" in
        0)
        # Daemon is running in this case so we do nothing.
        echo "`date +'%Y-%m-%d %H:%M:%S.%3N'` > $1 is running now" >> $LOGNAME
		DB_RETRY_COUNT=0
        $NOP
        ;;
            1)
            echo "`date +'%Y-%m-%d %H:%M:%S.%3N'` > $1 is NOT RUNNING." >> $LOGNAME

			if [ $DB_RETRY_COUNT -lt  $DB_RETRY_MAX ] ; then
				# DB Server Start
				systemctl start mysql 
				DB_RETRY_COUNT=$(($DB_RETRY_COUNT+1))
				echo "systemctl start mysql (count:$DB_RETRY_COUNT)" >> $LOGNAME
			else 
				# if Active then SwitchOver 
				if [ $2 == "Active" ] ; then
					# todo switch over command
					#crm node change
					 echo "crm node change" >> $LOGNAME
					crm node standby
					crm node online c-vnfm2 
				fi
			fi
        ;;
    esac
    return $RESULT

}

function kill_proc()
{
        ps -ef | grep $1 | grep -v grep | awk '{print $2}' | xargs kill -9 > /dev/null 2>&1
        echo "`date +'%Y-%m-%d %H:%M:%S.%3N'` > SUCCESS : stopped <$1>!"
}

function stop_all()
{
        check_ps "$DAEMON"
        RETURN1=$?

        if [ $RETURN1 -eq 1 ] ; then
                echo "`date +'%Y-%m-%d %H:%M:%S.%3N'` > The service is not running." >> $LOGNAME
                return 1 
        fi

        kill_proc $DAEMON

        echo "`date +'%Y-%m-%d %H:%M:%S.%3N'` > The service has stopped." >> $LOGNAME
	
	return 1 
}

while :
do
	CURRMIN=`$DATE +%M`>/dev/null 2>&1
	CURRSEC=`$DATE +%s`>/dev/null 2>&1
	if [ $CURRSEC -gt $LASTSEC ] ; then 
		#echo " ARG: $ARG "
		MOD=$((CURRSEC%ARG))
		#echo `$DATE +%S` second : "$CURRMIN, $ALIVECHECK"
		if [ $MOD -eq 0 ] ; then
			ACTHOST=`crm status | grep "Started" | head -n 1 | awk {'print $4'}`
			check_netstat "DBSERVER" $CLUSTER_STAT
			if [ $MYHOST = $ACTHOST ] ; then
				CLUSTER_STAT="Active"
				if [ $LOG_LEV = "DEBUG" ]; then
					echo "`date +'%Y-%m-%d %H:%M:%S.%3N'` > ($$)[CLUSTER  ] Myhost is Active!! " >> $LOGNAME			
				fi

				check_ps "$DAEMON"
				RETURN=$?
				if [ $RETURN -eq 1 ]; then
					echo "`date +'%Y-%m-%d %H:%M:%S.%3N'` > ($$)[CVNFM    ] not running. Try to restart.. " >> $LOGNAME
###  ignoring input and redirecting stderr to stdout, only log file
					nohup java -jar /home/vnfm/bin/$DAEMON $DARG > /dev/null 2>&1 &  
					check_ps "$DAEMON"
					if [ $? -eq 0 ]; then 
						echo "`date +'%Y-%m-%d %H:%M:%S.%3N'` > ($$)[CVNFM    ] Successfully Running ! " >> $LOGNAME
					else 
						echo "`date +'%Y-%m-%d %H:%M:%S.%3N'` > ($$)[CVNFM    ] Restart Failed " >> $LOGNAME
					fi
				fi

## end of Daemon alive check ###

				VNFM_STOP=0
			else
				CLUSTER_STAT="Standby"
				if [ $LOG_LEV = "DEBUG" ]; then
					echo "`date +'%Y-%m-%d %H:%M:%S.%3N'` > ($$)[CLUSTER  ] Myhost is Standby !! " >> $LOGNAME			
				fi
				if [ $VNFM_STOP -eq 0 ] ; then
					echo "`date +'%Y-%m-%d %H:%M:%S.%3N'` > ($$)[CLUSTER  ] Service is Stop !! VNFM_STOP[$VNFM_STOP]" >> $LOGNAME			
					stop_all	
					if [ $? -eq 1 ]; then
						echo "`date +'%Y-%m-%d %H:%M:%S.%3N'` > ($$)[CLUSTER  ] The service has stopped." >> $LOGNAME
						VNFM_STOP=1
					fi
				fi
			fi
		fi
		if [ $CURRMIN -eq 00 ]; then
			if [ $ALIVECHECK -eq $FALSE ]; then 
				echo "`date +'%Y-%m-%d %H:%M:%S.%3N'` > ($$)I'm Alive. Myhost is $CLUSTER_STAT." >> $LOGNAME
			fi
			ALIVECHECK=$TRUE;
		else ALIVECHECK=$FALSE;
		fi
		LASTSEC=$CURRSEC
	fi
	sleep 0.1
done
exit
