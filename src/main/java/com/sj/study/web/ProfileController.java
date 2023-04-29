package com.sj.study.web;

import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class ProfileController {
    private final Environment env;

    @GetMapping("/profile")
    public String profile() {
        List<String> profiles = Arrays.asList(env.getActiveProfiles());
        // 현재 실행 중인 ActiveProfile 을 모두 가져옴
        // real, oauth, real-db 등이 활성화되어 있다면(active) 3개가 모두 담겨 있음
        List<String> realProfiles = Arrays.asList("real", "real1", "real2");
        // real, real1, real2 모두 배포에 사용될 profile 이라 이 중 하나라도 있으면 그 값을 반환
        // + 현재 무중단 배포에서는 real1과 real2만 사용되지만 step2를 다시 사용해 볼 수도 있으니 real 도 남겨놓은 것임
        String defaultProfiles = profiles.isEmpty()? "default": profiles.get(0);

        return profiles.stream()
                .filter(realProfiles::contains)
                .findAny()
                .orElse(defaultProfiles);
    }
}
