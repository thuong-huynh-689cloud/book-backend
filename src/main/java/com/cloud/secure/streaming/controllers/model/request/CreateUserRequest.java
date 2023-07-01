package com.cloud.secure.streaming.controllers.model.request;

import com.cloud.secure.streaming.common.enums.Gender;
import com.cloud.secure.streaming.common.enums.Language;
import com.cloud.secure.streaming.common.enums.UserType;
import com.cloud.secure.streaming.common.utilities.ParamError;
import com.cloud.secure.streaming.common.validator.NoXSS;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateUserRequest {

    @NotBlank(message = ParamError.FIELD_NAME)
    @Size(max = 255, message = ParamError.MAX_LENGTH)
    private String email;

    @NotBlank(message = ParamError.FIELD_NAME)
    @Size(max = 255, message = ParamError.MAX_LENGTH)

    @NoXSS
    private String name;

    private String newPassword;

    private String confirmNewPassword;

    private Gender gender;

    @Size(max = 10, message = ParamError.MAX_LENGTH)
    private String countryCode;

    @Size(max = 10, message = ParamError.MAX_LENGTH)
    private String phoneDialCode;

    @Size(max = 20, message = ParamError.MAX_LENGTH)
    private String phone;

    private String avatar;

    private String dob;

    @Size(max = 255, message = ParamError.MAX_LENGTH)
    private String country;

    @Size(max = 255, message = ParamError.MAX_LENGTH)
    private String city;

    @Size(max = 255, message = ParamError.MAX_LENGTH)
    private String website;

    @Size(max = 255, message = ParamError.MAX_LENGTH)
    private String twitter;

    @Size(max = 255, message = ParamError.MAX_LENGTH)
    private String facebook;

    @Size(max = 255, message = ParamError.MAX_LENGTH)
    private String linkedin;

    @Size(max = 255, message = ParamError.MAX_LENGTH)
    private String youtube;

    private String description;

    private UserType type = UserType.SYSTEM_ADMIN;

    private Language languageSendMail = Language.ENGLISH; // language to send mail

    private List<Language> languages; // user languages
}
