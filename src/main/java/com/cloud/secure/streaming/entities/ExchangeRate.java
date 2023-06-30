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
@Table(name = "exchange_rate")
public class ExchangeRate extends BaseEntity implements Serializable {
    @Id
    @Column(name = "id", nullable = false, length = 32, updatable = false)
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "point", columnDefinition = "double default 0")
    private double point = 0;

    @Column(name = "exchanged_price", columnDefinition = "double default 0")
    private double exchangedPrice = 0;
}
