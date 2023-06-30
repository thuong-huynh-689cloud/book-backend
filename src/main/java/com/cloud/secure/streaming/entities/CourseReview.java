package com.cloud.secure.streaming.entities;

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
@Table(name = "course_review")
public class CourseReview extends BaseEntity implements Serializable {
    @Id
    @Column(name = "id", nullable = false, length = 32, updatable = false)
    private String id;

    @Column(name = "rating",columnDefinition = "int default 0")
    private int rating;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "course_id", length = 32, nullable = false, updatable = false)
    private String courseId;

    @Column(name = "user_id", length = 32, nullable = false, updatable = false)
    private String userId;
}
