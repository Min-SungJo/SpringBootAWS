# SpringBootAWS

## 단위 테스트 코드 작성 이점
1. 단위 테스트는 개발단계 초기에 문제를 발견하게 도와줌
2. 단위 테스트는 나중에 코드를 리팩토링하거나 라이브러리 업그레이드 등에서 기존 기능이 올바르게 작동하는지 확인할 수 있음(예. 회귀 테스트)
3. 단위 테스트는 기능에 대한 불확실성을 감소시킬 수 있음
4. 단위 테스트는 시스템에 대한 실제 문서를 제공. 자체를 문서로 사용할 수 있음
(톰캣 재시작 없이)자동 검증이 가능, 개발자가 만든 기능을 안전하게 보호, 기존 기능이 잘 작동되는 것을 보장


## JPA: 자바 표준 ORM(Object Relation Mapping)
(*MyBatis 는 SQL Mapper 임*)    
객체를 관계형 데이터 베이스에서 관리하는 것이 중요 -> 애플리케이션의 중심이 SQL 로 쏠릴 수 있음   
객체 중심으로 개발 할 수 있게 도와줌    

## 스프링 웹 계층   
### Web Layer(controllers, exception handlers, filters, view templates, and so on) - DTOs   
### Service Layer(application services and infrastructures services)   
### Repository Layer(repository interfaces and their implementations) - Domain Model(domain services, entities, and value objects)   

1. Web Layer   
@Controller 컨트롤러 와 JSP/Freemarker 등의 뷰 템플릿 영역   
@Filter 필터, 인터셉터, @ControllerAdvice 컨트롤러 어드바이스 등 외부 요청과 응답에 대한 전반적인 영역   

2. Service Layer   
@Service 서비스 영역   
일반적으로 Controller 와 Dao 의 중간 영역에서 사용됨   
@Transactional 이 사용되어야 하는 영역   

3. Repository Layer   
DB 와 같이 데이터 저장소에 접근하는 영역 (Dao 영역)   

4. Dtos   
Dto(Data Transfer Object)는 계층 간에 데이터 교환을 위한 객체이며 이들의 영역   
예컨대 뷰 템플릿 엔진에서 사용될 객체나 Repository Layer 에서 결과로 넘겨준 객체 등   

5. Domain Model   
도메인이라 불리는 개발 대상을 모든 사람이 동일한 관점에서 이해할수 있고 공유할 수 있도록 단순화시킨 것   
@Entity 가 사용된 영역 역시 포함   
무조건 데이터베이스의 테이블과 간계가 있어야 하는 것은 아님   
VO처럼 값 객체들도 이 영역에 해당   

5가지 레이어 중 Domain 은 비즈니스 처리를 담당한다.  
*서비스 메소드는 트랜잭션과 도메인 간의 순서만 보장.*  

## dirty checking 더티체킹   
JPA 의 EntityManager 엔티티 매니저가 활성화된 상태로(Spring Data JPA 사용 Default) 트랜잭션 안에서 데이터베이스에서 데이터를 가져오면 이데이터는 영속성 컨텍스트가 유지된 상태임   
이 상태에서 해당 데이터의 값을 변경하면 트랜잭션이 끝나는 시점에서 해당 테이블에 변경분을 반영   
-> Entity 객체의 값만 변경하면 별도로 Update 쿼리를 날릴 필요가 없음   

## 서버 템플릿 엔진과 머스테치
### 템플릿 엔진
지정된 템플릿 양식과 데이터가 합쳐져 HTML 문서를 출력하는 소프트웨어
(웹 사이트의 화면을 어떤 형태로 만들지 도와주는 양식)
#### 예시
#### 서버 템플릿 엔진: Freemaker, JSP(View 역할만 하도록 구성할 때)
#### 클라이언트 템플릿 엔진: View(React, Vue) - 지정된 템플릿과 데이터를 이용하여 HTML 을 생성하는 템플릿 엔진

서버 템플릿 엔진을 이용한 화면 생성은 서버에서 Java 코드로 문자열을 만든 뒤 이 문자열을 HTML 로 변환하여 브라우저로 전달   
-> 서버에서 얼추 정리하고 화면을 설정할 문자열을 브라우저로 전달   
클라이언트 템플릿 엔진(JS)은 브라우저 위에서 작동(단순한 문자열이 아님), 서버에서 이미 코드가 벗어났음   
-> 서버에서 데이터를 Json 으로 전달하여 클라이언트에서 조립   

### 머스테치
수많은 언어를 지원하는 가장 심플한 템플릿 엔진

#### 각종 템플릿 엔진의 단점
JSP, Velocity: 스프링 부트에서 권장하지 않음   
Freemaker: 템플릿 엔진으로는 너무 과하게 많은 기능을 지원, 높은 자유도로 인해 숙련도가 낮을 수록 Freemaker 안에 비즈니스 로직이 추가될 확률이 높음   
Thymeleaf: 스프링 진영에서 적극적으로 밀고 있지만 문법이 어려움, HTML 태그에 속성으로 템플릿 기능을 사용하는 방식(Vue.js 와 비슷한 태그 속성 방식)   
#### 머스테치 장점   
-> 인텔리제이 커뮤니티 버전에서 플러그인 사용가능   