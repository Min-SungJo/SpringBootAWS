package com.sj.study.web;

import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IndexControllerTest extends TestCase {

    @Autowired
    private TestRestTemplate restTemplate;

    /**실제로 URL 호출 시 페이지의 내용이 제대로 호출되는 지에 대한 테스트*/
    @Test
    public void mainPageLoading() {
        // when
        String body = this.restTemplate.getForObject("/", String.class);

        // then
        assertThat(body).contains("스프링 부트로 시작하는 웹 서비스"); // 모든 코드를 확인할 수는 없으니 특정 문자열이 포함되어 있는지만 비교
    }

}