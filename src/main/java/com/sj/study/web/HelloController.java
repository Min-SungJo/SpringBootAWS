package com.sj.study.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController // 컨트롤러가 JSON 을 반환하게 만들어줌
public class HelloController {

    @GetMapping("/hello") // HTTP Method Get 을 받을 수 있는 API 를 만들어줌
    public String hello() {
        return "hello";
    }
}
