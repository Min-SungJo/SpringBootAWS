package com.sj.study.web;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class) // 테스트를 진행할 때 JUnit 에 내장된 실행자 외에 다른 실행자를 실행시킴, 스프링 부트 테스트와 JUnit 사이에 연결자 역할을 함
@WebMvcTest(controllers = HelloController.class) // 현재 클래스에서 컨트롤러만 사용할 경우 선언, @Controller, @ControllerAdvice 사용 가능
public class HelloControllerTest {

    /**웹 API 를 테스트할 때 사용
     * 스프링 MVC 테스트의 시작점
     * 이 클래스를 통해 HTTP, GET, POST 등에 대한 API 를 테스트할 수 있음*/
    @Autowired // 스프링이 관리하는 빈을 주입 받음
    private MockMvc mockMvc;

    @Test
    public void testHello() throws Exception {
        String hello = "hello";
        mockMvc.perform(get("/hello")) // 다음 주소로 HTTP GET 요청, 체이닝 지원 -> 여러 검증을 이어서 할 수 있음
                .andExpect(status().isOk()) // 200, 404, 500 등 HTTP Header Status 검증
                .andExpect(content().string(hello)); // 리턴값(content)이 맞는지 검증
    }

    @Test
    public void testHelloDto() throws Exception {
        String name = "hello";
        int amount = 1000;
        mockMvc.perform(get("/hello/dto")
                .param("name", name) // API 테스트할 때 사용될 요청 파라미터를 설정, 단 값은 String 만 허용(숫자, 날짜도 문자열로)
                .param("amount", String.valueOf(amount)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name",is(name))) // JSON 응답값을 필드별로 검증할 수 있는 메소드, $ 를 기준으로 필드명 명시
                .andExpect(jsonPath("$.amount", is(amount)));
    }

}
