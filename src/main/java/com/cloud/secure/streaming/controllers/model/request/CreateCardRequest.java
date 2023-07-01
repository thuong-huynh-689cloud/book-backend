package com.cloud.secure.streaming.controllers.model.request;

import com.cloud.secure.streaming.common.enums.CardType;
import com.cloud.secure.streaming.common.utilities.ParamError;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateCardRequest {
    @NotBlank(message = ParamError.FIELD_NAME)
    private String cardNumber;

    @NotBlank(message = ParamError.FIELD_NAME)
    private String cardName;

    @NotBlank(message = ParamError.FIELD_NAME)
    private Date expiredDate;

    @NotBlank(message = ParamError.FIELD_NAME)
    @Enumerated(EnumType.STRING)
    private CardType type;

    @NotBlank(message = ParamError.FIELD_NAME)
    private Double moneyInCard;

}
