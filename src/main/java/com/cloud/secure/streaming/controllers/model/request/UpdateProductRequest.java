package com.cloud.secure.streaming.controllers.model.request;

import com.cloud.secure.streaming.common.utilities.ParamError;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateProductRequest {

    private String name;

    private Double price;

    private String defaultImage;

    @Max(value = 999999999,  message = ParamError.MAX_VALUE)
    @Min(value = 0, message = ParamError.MIN_VALUE)
    private Integer quantity;

    private String description;

    private String image;

    private List<String> categoryIds;
}
