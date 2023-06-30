package com.cloud.secure.streaming.controllers.model.request;

import com.cloud.secure.streaming.common.enums.ResourceType;
import com.cloud.secure.streaming.common.utilities.ParamError;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateResourceRequest {

    @Size(max = 124, message = ParamError.MAX_LENGTH)
    private String title;

    private String fileName;

    private String originalFileName;

    private ResourceType type;

    @Size(max = 255, message = ParamError.MAX_LENGTH)
    private String externalLink; // for EXTERNAL_RESOURCE
}
