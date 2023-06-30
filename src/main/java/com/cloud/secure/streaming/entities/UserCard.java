package com.cloud.secure.streaming.entities;

import com.cloud.secure.streaming.common.enums.AppStatus;
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
@Table(name = "card")
public class UserCard extends BaseEntity implements Serializable {

    @Id
    @Column(name = "id", nullable = false, length = 32, updatable = false)
    private String id;

    @Column(name = "user_id", length = 32)
    private String userId;

    @Column(name = "stripe_card_id", length = 32)
    private String stripeCardId;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private AppStatus status;
}
