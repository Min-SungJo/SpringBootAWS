package com.sj.study.domain.posts;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter // Setter 는 Entity 에서 만들지 않음 -> 값이 변하는 위치를 명확히 하여 이를 추적하기 쉽도록 하기 위함
@NoArgsConstructor
@Entity // 테이블과 링크될 클래스임을 나타냄(기본값으로 클래스의 카멜케이스 이름을 언더스코어 네이밍으로 테이블 이름을 매칭함 - TableName.java -> table_name table)
public class Posts { // JPA 를 사용하면, DB 데이터 수정을 원할 때, 이 Entity 클래스 수정을 통해 작업할 수 있음
    @Id // 해당 테이블의 PK 필드를 설정함
    @GeneratedValue(strategy = GenerationType.IDENTITY) // PK 생성 규칙 설정
    private Long id;

    @Column(length = 500, nullable = false) // 테이블의 칼럼을 나타내며 생략 가능, 추가 설정 시 표기
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    private String author;

    @Builder // Setter 대신, Builder 를 통해 생성자 값 채운 뒤 DB 에 삽입, 어느 필드에 어떤 값을 채울지 명확히 인지 가능하다는 장점.
    public Posts(String title, String content, String author){
        this.title=title;
        this.content=content;
        this.author=author;
    }
}
