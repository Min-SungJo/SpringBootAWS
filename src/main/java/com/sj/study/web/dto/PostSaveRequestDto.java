package com.sj.study.web.dto;

import com.sj.study.domain.posts.Posts;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostSaveRequestDto {

    // Entity(Posts)와 유사한 형태임에도 Dto 클래스를 추가로 생성
    // DB 와 맞닿은 핵심 클래스인, Entity 클래스를 Request/Response 클래스로 사용하면 안됨
    // (대규모 변경이기 때문)
    // 수많은 서비스 클래스나 비즈니스 로직들이 Entity 클래스를 기준으로 동작
    // -> Entity 클래스가 변경되면 여러 클래스에 영향을 끼침
    // Request 와 Response 용 Dto 는 View 를 위한 클래스라 자주 변경이 필요함
    // -> View Layer 와 DB Layer 의 역할 분리를 철저하게 하는 것이 좋음
    // Controller 에서 결괏값으로 여러 테이블을 조인해서 줘야 할 경우가 빈번하므로 Entity 클래스만으로 표현하기 어려움
    // * Entity 클래스와 Controller 에서 쓸 Dto 분리가 필요

    private String title;
    private String content;
    private String author;

    @Builder
    public PostSaveRequestDto(String title, String content, String author) {
        this.title = title;
        this.content = content;
        this.author = author;
    }

    public Posts toEntity() {
        return Posts.builder()
                .title(title)
                .content(content)
                .author(author)
                .build();
    }
}
