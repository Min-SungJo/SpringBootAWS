package com.sj.study.web;

import com.sj.study.web.dto.HelloResponseDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController // 컨트롤러가 JSON 을 반환하게 만들어줌
public class HelloController {

    @GetMapping("/hello") // HTTP Method Get 을 받을 수 있는 API 를 만들어줌
    public String hello() {
        return "hello";
    }

    //@RequestParam -> 외부에서 API 로 넘긴 파라미터를 가져오는 어노테이션
    //외부 에서 name 이라는 이름으로 넘긴 파라미터를 메소드 파라미터 String name 에 저장
    @GetMapping("/hello/dto")
    public HelloResponseDto helloDto(@RequestParam("name") String name, @RequestParam("amount") int amount) {
        return new HelloResponseDto(name,amount);
    }
}
