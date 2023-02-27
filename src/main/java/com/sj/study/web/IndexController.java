package com.sj.study.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    // 머스테치 스타터 덕분에 컨트로로러에서 문자열을 반환할 때, 앞의 경로와 뒤의 파일 확장자는 자동으로 지정됨
    // src/main/resources/templates
    // .mustache
    @GetMapping("/")
    public String index() {
        return "index"; // src/main/resources/templates/index.mustache 로 전환되어 View Resolver 가  처리
    }
    @GetMapping("/posts/save")
    public String postsSave() {
        return "posts-save";
    }
}
