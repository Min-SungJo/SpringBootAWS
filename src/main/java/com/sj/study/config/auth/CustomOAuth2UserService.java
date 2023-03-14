package com.sj.study.config.auth;

import com.sj.study.config.auth.dto.OAuthAttributes;
import com.sj.study.config.auth.dto.SessionUser;
import com.sj.study.domain.user.User;
import com.sj.study.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Collections;

/**구글 로그인 이후 가져온 사용자의 정보를 기반으로 가입 및 정보수정, 세션 저장 등의 기능을 지원*/
@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final UserRepository userRepository;
    private final HttpSession httpSession;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        /**현재 로그인 진행중인 서비스를 구분하는 코드 (네이버 등 확장 고려)*/
        String registrationId = userRequest
                .getClientRegistration()
                .getRegistrationId();

        /**OAuth2 진행 시 키가 되는 필드값, PK 와 같은 의미(구글의 기본 코드는 "sub")
         * 이후 네이버, 구글 고르인을 동시 지원할 때 사용
         * */
        String userNameAttributeName = userRequest
                .getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();

        /**OAuth2UserService 를 통해 가져온 OAuth2User attribute 를 담을 클래스,
         * 이후 네이버 등 다른 소셜 로그인도 이 클래스를 사용
         * */
        OAuthAttributes attributes = OAuthAttributes.of(
                registrationId,
                userNameAttributeName,
                oAuth2User.getAttributes());

        User user = saveOrUpdate(attributes);
        httpSession.setAttribute(
                "user", new SessionUser(user)); // 세션에 사용자 정보를 저장하기 위한 Dto 클래스
        // User 클래스를 쓰지 않고 새로 만듬 -> 구글 사용자 정보가 업데이트 되었을 때를 대비

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(user.getRoleKey())),
                attributes.getAttributes(),
                attributes.getNameAttributeKey());
    }


    private User saveOrUpdate(OAuthAttributes attributes) {
        User user = userRepository.findByEmail(attributes.getEmail())
                .map(entity -> entity.update(attributes.getName(), attributes.getPicture()))
                .orElse(attributes.toEntity());

        return userRepository.save(user);
    }
}