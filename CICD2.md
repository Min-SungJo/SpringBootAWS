## 차이
CodeDeploy 를 사용하던 것과 달리 Beanstalk 을 사용하면,
1. 배포 파일 전송을 위해 S3에 업로하는 하는 과정이 없음
2. EC2에 배포를 위해 별도의 agent 설치가 필요하지 않음
3. Nginx 설치, OS 설정 등 OS 상에 필요한 모든 설정은 코드로 관리가 가능
## 1. Github Action yml 파일 생성