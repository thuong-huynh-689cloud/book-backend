package com.cloud.secure.streaming.controllers.model.request;

import com.cloud.secure.streaming.common.enums.Gender;
import com.cloud.secure.streaming.common.enums.Language;
import com.cloud.secure.streaming.common.utilities.ParamError;
import com.cloud.secure.streaming.common.validator.NoXSS;
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
public class CreateSignUpLeanerRequest {

    @NotBlank(message = ParamError.FIELD_NAME)
    @Size(max = 255, message = ParamError.MAX_LENGTH)
    private String email;

    @NotBlank(message = ParamError.FIELD_NAME)
    @Size(max = 255, message = ParamError.MAX_LENGTH)
    @NoXSS
    private String name;

    @NotBlank(message = ParamError.FIELD_NAME)
    private String newPassword;

    @NotBlank(message = ParamError.FIELD_NAME)
    private String confirmNewPassword;

    private Gender gender;

    private String dob;

    @Size(max = 10, message = ParamError.MAX_LENGTH)
    private String countryCode;

    @Size(max = 20, message = ParamError.MAX_LENGTH)
    private String phone;

    private String phoneDialCode;

    private String avatar;

    @Size(max = 255, message = ParamError.MAX_LENGTH)
    private String city;

    @NoXSS
    private String description;

    private Language languageSendMail = Language.ENGLISH; // language to send mail

    @NotBlank(message = ParamError.FIELD_NAME)
    private String zoneId;
}
