package com.cloud.secure.streaming.entities;

import com.cloud.secure.streaming.common.enums.ContentType;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import javax.persistence.*;
import java.io.Serializable;
import java.sql.Time;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.USE_DEFAULTS)
@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name = "lecture")
public class Lecture extends BaseEntity implements Serializable {

    @Id
    @Column(name = "id", nullable = false, length = 32, updatable = false)
    private String id;

    @Column(name = "section_id", nullable = false, length = 32)
    private String sectionId;

    @Column(name = "lecture_name" , length = 64)
    private String lectureName;

    @Column(name = "content_type")
    @Enumerated(EnumType.STRING)
    private ContentType contentType;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "original_file_name")
    private String originalFileName;

    @Column(name = "file_size")
    private Long fileSize; // By kb

    @Column(name = "duration")
    private Integer duration; // by minute

    @Column(name = "number_of_question")
    private Integer numberOfQuestion;

    @Column(name = "downloadable", columnDefinition = "boolean default false")
    private boolean downloadable;

    @Column(name = "public_view", columnDefinition = "boolean default false")
    private boolean publicView;
}
