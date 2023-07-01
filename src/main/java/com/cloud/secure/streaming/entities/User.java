package com.cloud.secure.streaming.entities;

import com.cloud.secure.streaming.common.enums.AppStatus;
import com.cloud.secure.streaming.common.enums.CardType;
import com.cloud.secure.streaming.common.enums.Gender;
import com.cloud.secure.streaming.common.enums.UserType;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
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
@Table(name = "user")
public class User extends BaseEntity implements Serializable {

    @Id
    @Column(name = "id", nullable = false, length = 32, updatable = false)
    private String id;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "gender", columnDefinition = "varchar(32) default 'OTHER'")
    @Enumerated(EnumType.STRING)
    private Gender gender; // MALE, FEMALE , OTHER

    @Column(name = "country_code", length = 10)
    private String countryCode; // country phone number code

    @Column(name = "phone_dial_code", length = 10)
    private String phoneDialCode; // country phone number code

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "avatar", columnDefinition = "TEXT")
    private String avatar;

    @Column(name = "dob")
    private Date dob;  //Dob format MM/dd/yyyy

    @Column(name = "country")
    private String country;

    @Column(name = "city")
    private String city;

    @Column(name = "website")
    private String website;

    @Column(name = "twitter")
    private String twitter;

    @Column(name = "facebook")
    private String facebook;

    @Column(name = "linkedin")
    private String linkedin;

    @Column(name = "youtube")
    private String youtube;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, columnDefinition = "TEXT")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String passwordHash;

    @Column(nullable = false, length = 16)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String passwordSalt;

    @Column(nullable = false, columnDefinition = "varchar(32) default 'SYSTEM_ADMIN'")
    @Enumerated(EnumType.STRING)
    private UserType type;

    @Column(nullable = false, columnDefinition = "varchar(32) default 'ACTIVE'")
    @Enumerated(EnumType.STRING)
    private AppStatus status;

    @Column(name = "total_point", columnDefinition = "double default 0")
    private double totalPoint = 0;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "google_id", length = 64, unique = true)
    private String googleId;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "facebook_id", length = 64, unique = true)
    private String facebookId;

//    @Column(name = "stripe_customer_id", length = 64, unique = true)
//    private String stripeCustomerId;
//
//    @Column(name = "card_id")
//    private String cardId;
//
//    @Column(name = "card_number")
//    private String cardNumber; // stripe
//
//    @Column(name = "card_holder")
//    private String cardHolder; // stripe
//
//    @Column(name = "card_exp_date")
//    private String cardExpDate; // stripe
//
//    @Column(name = "card_token")
//    private String cardToken; // stripe
//
//    @Column(name = "card_type")
//    @Enumerated(EnumType.STRING)
//    private CardType cardType; // stripe

}
