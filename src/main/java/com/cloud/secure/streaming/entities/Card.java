package com.cloud.secure.ecommerce.entities;

import com.cloud.secure.ecommerce.common.enums.CardType;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@EntityListeners(AuditingEntityListener.class)
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "card")
public class Card extends BaseEntity implements Serializable {

    @Id
    @Column(name = "id", nullable = false, length = 32, updatable = false)
    private String id;

    @Column(name = "user_id", length = 32)
    private String userId;

    @Column(name = "card_number", length = 255)
    private String cardNumber;

    @Column(name = "card_name", length = 255)
    private String cardName;

    @Column(name = "expired_date", length = 255)
    private Date expiredDate;

    @Column(name = "type", length = 32)
    @Enumerated(EnumType.STRING)
    private CardType type;

    @Column(name = "money_in_card", length = 32)
    private double moneyInCard;



}
