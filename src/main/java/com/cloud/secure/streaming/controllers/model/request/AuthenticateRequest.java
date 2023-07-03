package com.cloud.secure.streaming.controllers.model.request;

import com.cloud.secure.streaming.common.enums.UserRole;
import com.cloud.secure.streaming.common.utilities.ParamError;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthenticateRequest {

    @NotBlank(message = ParamError.FIELD_NAME)
    private String authId; // username or email

    @NotBlank(message = ParamError.FIELD_NAME)
    private String passwordHash;

    private boolean keepLogin;

    private UserRole userRole;
}
