package com.cloud.secure.streaming.entities;

import com.cloud.secure.streaming.common.enums.*;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.USE_DEFAULTS)
@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name = "course")
public class Course extends BaseEntity implements Serializable {

    @Id
    @Column(name = "id", nullable = false, length = 32, updatable = false)
    private String id;

    @Column(name = "user_id", nullable = false, length = 32, updatable = false)
    private String userId;

    @Column(name = "title", nullable = false, length = 64)
    private String title;

    @Column(name = "subtitle", length = 120)
    private String subtitle;

    @Column(columnDefinition = "varchar(45) default 'DRAFT'")
    @Enumerated(EnumType.STRING)
    private CourseStatus courseStatus; // WAITING_FOR_APPROVAL,PUBLISH,DRAFT,REJECT

    @Column(name = "image", columnDefinition = "TEXT")
    private String image;

    @Column(name = "promotion_video", columnDefinition = "TEXT")
    private String promotionVideo;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "varchar(32) default 'ENGLISH'")
    @Enumerated(EnumType.STRING)
    private Language language;

    @Column(name = "point", columnDefinition = "double default 0")
    private double point = 0;

    @Column(name = "discount", columnDefinition = "double default 0")
    private double discount = 0;

    @Column(name = "expire_date")
    private Date expireDate;

    @Column( columnDefinition = "varchar(32) default 'PENDING'")
    @Enumerated(EnumType.STRING)
    private AppStatus status;

    @Column(columnDefinition = "varchar(32) default 'ALL_LEVELS'")
    @Enumerated(EnumType.STRING)
    private CourseLevel level;

    @Column(name = "course_information_type") // LANDING_PAGE,PREVIEW,PRICING,PROMOTION
    private String courseInformationType;

    @Column( columnDefinition = "varchar(32) default 'FREE'")
    @Enumerated(EnumType.STRING)
    private CoursePrice coursePrice;

    @Column(name = "show_promotion", columnDefinition = "boolean default '0'")
    private boolean showPromotion;
}
