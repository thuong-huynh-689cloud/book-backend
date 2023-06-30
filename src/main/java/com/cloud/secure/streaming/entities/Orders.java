package com.cloud.secure.streaming.entities;
import com.cloud.secure.streaming.common.enums.BuyType;
import com.cloud.secure.streaming.common.enums.OrderStatus;
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
@Table(name = "orders")
public class Orders extends BaseEntity implements Serializable {
    @Id
    @Column(name = "id", length = 32, nullable = false, updatable = false)
    private String id;

    @Column(name = "user_id", length = 32, nullable = false, updatable = false)
    private String userId;

    @Column(name = "total_point", columnDefinition = "double default 0")
    private double totalPoint;

    @Column(nullable = false,columnDefinition = "varchar(32) default 'ORDERED'")
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Column(nullable = false,columnDefinition = "varchar(32) default 'PURCHASED_COURSE'")
    @Enumerated(EnumType.STRING)
    private BuyType buyType;

}
