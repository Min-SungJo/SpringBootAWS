package com.sj.study.config.auth;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * indexController 에서 세션값을 가져오는 부분 반복하지 않게 하기 위한 설정
 * */
@Target(ElementType.PARAMETER) // 이 어노테이션이 생성될 수 있는 위치를 지정, 파라미터로 선언된 객체에서만 사용
@Retention(RetentionPolicy.RUNTIME)
public @interface LoginUser { // @interface 를 통해 LoginUser 라는 이름의 어노테이션 클래스로 지정
}
