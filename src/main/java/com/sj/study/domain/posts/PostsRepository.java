package com.sj.study.domain.posts;

import org.springframework.data.jpa.repository.JpaRepository;

//JPA 에서 인터페이스를 생성 후 상속을 하면 CRUD 메소드가 자동으로 생성됨 Entity 와 같은 위치에 있어야 함
public interface PostsRepository extends JpaRepository<Posts,Long> {

}
