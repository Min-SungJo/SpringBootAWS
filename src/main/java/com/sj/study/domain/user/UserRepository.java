package com.sj.study.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * User 의 CRUD 를 책임짐
 * */
public interface UserRepository extends JpaRepository<User, Long> {

    /**소셜 로그인으로 반환되는 값 중 email 을 통해 이미 생성된 사용자인지 처음 가입하는 사용자인지 판단하기 위한 메소드*/
    Optional<User> findByEmail(String email);
}
