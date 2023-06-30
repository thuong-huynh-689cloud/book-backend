package com.cloud.secure.streaming.entities;

import com.cloud.secure.streaming.common.enums.AppStatus;
import com.cloud.secure.streaming.common.enums.MediaInfoType;
import com.cloud.secure.streaming.common.enums.VideoType;
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
@Table(name = "media_info")
public class MediaInfo extends BaseEntity implements Serializable {

    @Id
    @Column(name = "id", nullable = false, length = 32, updatable = false) // FileName S3
    private String id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "name_original",length = 125)
    private String nameOriginal;

    @Column(name = "extension", nullable = false)
    private String extension;

    @Column(name = "file_size", nullable = false)
    private Long fileSize; // By bytes

    @Column(name = "duration", nullable = false)
    private Long duration; // By seconds

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "lecture_id")
    private String lectureId;

    @Column(name = "course_id")
    private String courseId;

    @Column(nullable = false, columnDefinition = "varchar(32) default 'COURSE'")
    @Enumerated(EnumType.STRING)
    private MediaInfoType mediaInfoType;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private VideoType videoType;

    @Column(nullable = false, columnDefinition = "varchar(32) default 'PENDING'")
    @Enumerated(EnumType.STRING)
    private AppStatus status;

    @Column(name = "is_encrypt", columnDefinition = "boolean default '0'")
    private boolean isEncrypt ;
}
