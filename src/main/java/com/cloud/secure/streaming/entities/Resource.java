package com.cloud.secure.streaming.entities;

import com.cloud.secure.streaming.common.enums.ResourceType;
import com.cloud.secure.streaming.common.enums.UploadStatus;
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
@Table(name = "resource")
public class Resource extends BaseEntity implements Serializable {
    @Id
    @Column(name = "id", length = 32, nullable = false, updatable = false)
    private String id;

    @Column(name = "title", length = 124)
    private String title;

    @Column(name = "type")
    private ResourceType type;

    @Column(name = "external_link")
    private String externalLink;

    @Column(name = "file_name")
    private String fileName; // for type FILE, SOURCE_CODE

    @Column(name = "original_file_name")
    private String originalFileName;

    @Column(name = "upload_status")
    private UploadStatus uploadStatus;

    @Column(name = "downloadable", columnDefinition = "tinyint default 0")
    private boolean downloadable;

    @Column(name = "lecture_id", length = 32, nullable = false, updatable = false)
    private String lectureId;
}
