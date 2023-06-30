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

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateExchangeRateRequest {

    private String name;

    @Min(value = 0, message = ParamError.MIN_VALUE)
    private Double point;

    @Min(value = 0, message = ParamError.MIN_VALUE)
    @Max(value = 100 ,message = ParamError.MAX_VALUE)
    private Double exchangedPrice;
}
