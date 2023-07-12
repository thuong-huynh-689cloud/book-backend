package com.cloud.secure.streaming.controllers.model.request;

import com.cloud.secure.streaming.common.enums.UserRole;
import com.cloud.secure.streaming.common.utilities.ParamError;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

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

    private String fistName;

    private String lastName;

    @NotBlank(message = ParamError.FIELD_NAME)
    private String newPassword;

    @NotBlank(message = ParamError.FIELD_NAME)
    private String confirmNewPassword;

    private String address;

    private String phoneNumber;
}
