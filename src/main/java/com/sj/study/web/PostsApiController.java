package com.sj.study.web;

import com.sj.study.service.posts.PostsService;
import com.sj.study.web.dto.PostSaveRequestDto;
import com.sj.study.web.dto.PostsResponseDto;
import com.sj.study.web.dto.PostsUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class PostsApiController {

    // API 를 만들기 위해 3개의 클래스가 필요함
    // Request 데이터를 받을 Dto
    // API 요청을 받을 Controller
    // 트랜잭션, 도메인 기능 간의 순서를 보장하는 Service - 비즈니스 로직을 처리하지 않음

    private final PostsService postsService;

    @PostMapping("/api/v1/posts")
    public Long save(@RequestBody PostSaveRequestDto requestDto) {
        return postsService.save(requestDto);
    }

    @PutMapping("/api/v1/posts/{id}")
    public Long update(@PathVariable Long id, @RequestBody PostsUpdateRequestDto requestDto) {
        return postsService.update(id, requestDto);
    }

    @GetMapping("/api/v1/posts/{id}")
    public PostsResponseDto findById(@PathVariable Long id) {
        return postsService.findById(id);
    }
}
