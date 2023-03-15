package com.sj.study;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**@SpringBootApplication 으로 인해 스프링 부트의 자동 설정, 스프링 Bean 읽기와 생성을 모두 자동으로 설정.
 * 작성 위치 부터 설정을 읽기 때문에 프로젝트 최상단에 위치해야함 */
//@EnableJpaAuditing // JPA Auditing 활성화, WebMvcTest 에는 @Entity 클래스가 없기 때문에 주석처리
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args); // 위 코드를 통해 내장 WAS 를 실행함 -> 언제 어디서나 같은 환경에서 스프링 부트를 배포할 수 있음
    }
}
