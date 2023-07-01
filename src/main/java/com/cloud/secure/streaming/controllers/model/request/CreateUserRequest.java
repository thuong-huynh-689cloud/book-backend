package com.cloud.secure.ecommerce.controllers.model.request;

import com.cloud.secure.ecommerce.common.enums.UserRole;
import com.cloud.secure.ecommerce.common.utils.ParamError;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateUserRequest {

    @NotBlank(message = ParamError.FIELD_NAME)
    private UserRole role;

    @NotBlank(message = ParamError.FIELD_NAME)
    @Size(max = 255, message = ParamError.MAX_LENGTH)
    private String email;

    @NotBlank(message = ParamError.FIELD_NAME)
    @Size(max = 255, message = ParamError.MAX_LENGTH)
    private String fistName;

    @NotBlank(message = ParamError.FIELD_NAME)
    @Size(max = 255, message = ParamError.MAX_LENGTH)
    private String lastName;

    @NotBlank(message = ParamError.FIELD_NAME)
    private String newPassword;

    @NotBlank(message = ParamError.FIELD_NAME)
    private String confirmNewPassword;

    @NotBlank(message = ParamError.FIELD_NAME)
    private String address;

    @NotBlank(message = ParamError.FIELD_NAME)
    private String phoneNumber;

}
