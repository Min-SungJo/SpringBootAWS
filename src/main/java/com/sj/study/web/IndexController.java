package com.sj.study.web;

import com.sj.study.service.posts.PostsService;
import com.sj.study.web.dto.PostsResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RequiredArgsConstructor
@Controller
public class IndexController {

    private final PostsService postsService;

    // 머스테치 스타터 덕분에 컨트로로러에서 문자열을 반환할 때, 앞의 경로와 뒤의 파일 확장자는 자동으로 지정됨
    // src/main/resources/templates
    // .mustache
    @GetMapping("/")
    public String index(Model model) { // 서버 템플릿 엔진에서 사용할 수 있는 객체를 저장할 수 있음
        model.addAttribute("posts", postsService.findAllDesc());
        return "index"; // src/main/resources/templates/index.mustache 로 전환되어 View Resolver 가  처리
    }
    @GetMapping("/posts/save")
    public String postsSave() {
        return "posts-save";
    }
    @GetMapping("/posts/update/{id}")
    public String postsUpdate(@PathVariable Long id, Model model) {
        PostsResponseDto dto = postsService.findById(id);
        model.addAttribute("post", dto);

        return "posts-update";
    }
}
