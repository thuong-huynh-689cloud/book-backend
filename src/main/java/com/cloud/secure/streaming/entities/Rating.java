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
@Table(name = "rating")
public class Rating extends BaseEntity implements Serializable {
    @Id
    @Column(name = "id", nullable = false, length = 32, updatable = false)
    private String id;

    @Column(name = "select_rating", columnDefinition = "int default '0'")
    private int selectRating = 0;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "course_id", nullable = false, length = 32, updatable = false)
    private String courseId;
}
