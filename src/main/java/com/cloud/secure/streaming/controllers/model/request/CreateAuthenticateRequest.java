package com.cloud.secure.streaming.controllers.model.request;

import com.cloud.secure.streaming.common.enums.UserRole;
import com.cloud.secure.streaming.common.utilities.ParamError;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateAuthenticateRequest {

    @NotBlank(message = ParamError.FIELD_NAME)
    private String email;

    //mã hóa Chuỗi md5
    @NotBlank(message = ParamError.FIELD_NAME)
    private String password;

    @NotBlank(message = ParamError.FIELD_NAME)
    private boolean keepLoggedIn;

    @NotBlank(message = ParamError.FIELD_NAME)
    private UserRole userRole;

}
