package com.cloud.secure.ecommerce.entities;

import com.cloud.secure.ecommerce.common.enums.ResetCodeType;
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
@Table(name = "resetCode")
public class ResetCode extends BaseEntity implements Serializable {
    @Id
    @Column(name = "id", nullable = false, length = 255, updatable = false)
    private String id;

    @Column(name = "expire_date")
    private Date expireDate ;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private ResetCodeType type;

    @Column(name = "user_id")
    private String userId ;

}
