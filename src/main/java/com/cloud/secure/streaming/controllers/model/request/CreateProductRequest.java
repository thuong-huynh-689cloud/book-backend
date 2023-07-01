package com.cloud.secure.streaming.controllers.model.request;

import com.cloud.secure.streaming.common.utilities.ParamError;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import javax.validation.constraints.*;
import java.util.List;


@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateProductRequest {


    @NotBlank(message = ParamError.FIELD_NAME)
    private String name;

//    @NotBlank(message = ParamError.FIELD_NAME)
//    private String userId;

    @NotBlank(message = ParamError.FIELD_NAME)
    private Double salePrice;

//    @NotBlank(message = ParamError.FIELD_NAME)
//    private String defaultImage;

    @NotBlank(message = ParamError.FIELD_NAME)
    @Max(value = 999999999,  message = ParamError.MAX_VALUE)
    @Min(value = 0, message = ParamError.MIN_VALUE)
    private Integer quantity;

    @NotBlank(message = ParamError.FIELD_NAME)
    private Integer rank;

    @NotBlank(message = ParamError.FIELD_NAME)
    private String sku;

    private String description;

    @NotBlank(message = ParamError.FIELD_NAME)
    private List<String> categoryIds;

}
