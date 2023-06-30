package com.cloud.secure.streaming.entities;

import com.cloud.secure.streaming.common.enums.CodeType;
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
@Table(name = "code")
public class Code extends BaseEntity implements Serializable {
    @Id
    @Column(name = "id", nullable = false, length = 32, updatable = false)
    private String id;

    @Column(name = "expire_date")
    private Date expireDate;

    @Column(name = "type", length = 16, nullable = false)
    @Enumerated(EnumType.STRING)
    private CodeType type;

    @Column(name = "user_id", length = 32, nullable = false)
    private String userId;

}
