1. 구글 서비스에 신규 서비스 등록   
https://console.cloud.google.com   
새 프로젝트 생성   
API 및 지원 서비스 / 사용자 인증 정보 / 사용자 인증 정보 만들기 / OAuth 클라이언트 ID / 동의 화면 구성   
애플리케이션 이름: 구글 로그인 시 사용자에게 노출될 애플리케이션 이름   
지원 이메일: 사용자 동의 화면에서 노출될 이메일 주소, 보통 help 이메일 주소   
Google API 범위: 구글 서비스에서 사용할 범위 목록, 기본값은 email/profile/openid   
#### 승인된 리디렉션 URI:
서비스에서 파라미터로 인증 정보를 주었을 때, 인증이 성공하면 구글에서 리다이렉트할 URL   
스프링 부트 2 버전의 시큐리티에서는 기본적으로 {도메인}/login/oauth2/code/{소셜서비스코드}로 리다이렉트 URL 을 지원   
사용자가 별도로 리다이렉트 URL 을 지원하는 Controller 를 만들 필요가 없음(시큐리티에서 이미 구현)   
AWS 서버에 배포하면 localhost 외의 주소 추가
2. application-oauth.properties 에 클라이언트 ID 와 보안 비밀 코드를 등록   
oauth 라는 이름의 profile 이 생성, 내부에 작성한 설정을 가져올 수 있음
3. .gitignore 설정