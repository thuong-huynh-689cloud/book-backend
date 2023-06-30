package com.cloud.secure.streaming.entities;

import com.cloud.secure.streaming.common.enums.IntendedLearnerType;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.USE_DEFAULTS)
@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name = "intended_learner")
public class IntendedLearner extends BaseEntity implements Serializable {

    @Id
    @Column(name = "id", nullable = false, length = 32, updatable = false)
    private String id;

    @Column(name = "course_id", nullable = false, length = 32, updatable = false)
    private String courseId;

    @Column(name = "sentence", length = 160)
    private String sentence;

    @Column(name = "sequence")
    private Integer sequence;

    @Column(nullable = false, columnDefinition = "varchar(45) default ' COURSE_OBJECT'")
    @Enumerated(EnumType.STRING)
    private IntendedLearnerType type;
}
