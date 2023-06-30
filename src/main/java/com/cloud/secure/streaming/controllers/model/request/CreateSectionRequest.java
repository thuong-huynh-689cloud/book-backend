package com.cloud.secure.streaming.controllers.model.request;

import com.cloud.secure.streaming.common.enums.DisplayStatus;
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
public class CreateSectionRequest {

    @NotBlank
    @Size(max = 32, message = ParamError.MAX_LENGTH)
    private String courseId;
    @NotBlank
    @Size(max = 64, message = ParamError.MAX_LENGTH)
    private String name;

    private DisplayStatus displayStatus;
}
