package com.cloud.secure.streaming.controllers.model.request;

import com.cloud.secure.streaming.common.enums.TeachingType;
import com.cloud.secure.streaming.common.utilities.ParamError;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateTeachingExperienceRequest {

    @Size(max = 255, message = ParamError.MAX_LENGTH)
    private String teachingPlace;

    private TeachingType teachingType;

    @Size(max = 255, message = ParamError.MAX_LENGTH)
    private String major;

    private String description;
}
