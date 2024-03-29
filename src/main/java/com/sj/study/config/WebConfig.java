package com.sj.study.config;

import com.sj.study.config.auth.LoginUserArgumentResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@RequiredArgsConstructor
@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final LoginUserArgumentResolver loginUserArgumentResolver;

    /**
     * 스프링에서 LoginUserArgumentResolver 를 인식할 수 있게 설정
     * HandlerMethodArgumentResolver 는 항상 WebMvcConfigurer addArgumentResolvers() 를 통해 추가해야 함
     * */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(loginUserArgumentResolver);
    }
}
