#!/usr/bin/env bash

ABSPATH=$(readlink -f $0) # 현재 stop.sh 가 속해 있는 경로를 찾음, 하단의 코드와 같이 profile.sh 의 경로를 찾기 위해 사용됨
ABSDIR=$(dirname $ABSPATH) # 자바로 보면 일종의 import 구문임, 해당 코드로 인해 stop.sh 에서도 여러 function 을 사용할 수 있게 됨
source ${ABSDIR}/profile.sh

IDLE_PORT=$(find_idle_port)

echo "> $IDLE_PORT 에서 구동중인 애플리케이션 pid 확인"
IDLE_PID=$(lsof -ti tcp:${IDLE_PORT})

if [ -z ${IDLE_PID} ]
then
  echo "> 현재 구동중인 애플리케이션이 없으므로 종료하지 않습니다."
else
  echo "> kill -15 $IDLE_PID"
  kill -15 ${IDLE_PID}
  sleep 5
fi