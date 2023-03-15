package com.sj.study.web.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
// Junit 의 기본 assertThat 이 아닌 assertj 의 assertThat 을 사용한 이유
// -> CoreMatchers 와 달리 추가적으로 라이브러리가 필요하지 않고 자동완성이 좀 더 확실히 지원됨

public class HelloResponseDtoTest {

    @Test
    public void testLombok() {
        //given
        String name = "test";
        int amount = 1000;

        //when
        HelloResponseDto dto = new HelloResponseDto(name, amount);

        //then
        assertThat(dto.getName()).isEqualTo(name);
        //assertThat 는 assertj 라는 테스트 검증 라이브러리의 검증 메소드,
        //검증하고 싶은 대상을 메소드 인자로 받음,
        //메소드 체이닝이 지원되어 isEqualTo와 같이 메소드를 이어서 사용
        assertThat(dto.getAmount()).isEqualTo(amount);
        //isEqualTo 는 assertj 의 동등 비교 메소드,
        //assertThat 에 있는 값과 비교, 같을 때 성공
    }

}