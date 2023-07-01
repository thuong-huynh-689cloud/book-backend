package com.cloud.secure.streaming.entities;
import com.cloud.secure.streaming.common.enums.Status;
import com.cloud.secure.streaming.common.enums.UserRole;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;


@Data
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@EntityListeners(AuditingEntityListener.class)
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user")
public class User extends BaseEntity implements Serializable {

    @Id
    @Column(name = "id", nullable = false, length = 32, updatable = false)
    private String id;
    // Miss annotation
    @Column(name = "role", length = 32)
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Column(name = "email", length = 255)
    private String email;

    @Column(name = "first_name", length = 255)
    private String firstName;

    @Column(name = "last_name", length = 255)
    private String lastName;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "salt", length = 45)
    private String salt;

    @Column(name = "address", length = 255)
    private String address;

    @Column(name = "phone_number", length = 45)
    private String phoneNumber;

    @Column(nullable = false, columnDefinition = "TEXT")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String passwordHash;

    @Column(nullable = false, length = 16)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String passwordSalt;
}
