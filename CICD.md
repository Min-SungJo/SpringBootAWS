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