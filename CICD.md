# CI / CD
여러 개발자의 코드가 실시간으로 병합, 테스트 수행, master 브랜치 push -> 자동 배포 가 요구됨   
*생산성을 위해*
## 1. CI & CD 란?
### CI (Continuous Integration - 지속적 통합)
> 코드 버전 관리를 하는 VCS 시스템(Git, SVN 등)에 PUSH -> 자동으로 테스트와 빌드가 수행   
안정적인 배포 파일을 만드는 과정
### CD (Continuous Deployment - 지속적 배포)
> 빌드 결과를 자동으로 운영 서버에 무중단 배포까지 진행되는 과정
 
### CI 에 대한 4가지 규칙
> 1. 모든 소스코드가 살아 있고(현재 실행되고) 누구든 현재의 소스에 접근할 수 있는 단일 지점을 유지할 것
> 2. 빌드 프로세스를 자동화해서 누구든 소스로부터 시스템을 빌드하는 단일 명령어를 사용할 수 있게 할 것
> 3. <strong>테스팅을 자동화</strong>해서 단일 명령어로 언제든지 시스템에 대한 건전한 테스트 수트를 실행할 수 있게할 것
> 4. 누구나 현재 실행 파일을 얻으면 지금까지 완전한 실행파일을 얻었다는 확신을 하게 할 것
>
*지속적으로 통합하기 위해서는 무엇보다 프로젝트가 완전한 상태임을 보장하기 위해 테스트 코드가 구현되어 있어야만 함*

## 2. Travis CI 연동
깃허브에서 제공하는 무료 CI 서비스   
*젠킨스는 설치형이기 때문에 이를 위한 EC2 인스턴스가 하나 더 필요함*
1. https://travis-ci.org/ > 깃허브 계정 로그인 > 계정명 > Settings > Repository 선택
2. travis plan 설정 및 이메일 인증 진행
3. CI를 진행할 .travis.yaml 생성, pull push -> travis 확인

## 3. Travis 와 AWS S3 연동
배포를 담당하는 CodeDeploy 에 저장기능이 없기 때문에 S3 를 통해 Jar 파일을 전달   
S3 는 일종의 파일서버임
1. aws IAM > 사용자 > 사용자 추가 > 사용자 명 입력 > 직접 정책 연결 > 태그 등록
    > 정책 이름   
    AmazoneS3FullAccess   
    AWSCodeDeployFullAccess
2. 액세스키 생성 및 등록
    > AWS 외부에서 실행되는 애플리케이션 설정으로 생성,   
    travis 에서 해당 repository settings 의 Environment Variables 에 기입하기(2가지 기입)   
    여기에 기입된 값은 .travis.yml 에서 $AWS_ACCESS_KEY, $AWS_SECRET_KEY 로 사용(둘 다 위에서 기입한 이름임)
3. S3 버킷 생성
    > 버킷 생성 후, Travis CI 에서 빌드하여 만든 Jar 파일을 S3 에 올릴 수 있게   
    .travis.yml 에 코드 추가   
    > >...   
   > before_deploy:   
   > -zip -r freelec-springboot2-webservice * # 현재 위치의 모든 파일을 압축(freelec-springboot2-webservice.zip)   
   > -mkdir -p deploy # deploy 라는 이름의 디렉토리 생성   
   > -mv freelec-springboot2-webservice.zip deploy/freelec-springboot2-webservice.zip # 파일 이동   
   >  S3, CodeDeploy 로 배포 등 외부 서비스와 연동될 행위 선언   
   > deploy:   
   > -provider: s3   
   > access_key_id: $AWS_ACCESS_KEY # Travis repo settings 에 설정된 값   
   > secret_access_key: $AWS_SECRET_KEY   
   > bucket: freelec-springboot-build-springbootaws # 버킷 명   
   > region: ap-northeast-2   
   > skip_cleanup: true   
   > acl: private # zip 파일 접근을 private 로   
   > local_dir: deploy # before_deploy 에서 생성한 디렉토리, 해당 위치의 파일들만 s3 로 전송   
   > wait-until-deploy: true   
   >
4. Travis CI 와 AWS S3, CodeDeploy 연동
CodeDeploy 는 AWS 의 배포 시스템,   
EC2 가 CodeDeploy 를 연동받을 수 있도록 IAM 역할 생성
   1. EC2 에 IAM 역할 추가하기
      > AWS 서비스 -> EC2 > 정책 선택 > AmazonEC2RoleForAWS-CodeDeploy   
      EC2 인스턴스 설정의 IAM 역할 연결/바꾸기를 통해 서비스 등록   
      EC2 에 접속, 명령어 입력하여 에이전트 설치(CodeDeploy 요청 받을 수 있게)
      > > aws s3 cp s3://aws-codedeploy-ap-northeast-2/latest/install . --region ap-northeast-2   
      >
      > chmod 로 install 파일 실행 권한 추가
      > > chmod +x ./install
      > 
      > install 파일 설치 진행 (sudo yum install ruby 를 통해 ruby 설치)
      > > sudo ./install auto
      > 
      > Agent 가 정상적으로 실행되고 있는지 확인
      > > sudo service codedeploy-agent status
      > 
   2. CodeDeploy 를 위한 권한 생성   
      CodeDeploy 에서 EC2 에 접근하기 위한 IAM 권한 생성
      > AWS 서비스 -> CodeDeploy
      > 
   3. CodeDeploy 생성
      AWS 에서 배포를 담당하는 3가지 서비스   
      - Code Commit
        > 깃허브와 같은 코드 저장소 역할   
        프라이빗 기능을 지원하는 강점(현재 깃허브에서 무료로 제공하기 때문에 굳이 CodeCommit 사용 X)   
      - Code Build
        > Travis CI 와 마찬가지로 빌드용 서비스   
        멀티 모듈을 배포해야 하는 경우 사용해 볼만하지만, 규모가 있는 서비스에서는 대부분 젠킨스/팀시티 등을 이용함(굳이 사용 X)
      - CodeDeploy
        > AWS 배포 서비스   
        대체재가 없음   
        오토스케일링 그룹 배포, 블루 그린 배포, 롤링 배포, EC2 단독 배포 등 많은 기능을 지원함   
        > 
      애플리케이션 생성 > 컴퓨팅 플랫폼 - EC2/온프레미스 > 배포그룹 생성 > 서비스 역할 선택 > 배포유형 - 현재위치   
      환경구성 > Amazon EC2 인스턴스, 해당 키, 값 선택   
      배포설정 > 배포구성 - CodeDeployDefault.AllAtOnce , 로드밸런싱 비활성화   
      *배포구성이란? 한 번 배포할 때 몇 대의 서버에 배포할지를 결정
   4. Travis CI, S3, CodeDeploy 연동
      1. S3 에서 넘겨줄 zip 파일이 담길 디렉토리 하나 생성
         > mkdir ~/app/step2 && mkdir ~/app/step2/zip
         >
         Travis CI 의 Build 가 끝나면 S3 에 zip 파일이 전송되고, 이 zip 파일은   
         /home/ec2-user/app/step2/zip 으로 복사되어 압축을 풀 예정임
         AWS CodeDeploy 의 설정은 appspec.yml 을 생성하여 진행
      2. appspec.yml 생성
         > version: 0.0 # CodeDeploy 버전 표기   
         os: linux   
         files:   
         -source: / # CodeDeploy 에서 전달해준 파일 중 destination 으로 이동시킬 대상을 지정, 루트경로지정 -> 전체 파일   
         destination: /home/ec2-user/app/step2/zip/ # source 에서 지정된 파일을 받을 위치, 이후 Jar 를 실행하는 등은 destination 에서 옮긴 파일들로 진행   
         overwrite: yes # 기존에 파일들이 있으면 덮어쓸지를 결정, yes -> 덮어씀   
         > 
      3. .travis.yml 에도 CodeDeploy 내용을 추가
         > -provider: codedeploy   
               access_key_id: $AWS_ACCESS_KEY # Travis repo settings 에 설정된 값   
               secret_access_key: $AWS_SECRET_KEY   
               bucket: freelec-springboot-build-springbootaws # 버킷 명   
               key: freelec-springboot2-webservice.zip # 빌드 파일을 압축해서 전달   
               bundle_type: zip # 압축 확장자   
               application: freelec-springboot2-webservice # CodeDeploy 애플리케이션 명   
               deployment_group: freelec-springboot2-webservice-group # CodeDeploy 배포 그룹 명   
               region: ap-northeast-2   
               wait-until-deployed: true   
         > 
      명령어를 통해 파일이 잘 도착했는지 확인
      > cd /home/ec2-user/app/step2/zip   
      ll
      > 
   5. 배포 자동화 구성
      Jar 배포부터 실행까지
      1. step2 환경에서 실행될 deploy.sh 파일 생성
         최상위 디렉토리에 scripts/deploy.sh 생성하여 스크립트 작성
         step1 에서 작성된 deploy.sh 와 큰 차이가 없음
         > #!/bin/bash
         > REPOSITORY=/home/ec2-user/app/step2   
         > PROJECT_NAME=SpringBootAWS   
         > echo "> Build 파일 복사"   
         > cp $REPOSITORY/zip/*.jar $REPOSITORY/   
         > echo "> 현재 구동 중인 애플리케이션 pid 확인"   
         > CURRENT_PID=$(pgrep -fl freelec-springboot2-webservice | grep jar | awk '{print $1}')   
         > #현재 실행 중인 스프링 부트 애플리케이션의 PID 를 찾아서 종료하기, 이름이 같을 수 있기 때문에, 실행중인 jar 프로세스 탐색   
         > echo "현재 구동 중인 애플리케이션 pid: $CURRENT_PID"
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
         #Jar 파일은 실행 권한이 없는 상태, nohup 으로 실행할 수 있게 실행 권한을 부여   
         echo "> $JAR_NAME 실행"   
         nohup java -jar \   
         -Dspring.config.location=classpath:/application.properties,classpath:/applicationn-real.properties,/home/ec2-user/app/application-oauth.properties,/home/ec2-user/app/application-real-db.properties \   
         -Dspring.properties.profiles.active=real \   
         $JAR_NAME > $REPOSITORY/nohup.out 2>&1 &   
         #nohup 실행 시, CodeDeploy 가 무한 대기하는 이슈(nohup 이 끝나기 전까지 CodeDeploy 도 끝나지 않음)를 해결하기 위해 nohup.out 파일을 표준 입출력용으로 별도로 사용함   
         #이렇게 하지 않으면 nohup.out 파일이 생기지 않고, codeDeploy 로그에 표준 입출력이 출력됨
      2. .travis.yml 수정
         현재, 프로젝트의 모든 파일을 zip 로 만드는 데 필요한 파일은 Jar, appspec.yml, 배포를 위한 스크립트임.   
         > -mkdir -p before-deploy # zip 에 포함시킬 파일들을 담을 디렉토리 생성, travis CI 는 S3 로 특정 파일만 업로드가 불가(디렉토리 단위만 업로드 가능하기에 디렉토리 항상 생성)   
         -cp scripts/*.sh before-deploy/ # before-deploy 에 zip 파일에 포함시킬 파일들을 저장   
         -cp appspec.yml before-deploy/   
         -cp build/libs/*.jar before-deploy/   
         -cp before-deploy && zip -r before-deploy * # before-deploy 로 이동 후 전체 압축, zip -r 명령어를 통해 before-deploy 디렉토리 전체 파일을 압축   
         -cd ../ && mkdir -p deploy # 상위 디렉토리로 이동 후 deploy 디렉토리 생성   
         -mv before-deploy/before-deploy.zip deploy/freelec-springboot2-webservice.zip # deploy 로 zip 파일 이동   
      3. appspec.yml 수정
         (location, timeout, runas 들여쓰기 주의)   
         > permissions:   
         -object: /   
         pattern: "**"   
         owner: ec2-user   
         group: ec2-user
         hooks:   
         ApplicationStart:   
         -location: deploy.sh   
         timeout: 60   
         runas: ec2-user   
      4. 배포 확인
         build.gradle 에서 프로젝트 버전 수정, index.mustache 내용 수정
      5. 로그 살펴보기
         > cd /opt/codedeploy-agent/deployment-root/   
         ll
         > > drwxr-xr-x 2 root root 247  4월 25 01:17 deployment-instructions   
         drwxr-xr-x 2 root root  46  4월 25 00:45 deployment-logs   
         -> CodeDeploy 로그 파일, CodeDeploy 로 이루어지는 표준 입출력 내용이 모두 여기에 담김, 작성한 echo 내용도 모두 표기   
         drwxr-xr-x 5 root root  63  4월 25 01:17 e0e729a1-554b-4ec3-9994-c5673d0ec397   
         -> 사용자 고유 ID, 배포한 단위별로 배포 파일들이 있음. 본인의 배포파일이 정상적으로 왔는지 확인 가능   
         drwxr-xr-x 2 root root   6  4월 25 01:17 ongoing-deployment   
## 이제 작업이 끝난 내용을 Master 브랜치에 푸쉬하면 자동으로 EC2에 배포가 됨
- 배포하는 동안 서비스를 사용할 수 없기 때문에 서비스 중단 없는 배포 방법이 요구됨
- 새로운 Jar 가 실행되기 전까진 기존 Jar 를 종료시켜 놓음
# 무중단 배포
- AWS Blue-Green 무중단 배포
- 도커를 이용한 웹서비스 무중단 배포
위의 방법을 포함한 다양한 방법이 존재
- 이 프로젝트에서는 엔진엑스를 이용
- Nginx : 웹 서버, 리버스 프록시, 캐싱, 로드 밸런싱, 미디어 스트리밍 등을 위한 오픈소스 소프트웨어   
*리버스 프록시는 외부의 요청을 받아 백엔드 서버로 요청을 전달하는 행위를 말함*
- 엔진엑스는 요청을 전달하고(리버스 프록시 서버), 실제 요청에 대한 처리는 뒷단의 웹 애플리케이션 서버들이 처리
- 가장 저렴한 Nginx, 개인 서버에서도 동일한 방식으로 구축 가능
- 하나의 EC2 혹은 리눅스 서버에 Nginx 1대와 SpringBoot Jar 를 2대를 사용하는 구조
   > 엔진엑스는 80(http), 443(https) 포트를 할당   
   > 스프링 부트1은 8081 포트로 실행   
   > 스프링 부트2는 8082 포트로 실행   
   > 1. 사용자가 서비스 주소로 접속(80 혹은 443 포트)
   > 2. 엔진엑스는 사용자의 요청을 받아 현재 연결된 스프링 부트1로 요청을 전달
   > 3. 다른 스프링 부트2는 엔진엑스와 연결된 상태가 아니므로 요청받지 못함   
   > *신규 배포가 필요하면, 연결되지 않은 스프링 부트2로 배포*
   > 4. 배포하는 동안에도 서비스(스프링 부트1)가 중단되지 않음
   > 5. 배포가 끝난 후 정상적으로 스프링 부트2가 구동중인지 확인
   > 6. 스프링 부트2가 정상 구동 중이면 nginx reload 명령어를 통해 8081 대신 8082를 바라보도록 함
   > 7. nginx reload 는 0.1초 이내에 완료
## 1. 엔진엑스 설치와 스프링 부트 연동
1. EC2 에서 엔진엑스 설치 및 보안 그룹 추가
   > 설치   
   > sudo yum install nginx   
   > 오류 시 아마존에서 지원하는 nginx 설치(sudo amazon-linux-extras install nginx1)   
   > 구동 확인   
   > sudo service nginx start   
   > 보안 그룹   
   > EC2 > ec2 보안그룹 편집 > 인바운드 규칙 편집 > 80 추가 (IP4, IP6)가
2. 구글 및 네이버에서 리다이렉션 주소 추가(Nginx -> 80)
   > 구글   
   > 사용자 인증 정보 > OAuth 클라이언트 수정 > 승인된 리디렉션 URI 추가   
   > http://ec2-43-200-89-72.ap-northeast-2.compute.amazonaws.com/login/oauth2/code/google   
   > 네이버
   > API 설정 > Callback URL 에 추가   
   > http://ec2-43-200-89-72.ap-northeast-2.compute.amazonaws.com/login/oauth2/code/naver
   > 
3. 엔진엑스와 스프링 부트 연동   
   엔진엑스가 현재 구동 중인 스프링 부트 프로젝트를 바라볼 수 있게 프록시 설정
   > sudo vim /etc/nginx/nginx.conf > server{...include 아래에 추가   
   >location / {   
   proxy_pass http://localhost:8080; # 엔진엑스로 요청이 오면 http://localhost:8080으로 전달   
   proxy_set_header X-Rreal-IP $remote_addr;   
   proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for; # 실제 요청 데이터를 header 의 각 항목에 할당   
   proxy_set_header Host $http_host;   
   }   
   -> proxy_set_header X-Real_IP $remote_addr: Request Header 의 X-Real-IP 에 사용자의 IP 를 저장
   >
   엔진엑스 재시작
   > sudo service nginx restart
   > 
4. 무중단 배포 스크립트 만들기   
먼저, 배포 시 8081을 쓸지, 8082를 쓸지 판단하는 기준이 될 API 추가
   > ProfileController 추가 및 ProfileControllerUnitTest 작성(스프링 환경이 필요하지 않으므로 @SpringBootTest 없이 작성)   
   > 스프링 시큐리티 설정을 포함한 ProfileControllerTest 추가(스프링 환경이 필요하므로 @SpringBootTest 사용)