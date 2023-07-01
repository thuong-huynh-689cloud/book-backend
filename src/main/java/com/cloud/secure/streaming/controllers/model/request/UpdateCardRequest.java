package com.cloud.secure.streaming.controllers.model.request;

import com.cloud.secure.streaming.common.enums.CardType;
import com.cloud.secure.streaming.common.utilities.ParamError;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateCardRequest {
    @NotBlank(message = ParamError.FIELD_NAME)
    private String cardNumber;

    @NotBlank(message = ParamError.FIELD_NAME)
    private String cardName;

    @NotBlank(message = ParamError.FIELD_NAME)
    private Date expiredDate;

    @Enumerated(EnumType.STRING)
    private CardType type;


}
