package com.cloud.secure.streaming.controllers.model.request;

import com.cloud.secure.streaming.common.enums.Gender;
import com.cloud.secure.streaming.common.enums.Language;
import com.cloud.secure.streaming.common.utilities.ParamError;
import com.cloud.secure.streaming.common.validator.NoXSS;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateUserRequest {

    @Size(max = 255, message = ParamError.MAX_LENGTH)
    @NoXSS
    private String name;

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

    private List<Language> languages;
}
