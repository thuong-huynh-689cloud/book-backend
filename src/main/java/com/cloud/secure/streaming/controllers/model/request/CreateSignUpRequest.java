package com.cloud.secure.ecommerce.controllers.model.request;

import com.cloud.secure.ecommerce.common.utils.ParamError;
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

public class CreateSignUpRequest {

    @NotBlank(message = ParamError.FIELD_NAME)
    private String email;

    @NotBlank(message = ParamError.FIELD_NAME)
    private String passwordHash;

}
