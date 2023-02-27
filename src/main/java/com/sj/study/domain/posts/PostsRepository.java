package com.sj.study.domain.posts;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

//JPA 에서 인터페이스를 생성 후 상속을 하면 CRUD 메소드가 자동으로 생성됨 Entity 와 같은 위치에 있어야 함
public interface PostsRepository extends JpaRepository<Posts,Long> {
    @Query("SELECT p FROM Posts p ORDER BY p.id DESC")
    List<Posts> findAllDesc();
}

// 규모가 있는 프로젝트에서의 데이터 조회는 FK 조인, 복잡한 조건 등으로 인해 이러한 Entity 클래스 만으로 처리하기 어려움
// -> 조회용 프레임 워크를 추가로 사용 (querydsl, jooq, Mybatis) - (querydsl 타입 안정성 보장-메소드 기반으로 쿼리를 생성하기 때문)
// -> 등록/수정/삭제는 SpringDataJPA 를 통해 진행