# AWS(Amazon Web Service)
> ### 서버를 24시간 작동하기 위한 선택지
> 1. 서버전용 컴퓨터 24시간 구동
> 2. 호스팅 서비스(Cafe24, 코리아호스팅)
> 3. 클라우드 서비스(AWS, AZURE, GCP)
## 클라우드 서비스
> ### 형태
> 1. Iaas(Infrastructure as a Service)
>    1. 기존 물리 장비를 미들웨어와 함께 묶어둔 추상화 서비스
>    2. 가상머신, 스토리지, 네트워크, 운영체제 등의 IT 인프라를 대여
>    3. AWS(EC2, S3) 등
> 2. Paas(Platform as a Service)
>    1. Iaas 에서 한 번 더 추상화한 서비스
>    2. 많은 기능 자동화되어 있음
>    3. AWS(Beanstalk), Heroku 등
> 3. Saas(Software as a Service)
>    1. 소프트웨어 서비스
>    2. 구글 드라이브, 드랍박스, 와탭 등
### AWS
프리티어(1년간 대부분 서비스가 무료)   
지원 기능이 많음(소규모 개발 시 편리함)   
높은 국내 점유율(2019~2022, 3년간 70% 내외, 2.MS 3.네이버 순, 퍼블릭 클라우드 위주) 공정거래위원회. (2022). *클라우드 서비스 분야 실태조사 연구*

> PaaS 의 빈스톡 사용 시, 작업이 간소화되지만, 프리티어는 무중단 배포가 불가능함   
> Iaas 사용

### EC2 서버 접속
> 1. EC2 인스턴스 생성
> 2. 태그 추가(pem 은 마스터 키 이므로 잘 관리하자)
> 3. 보안그룹 구성(SSH-내 IP, 사용자 지정 TCP- 8080 ::/0, HTTPS- ::/0)
> 4. 탄력적 IP(AWS 의 고정 IP) 할당 -> 인스턴스를 중지하고 다시 시작할 때 새로운 IP 가 할당 되므로 접근할 때마다 IP 주소를 확인해야함->고정 IP 설정   
> 주소와 인스턴스 연결(바로 연결하지 않으면 과금됨)
> 5. Mac & Linux - 터미널
>    1. cp ~/Downloads/이름.pem ~/.ssh/ 입력하여 pem 키를 복사함 (pem 을 Downloads 디렉토리에 저장했다는 가정, cd ~/shh 로 확인)
>    2. chmod 600 ~/.ssh/pem 키 이름.pem
>    3. vim ~/.ssh/config   
>       Host 서비스이름   
>       HostName 탄력적 IP 주소
>       User ec2-user
>       IdentityFile ~/.ssh/pem 키 이름.pem
>       :wq 로 저장 및 종료
>    4. chmod 700 ~/.ssh/config -> config 파일 권한 설정
>    5. ssh 서비스이름 -> yes 를 통해 EC2 접속
> 6. Windows - putty
>    1. putty.exe, puttygen.exe 설치, puttygen.exe 실행
>    2. Conversions>Import key>pem 키 이름.pem 을 통해 pem 키를 ppk 파일로 변환(putty 는 pem 사용 불가하기 때문)
>    3. Save private key 로 저장
>    4. putty.exe>   
>    Host Name>ec2-user@탄력적 IP 주소   
>    Port>22 (ssh 접속 포트임)   
>    Connection type> SSH
>    5. Connection>SSH>Auth>private key file authentication Browse...>ppk 파일
>    6. Session>Saved Sessions 에 현재 설정 저장, Save
>    7. Open
> 7. Java 설치(필요한 버전), 타임존 변경(한국 시간대로), 호스트네임 변경(IP 만으로 서비스 용도를 파악하기 힘들기 때문)
>    > sudo yum install -y java-1.8.0-openjdk-devel.x86_64   
       sudo /usr/sbin/alternatives --config java (해당하는 버전의 인덱스 입력 후 엔터)   
       sudo rm /etc/localtime   
       sudo ln -sf /usr/share/zoneinfo/Asia/Seoul /etc/localtime   
       date 로 시간확인   
>    sudo -s root 권한 사용   
     hostname 으로 현재 hostname 확인   
     cat /etc/hostname 으로 파일 안의 내용 출력   
     hostnamectl set-hostname 이름   
     sudo reboot 로 서버 재부팅   
     ssh 서비스 이름 으로 접근, curl 이름 명령어로 등록 확인 (잘 되었으면 80 포트로 접근 안된다고 에러 발생)
### AWS RDS 를 통해 AWS 에 데이터베이스 환경 구축
DB를 직접 설치하지 않음   
비용만 지불하면 많은 양의 데이터 처리 가능   
> 1. RDS 인스턴스 생성 (퍼블릭 엑세스 가능)
> 2. 운영환경에 맞는 파라미터 설정
>   > 1. 파라미터 그룹 생성
>   > 2. 파라미터 편집   
>   > time_zone - Asia/Seoul   
>   > character_set - utf8mb4
>   > collation - utf8mb4_general_ci
>   > max_connections - 150 (프리티어의 경우 약60개로 자동 설정 )
>   > 데이터베이스, 체크 후 수정, DB 파라미터 그룹 적용 후 저장, 즉시 적용, 작업>재부팅(적용이 안 되면)
>   > 인바운드 규칙에 MYSQL/Aurora 내 IP, EC2 추가
> 3. IntelliJ Database 에 MariaDB 추가(엔드포인트 입력)
> 4. MySQLCLI 설치 - 명령어 라인을 쓰기 위함임
>   > sudo yum install mysql
     mysql -u 계정 -p -h Host주소(엔드포인트임)
     터미널에서 show databases 를 통해 확인
### EC2 서버에 프로젝트 배포
1. EC2에 프로젝트 Clone 받기
   > sudo yum install git (EC2에 깃을 설치)   
   > git --version 으로 확인   
   > mkdir ~/app && mkdir ~/app/step1 (git clone 으로 프로젝트를 저장할 디렉토리를 생성)   
   > cd ~/app/step1 (생성된 디렉토리로 이동)   
   > 깃허브 웹페이지에서 https 주소 복사   
   > git clone 복사한 주소   
   > cd SpringBootAWS   
   > ll   
   > ./gradlew test 로 테스트   
   > 테스트가 실패해서 수정하고 깃허브에 푸시했다면 git pull 입력   
   > Permission denied 실행권한이 없다는 메시지가 뜨면, chmod +x ./gradlew
2. 배포 스크립트 만들기   
   배포란 작성한 코드를 실제 서버에 반영하는 것을 말한다
   >    git clone 혹은 git pull 을 통해 새 버전의 프로젝트 받음   
   >    Gradle 이나 Maven 을 통해 프로젝트 테스트와 빌드   
   >    EC2 서버에서 해당 프로젝트 실행 및 재실행
   >
       위 과정을 배포할 때마다 개발자가 하나하나 명령어를 실행하는 것은 비효율적임   
       따라서, 쉘 스크립트로 작성해 스크립트만 실행   
       *쉘 스크립트는 .sh 라는 파일 확장자를 가진 파일, 리눅스에서 기본적으로 사용할 수 있는 스크립트 파일의 한 종류*
       *빔은 리눅스환경과 같이 GUI 가 아닌 환경에서 사용할 수 있는 편집도구, 대중적임*
   >    vim ~/app/step1/deploy.sh
   > >   #!/bin/bash   
   >    REPOSITORY=/home/ec2-user/app/step1   
   >    *//프로젝트 디렉토리 주소는 스크립트 내에서 자주 사용하는 값이기 때문에 이를 변수로 저장*   
   >    *//프로젝트 이름도 마찬가지임*   
   >    *//쉘에서는 타입 없이 선언하여 저장*   
   >    *//쉘에서는 $변수명 으로 변수를 사용할 수 있음*   
   >    PROJECT_NAME=SpringBootAWS   
   >    cd $REPOSITORY/$PROJECT_NAME/   
   >    *//제일 처음 git clone 을 받았던 디렉토리로 이동*   
   >    *//바로 위의 쉘 변수 설명을 따라 /home/ec2-user/app/step1/SpringBootAWS 주소로 이동*   
   >    echo ">Git Pull" *//디렉토리 이동 후, master 브랜치의 최신 내용을 받음*   
   >    git pull   
   >    echo "> 프로젝트 Build 시작"   
   >    ./gradlew build *//프로젝트 내부의 gradlew 로 build 를 수행*   
   >    echo "> step1 디렉토리로 이동"   
   >    cd $REPOSITORY
   >    echo "> Build 파일 복사"   
   >    cp $REPOSITORY/$PROJECT_NAME/build/libs/*.jar $REPOSITORY/ *//build 의 결과물인 jar 파일을 복사, jar 를 모아둔 위치로 복사*   
   >    echo "> 현재 구동중인 애플리케이션 pid 확인"   
   >    CURRENT_PID=$(pgrep -f ${PROJECT_NAME}.*.jar)   
   >    *//기존에 수행 중이던 스프링 부트 애플리케이션을 종료*   
   >    *//pgrep 은 process id 만 추출하는 명령어임*   
   >    *//-f 옵션은 프로세스 이름으로 찾음*   
   >    echo "현재 구동중인 애플리케이션 pid: $CURRENT_PID"
   >    if [ -z "$CURRENT_PID ]; then   
   >    *//현재 구동중인 프로세스가 있는지 없는지를 판단해서 기능을 수행*   
   >    *//process id 값을 보고 프로세스가 있으면 해당 프로세스를 종료*   
   >    echo "> 현재 구동중인 애플리케이션이 없으므로 종료하지 않습니다"   
   >    else   
   >    echo "> kill -15 $CURRENT_PID"   
   >    kill -15 $CURRENT_PID   
   >    sleep 5   
   >    fi   
   >    echo "> 새 애플리케이션 배포"   
   >    JAR_NAME=$(ls -tr $REPOSITORY/ | grep jar | tail -n 1)   
   >    *//새로 실행할 jar 파일명을 찾음*   
   >    *//여러 jar 파일이 생기기 때문에 tail -n 로 가장 나중의 jar 파일(최신 파일)을 변수에 저장*   
   >    echo "> JAR Name: $JAR_NAME"
   >    nohup java -jar $REPOSITORY/$JAR_NAME 2>&1 &   
   >    *//찾은 jar 파일명으로 해당 jar 파일을 nohup 으로 실행*   
   >    *//스프링 부트의 장점으로 특별히 외장 톰캑을 설치할 필요가 없음*   
   >    *//내장 톰캣을 사용해서 jar 파일만 있으면 바로 웹 애플리케이션 서버를 실행할 수 있음*   
   >    *//일반적으로 자바를 실행할 때는 java -jar 라는 명령어를 사용, 이렇게 하면 사용자가 터미널 접속을 끊을 때 애플리케이션도 같이 종료*   
   >    *//애플리케이션 실행자가 터미널을 종료해도 애플리케이션은 계속 구동될 수 있도록 nohup 명령어를 사용*
   >
   > chmod +x ./deploy.sh 스크립트에 실행 권한 추가   
   > ll 로 확인   
   > ./deploy.sh 로 실행   
   > vim nohup.out 파일을 열어 로그 확인
3. 외부 Security 파일 등록   
   ClientRegistrationRepository 를 생성할 때 필요한 clientId, clientSecret 가 application-oauth.properties 에 있음   
   위 파일은 .gitignore 에서 git 제외 대상으로 설정하여 깃허브에 올라가 있지 않음   
   서버에서 직접 이 설정을 가지고 있게 할 필요가 있음   
   *//Travis CI는 비공개 저장소를 사용할 경우 비용이 부과됨*
   > 1. properties 파일 생성   
        vim /home/ec2-user/app/application-oauth.properties   
        local 에 있는 내용을 복사함
   > 2. deploy.sh 수정
        nohup. java -jar \
        -Dspring.config.location=classpath:/application.properties,/home/ec2-user/app/application-oauth.properties \
        $REPOSITORY/$JAR_NAME 2<&1 &   
        *-Dspring.config.location 는 스프링 설정 파일 위치를 지정, classpath 가 붙으면 jar 안에 있는 resources 디렉토리를 기준으로 경로가 생성, application-oauth.properties 는 외부에 파일이 있기 때문에 절대경로를 사용함*
4. 스프링 부트 프로젝트로 RDS 접근
   1. 테이블 생성
      > H2 에서 자동 생성해주던 테이블들을 MariaDB 에선 직접 쿼리를 이용해 생성   
      스프링 세션 테이블을 복사 - Command Shift O / Ctrl Shift N > schema-mysql.sql
   2. 프로젝트 설정
      > 자바 프로젝트가 MariaDB에 접근하려면 데이터베이스 드라이버가 필요함.   
      MariaDB 에서 사용 가능한 드라이버를 프로젝트에 추가   
      gradle.build 에 MariaDB Driver 의존성 등록   
      > > implementation('org.mariadb.jdbc:mariadb-java-client:버전')
      > 
      > src/main/resources 에 application-real.properties 생성   
      > > spring.profiles.include=oauth, real-db   
      spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL5InnoDBDialect   
      spring.session.store-type=jdbc
      
   3. EC2(리눅스 서버) 설정
      > 데이터베이스의 접속 정보는 중요하게 보호해야 할 정보임.   
      공개되면 외부에서 데이터를 모두 가져갈 수 있기 때문.   
      프로젝트 안에 접속 정보를 갖고 있다면 깃허브와 같이 오픈된 공간에선 누구나 해킹할 위험이 있음.   
      EC2 서버 내부에서 접속 정보를 관리하도록 설정
      > vim ~/app/application-real-db.properties 으로 설정파일 생성
      > >spring.jpa.hibernate.ddl-auto=none   
      > *JPA로 테이블이 자동 생성되는 옵션을 지정*   
      > *RDS 에는 실제 운영으로 사용될 테이블이니 절대 스프링 부트에서 새로 만들지 않도록 해야함*   
      > *이 옵션을 선택하지 않으면 테이블이 모두 새로 생성될 수 있으니 주의!*   
      > spring.datasource.url=jdbc:mariadb: //rds 주소:포트명(기본은3306)   
      > database 이름   
      > spring.datasource.username=db 계정   
      > spring.datasource.password=db 계정 비밀번호   
      > spring.datasource.driver-class-name=org.mariadb.jdbc.Driver
      > deploy.sh 수정 - (real profile 쓸 수 있게)> > nohup java -jar \   
      > -Dspring.config.location=classpath:/application.properties,/home/ec2-user/app/application-oauth.properties,/home/ec2-user/app/application-real-db.properties,classpath:/application-real.properties \   
      -Dspring.profiles.active=real \   
      $REPOSITORY/$JAR_NAME 2>&1 &   
      *-Dspring.profiles.active=real 은 application-real.properties 를 활성화시킴*   
      *application-real.properties 의 spring.profiles.include=oauth,real-db 옵션 때문에 real-db 역시 함께 활성화 대상에 포함됨*
      >
      > nohup.out 파일에 다음과 같은 로그가 뜨면 성공임
      > > Tomcat started on port(s): 8080 (http) with context path ''   
      > Started Application in ~~ seconds (JVM running for ~~~)
      >
      > curl 명령어로 html 코드가 정상적으로 보이면 성공임
      > > curl localhost:8080
   