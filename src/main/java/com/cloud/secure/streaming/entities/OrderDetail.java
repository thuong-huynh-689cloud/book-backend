package com.cloud.secure.streaming.entities;

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
@Table(name = "order_detail")
public class OrderDetail extends BaseEntity implements Serializable {
    @Id
    @Column(name = "id", length = 32, nullable = false, updatable = false)
    private String id;

    @Column(name = "course_id", length = 32, nullable = false, updatable = false)
    private String courseId;

    @Column(name = "point", columnDefinition = "double default 0")
    private double point;

    @Column(name = "discount", columnDefinition = "double default 0")
    private double discount;

    @Column(name = "total_point", columnDefinition = "double default 0")
    private double totalPoint;

    @Column(name = "order_id", length = 32, nullable = false, updatable = false)
    private String orderId;
}
