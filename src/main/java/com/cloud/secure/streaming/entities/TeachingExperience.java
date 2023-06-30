package com.cloud.secure.streaming.entities;

import com.cloud.secure.streaming.common.enums.TeachingType;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "teaching_experience")
public class TeachingExperience extends BaseEntity implements Serializable {

    @Id
    @Column(name = "id", nullable = false, length = 32, updatable = false)
    private String id;

    @Column(name = "job_experience_id", length = 32, nullable = false)
    private String jobExperienceId;

    @Column(name = "teaching_place", nullable = false)
    private String teachingPlace;

    @Column(name = "type", columnDefinition = "varchar(32) default 'OFFLINE'")
    @Enumerated(EnumType.STRING)
    private TeachingType type;

    @Column(name = "major", nullable = false)
    private String major;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @JsonIgnore
    @Column(name = "is_updating", columnDefinition = "tinyint default 0")
    private boolean isUpdating = false;
}
