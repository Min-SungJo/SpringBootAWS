package com.sj.study.config.auth;

import com.sj.study.domain.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.SecurityFilterChain;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity // Spring Security 설정을 활성화
public class SecurityConfig {
//public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final CustomOAuth2UserService customOAuth2UserService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception { // HttpSecurity 는 낮은 버전(Spring Boot 버전)에서 호환되지 않음
        http
                .csrf().disable()
                .headers().frameOptions().disable()
                .and()
                .authorizeRequests()
                .antMatchers("/", "/css/**", "/images/**", "/js/**", "/h2-console/**", "/profile").permitAll()
                .antMatchers("/api/v1/**").hasRole(Role.USER.name())
                .anyRequest().authenticated()
                .and()
                .logout()
                .logoutSuccessUrl("/")
                .and()
                .oauth2Login()
                .userInfoEndpoint()
                .userService(customOAuth2UserService);

        return http.build();
    }

//    /**람다식으로 변환*/
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception { // HttpSecurity 는 낮은 버전(Spring Boot 버전)에서 호환되지 않음
//        http
//                .csrf().disable()
//                .headers().frameOptions().disable()
//                .and()
//                .authorizeHttpRequests(authorize -> authorize
//                        .antMatchers("/", "/css/**", "/images/**", "/js/**", "/h2-console/**", "/profile").permitAll()
//                        .antMatchers("/api/v1/**").hasRole(Role.USER.name())
//                .anyRequest().authenticated())
//                .logout(logout -> logout
//                        .logoutSuccessUrl("/"))
//                .oauth2Login()
//                        .userInfoEndpoint(oauth2Login -> oauth2Login
//                        .userService(customOAuth2UserService));
//        return http.build();
//    }



//    @Override
//    protected void configure(HttpSecurity http) throws Exception{
//        http
//                .csrf().disable()
//                .headers().frameOptions().disable()// h2 콘솔을 사용하기 위해 해당 옵션을 비활성화
//                .and()
//                .authorizeRequests()// URL 별 권한 관리 시작
//                .antMatchers("/", "/css/**", "/images/**",
//                        "/js/**", "h2-console/**").permitAll()// 권한 관리 대상을 지정, URL 과 HTTP 메소드별로 관리 가능,
//                .antMatchers("/api/v1/**").hasRole(Role.USER.name()) // USER 만 이용할 수 있는 주소
//                .anyRequest().authenticated()// 설정 값을 제외한 나머지의 관리, authenticated 를 통해 인증된 사용자만 허용하게 함
//                .and()
//                .logout()
//                .logoutSuccessUrl("/") // 로그아웃 성공시 향할 주소
//                .and()
//                .oauth2Login() // OAuth 기능에 대한 설정의 진입점
//                .userInfoEndpoint()// 로그인 성공 이후 사용자 정보를 가져올 때의 설정을 담당
//                .userService(customOAuth2UserService); // 소셜 로그인 성공 시 후속 조치를 진행할 인터페이스의 구현체를 등록, 리소스 서버에서 사용자 정보를 가져온 상태에서 추가로 진행하고자 하는 기능을 명시
//    }
}
