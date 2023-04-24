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
