# 구글
1. 구글 서비스에 신규 서비스 등록   
https://console.cloud.google.com   
새 프로젝트 생성   
API 및 지원 서비스 / 사용자 인증 정보 / 사용자 인증 정보 만들기 / OAuth 클라이언트 ID / 동의 화면 구성   
애플리케이션 이름: 구글 로그인 시 사용자에게 노출될 애플리케이션 이름   
지원 이메일: 사용자 동의 화면에서 노출될 이메일 주소, 보통 help 이메일 주소   
Google API 범위: 구글 서비스에서 사용할 범위 목록, 기본값은 email/profile/openid   
#### 승인된 리디렉션 URI:
> 서비스에서 파라미터로 인증 정보를 주었을 때, 인증이 성공하면 구글에서 리다이렉트할 URL   
스프링 부트 2 버전의 시큐리티에서는 기본적으로 {도메인}/login/oauth2/code/{소셜서비스코드}로 리다이렉트 URL 을 지원   
사용자가 별도로 리다이렉트 URL 을 지원하는 Controller 를 만들 필요가 없음(시큐리티에서 이미 구현)   
AWS 서버에 배포하면 localhost 외의 주소 추가
2. application-oauth.properties 에 클라이언트 ID 와 보안 비밀 코드를 등록   
oauth 라는 이름의 profile 이 생성, 내부에 작성한 설정을 가져올 수 있음
   > spring.security.oauth2.client.registration.google.client-id=   
   spring.security.oauth2.client.registration.google.client-secret=   
   spring.security.oauth2.client.registration.google.scope=profile,email   
3. .gitignore 설정

# 네이버
1. 네이버 오픈 API 에 서비스 등록   
https://developers.naver.com/apps/#/register?api=nvlogin   
애플리케이션 이름, 사용 API(네이버 로그인)   
환경 설정(PC 웹)   
서비스 URL(http://localhost:8080/)   
Callback URL(http://localhost:8080/login/oauth2/code/naver)>
    > 구글에서 등록한 리디렉션 URL 과 같은 역할
2. application-oauth.properties 에 클라이언트 ID 와 클라이언트 SECRET 등록
    > 네이버는 스프링 시큐리티를 공식 지원하지 않기 때문에 그동안 CommonOAuth2Provider 에서 설정해주던 값들을 수동으로 입력해야 함   
   > ### registration
   > spring.security.oauth2.client.registration.naver.client-id=
spring.security.oauth2.client.registration.naver.client-secret=
spring.security.oauth2.client.registration.naver.redirect-uri={baseUrl}/{action}/oauth2/code/{registrationId}
spring.security.oauth2.client.registration.naver.authorization-grant-type=authorization_code
spring.security.oauth2.client.registration.naver.scope=name,email,profile_image
spring.security.oauth2.client.registration.naver.client-name=Naver
   > ### provider
   > spring.security.oauth2.client.provider.naver.authorization-uri=https://nid.naver.com/oauth2.0/authorize   
   spring.security.oauth2.client.provider.naver.token-uri=https://nid.naver.com/oauth2.0/token   
spring.security.oauth2.client.provider.naver.user-info-uri=https://openapi.naver.com/v1/nid/me   
기준이 되는 user_name 의 이름을 네이버에서는 response 로 함 -> 네이버 회원 조회 시 반환되는 JSON 형태에 따름   
spring.security.oauth2.client.provider.naver.user-name-attribute=response   