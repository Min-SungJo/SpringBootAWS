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
     mysql -u 계정 -p -h -Host주소(엔드포인트임)
     터미널에서 show databases 를 통해 확인
