package com.cloud.secure.streaming.controllers.model.request;

import com.cloud.secure.streaming.common.utilities.ParamError;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateCategoryRequest {

    @Size(max = 32, message = ParamError.MAX_LENGTH)
    private String parentId;
    
    @NotBlank
    @Size(max = 255, message = ParamError.MAX_LENGTH)
    private String nameEn;

    @NotBlank
    @Size(max = 255, message = ParamError.MAX_LENGTH)
    private String nameJa;

    @NotBlank
    @Size(max = 255, message = ParamError.MAX_LENGTH)
    private String nameVi;
}
