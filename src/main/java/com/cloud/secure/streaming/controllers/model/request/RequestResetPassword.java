package com.cloud.secure.streaming.controllers.model.request;

import com.cloud.secure.streaming.common.enums.Language;
import com.cloud.secure.streaming.common.enums.UserType;
import com.cloud.secure.streaming.common.utilities.ParamError;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RequestResetPassword {

    @NotBlank(message = ParamError.FIELD_NAME)
    private String authId; // username or email

    private Language language; // language for mail template

    private UserType userType;
}
