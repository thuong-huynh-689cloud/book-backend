package com.cloud.secure.streaming.controllers.model.request;

import com.cloud.secure.streaming.common.enums.IntendedLearnerType;
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
public class CreateIntendedLearnerRequest {

    private String id;

    private IntendedLearnerType type;

    @NotBlank
    @Size(max = 160, message = ParamError.MAX_LENGTH)
    private String sentence;
}
