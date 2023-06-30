package com.cloud.secure.streaming.entities;

import com.cloud.secure.streaming.common.enums.DisplayStatus;
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
@Table(name = "section")
public class Section extends BaseEntity implements Serializable {

    @Id
    @Column(name = "id", nullable = false, length = 32, updatable = false)
    private String id;

    @Column(name = "name", length = 64)
    private String name;

    @Column(name = "course_id", nullable = false, length = 32)
    private String courseId;

    @Column(nullable = false, columnDefinition = "varchar(32) default 'SHOW'")
    @Enumerated(EnumType.STRING)
    private DisplayStatus displayStatus;
}
