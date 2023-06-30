package com.cloud.secure.streaming.entities;

import com.cloud.secure.streaming.common.enums.BuyType;
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
@Table(name = "point_history")
public class PointHistory extends BaseEntity implements Serializable {

    @Id
    @Column(name = "id", nullable = false, length = 32, updatable = false)
    private String id;

    @Column(name = "transaction_id")
    private String transactionId;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "point")
    private double point;

    @Column(name = "message")
    private String message;

    @Column(nullable = false,columnDefinition = "varchar(32) default 'PURCHASED_COURSE'")
    @Enumerated(EnumType.STRING)
    private BuyType buyType;

    @Column(name = "order_id", length = 32)
    private String orderId;

    @Column(name = "transaction_status", columnDefinition = "varchar(32) default 'PENDING'")
    @Enumerated(EnumType.STRING)
    private TransactionStatus transactionStatus;

    @Column(name = "total_point")
    private double totalPoint;


}
