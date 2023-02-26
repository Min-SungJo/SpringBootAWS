package com.sj.study.web.dto;

import com.sj.study.domain.posts.Posts;
import lombok.Getter;

@Getter
public class PostsResponseDto {


    private Long id;
    private String title;
    private String content;
    private String author;

    // Entity 의 필드 중 일부만 사용 -> Entity 의 값을 받아 필드에 값을 넣음
    // 굳이 모든 필드를 가진 생성자가 필요하지 않으므로 Dto 는 Entity 를 받아 처리
    public PostsResponseDto(Posts entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.content = entity.getContent();
        this.author = entity.getAuthor();
    }
}
