package com.sj.study.domain.user;

import com.sj.study.domain.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column
    private String picture;

    // JPA 로 데이터베이스에 값을 저장할 때, Enum 값을 어떤 형태로 저장할지를 결정함
    // (기본적으로 int 이나 숫자가 의미하는 값을 알 수 없기 때문에 이를 문자열로 저장할 수 있도록 선언)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Builder
    public User(String name, String email, String picture, Role role) {
        this.name = name;
        this.email = email;
        this.picture = picture;
        this.role = role;
    }

    public User update(String name, String picture) {
        this.name = name;
        this.picture=picture;
        return this;
    }

    public String getRoleKey() {
        return this.role.getKey();
    }
}
