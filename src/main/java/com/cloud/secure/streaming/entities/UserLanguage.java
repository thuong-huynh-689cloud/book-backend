package com.cloud.secure.streaming.entities;

import com.cloud.secure.streaming.common.enums.AppStatus;
import com.cloud.secure.streaming.common.enums.Language;
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
@Table(name = "user_language")
public class UserLanguage extends BaseEntity implements Serializable {

    @Id
    @Column(name = "id", nullable = false, length = 32, updatable = false)
    private String id;

    @Column(name = "user_id", length = 32, nullable = false)
    private String userId;

    @Column(nullable = false, columnDefinition = "varchar(32) default 'ENGLISH'")
    @Enumerated(EnumType.STRING)
    private Language language;

    @Column(nullable = false, columnDefinition = "varchar(32) default 'ACTIVE'")
    @Enumerated(EnumType.STRING)
    private AppStatus status;

}
