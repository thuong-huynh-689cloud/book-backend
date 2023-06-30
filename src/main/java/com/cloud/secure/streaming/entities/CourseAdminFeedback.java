package com.cloud.secure.streaming.entities;

import com.cloud.secure.streaming.common.enums.FeedbackType;
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
@Table(name = "course_admin_feedback")
public class CourseAdminFeedback  extends BaseEntity implements Serializable {

    @Id
    @Column(name = "id", nullable = false, length = 32, updatable = false)
    private String id;

    // COURSE_LADING_PAGE,INTENDED_LEARNERS,CURRICULUM,COURSE_PREVIEW,LANGUAGE_CAPTION,PRICING,PROMOTION,ANNOUNCEMENTS,FULL_COURSE
    @Column( columnDefinition = "varchar(32) default 'FULL_COURSE'")
    @Enumerated(EnumType.STRING)
    private FeedbackType feedbackType;

    @Column(name = "feedback", columnDefinition = "TEXT")
    private String feedback;

    @Column(name = "course_id",nullable = false, length = 32, updatable = false)
    private String courseId;
}
