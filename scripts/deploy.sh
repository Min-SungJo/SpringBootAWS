#!/bin/bash

REPOSITORY=/home/ec2-user/app/step2
PROJECT_NAME=SpringBootAWS

echo "> Build 파일 복사"
cp $REPOSITORY/zip/*.jar $REPOSITORY/

echo "> 현재 구동 중인 애플리케이션 pid 확인"

CURRENT_PID=$(pgrep -fl freelec-springboot2-webservice | grep jar | awk '{print $1}')
# 현재 실행 중인 스프링 부트 애플리케이션의 PID 를 찾아서 종료하기, 이름이 같을 수 있기 때문에, 실행중인 jar 프로세스 탐색

echo "현재 구동 중인 애플리케이션 pid: $CURRENT_PID"

if [ -z "$CURRENT_PID" ]; then
  echo "> 현재 구동 중인 애플리케이션이 없으므로 종료하지 않습니다."
else
  echo "> kill -15 $CURRENT_PID"
  kill -15 $CURRENT_PID
  sleep 5
fi

echo "> 새 애플리케이션 배포"

JAR_NAME=$(ls -tr $REPOSITORY/*.jar | tail -n 1)

echo "> JAR Name: $JAR_NAME"

echo "> $JAR_NAME 에 실행권한 추가"

chmod +x $JAR_NAME
# Jar 파일은 실행 권한이 없는 상태, nohup 으로 실행할 수 있게 실행 권한을 부여

echo "> $JAR_NAME 실행"

nohup java -jar \
  -Dspring.config.location=classpath:/application.properties,classpath:/applicationn-real.properties,/home/ec2-user/app/application-oauth.properties,/home/ec2-user/app/application-real-db.properties \
  -Dspring.properties.profiles.active=real \
  $JAR_NAME > $REPOSITORY/nohup.out 2>&1 &
# nohup 실행 시, CodeDeploy 가 무한 대기하는 이슈(nohup 이 끝나기 전까지 CodeDeploy 도 끝나지 않음)를 해결하기 위해 nohup.out 파일을 표준 입출력용으로 별도로 사용함
# 이렇게 하지 않으면 nohup.out 파일이 생기지 않고, codeDeploy 로그에 표준 입출력이 출력됨