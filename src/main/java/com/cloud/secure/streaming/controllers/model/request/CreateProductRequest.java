package com.cloud.secure.streaming.controllers.model.request;

import com.cloud.secure.streaming.common.utilities.ParamError;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
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

    private String userId;

    private Double price;

    private Integer quantity;

    private String description;

    @NotEmpty(message = ParamError.FIELD_NAME)
    private List<String> categoryIds;
}
