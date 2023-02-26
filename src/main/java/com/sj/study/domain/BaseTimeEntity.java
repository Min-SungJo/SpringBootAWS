여package com.sj.study.domain;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

/**모든 Entity 의 상위 클래스가 되어 Entity 들의 createDate, modifiedDate 자동관리*/
@Getter
@MappedSuperclass // JPA Entity 클래스들이 BaseTimeEntity 를 상속할 경우 필드들(createDate, modifiedDate)도 칼럼으로 인식하도록 함 -> Posts 클래스가 상속받도록 설정하기
@EntityListeners(AuditingEntityListener.class) // Auditing 기능을 포함시킴
public abstract class BaseTimeEntity {

    @CreatedDate // Entity 가 생성되어 저장될 때 시간이 자동 저장됨
    private LocalDateTime createDate;

    @LastModifiedDate // 조회한 Entity 의 값을 변경할 때, 시간이 자동 저장됨
    private LocalDateTime modifiedDate;
}
