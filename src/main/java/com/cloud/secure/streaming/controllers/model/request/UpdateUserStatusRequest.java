package com.cloud.secure.streaming.controllers.model.request;

import com.cloud.secure.streaming.common.enums.AppStatus;
import com.cloud.secure.streaming.common.enums.FeedbackType;
import com.cloud.secure.streaming.common.utilities.ParamError;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateUserStatusRequest {

    @NotNull(message = ParamError.FIELD_NAME)
    private AppStatus status;

    private String feedback;
}
