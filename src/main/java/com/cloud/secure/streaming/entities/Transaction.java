package com.cloud.secure.streaming.entities;

import com.cloud.secure.streaming.common.enums.CurrencyType;
import com.cloud.secure.streaming.common.enums.TransactionStatus;
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
@Table(name = "transaction")
public class Transaction extends BaseEntity implements Serializable {

    @Id
    @Column(name = "id", nullable = false, length = 32, updatable = false)
    private String id;

    @Column(name = "user_id", length = 32)
    private String userId;

    @Column(name = "message")
    private String message;

    @Column(name = "point")
    private double point;

    @Column(name = "price")
    private double price;

    @Column(name = "currency_type")
    @Enumerated(EnumType.STRING)
    private CurrencyType currencyType;

    @Column(name = "charge_status")
    private String chargeStatus;

}
