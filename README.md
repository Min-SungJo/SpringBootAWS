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
