package com.sj.study.web;

import com.sj.study.config.auth.LoginUser;
import com.sj.study.config.auth.dto.SessionUser;
import com.sj.study.service.posts.PostsService;
import com.sj.study.web.dto.PostsResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@Controller
public class IndexController {

    private final PostsService postsService;
//    private final HttpSession httpSession;

    // 머스테치 스타터 덕분에 컨트로로러에서 문자열을 반환할 때, 앞의 경로와 뒤의 파일 확장자는 자동으로 지정됨
    // src/main/resources/templates
    // .mustache
    @GetMapping("/")
//    public String index(Model model) { // 서버 템플릿 엔진에서 사용할 수 있는 객체를 저장할 수 있음
    public String index(Model model, @LoginUser SessionUser user) {
        // 기존에(HttpSession 생성 및 httpSession.getAttribute("user")) 가져오던 값을 어느 컨트롤러든지 @LoginUser 만 사용하면 세션정보를 가져올 수 있게 설정
        model.addAttribute("posts", postsService.findAllDesc());
        /**CustomOAuth2UserService 에서 로그인 성공 시 세션에 SessionUser 저장
         * 로그인 성공 시 httpSession.getAttribute("user") 값을 가져옴
         * */
//        SessionUser user = (SessionUser) httpSession.getAttribute("user");

        if (user != null) { // 세션에 저장된 값이 있을 때만 model 에 userName 으로 등록, 값이 없으면(mustache 에서) 로그인 버튼이 보이게 됨
            model.addAttribute("userName", user.getName());
        }

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
