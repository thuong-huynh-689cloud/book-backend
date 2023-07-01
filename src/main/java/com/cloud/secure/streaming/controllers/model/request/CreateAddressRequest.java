package com.cloud.secure.streaming.controllers.model.request;

import com.cloud.secure.streaming.common.utilities.ParamError;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateAddressRequest {

    @NotBlank(message = ParamError.FIELD_NAME)
    private String city;

    @NotBlank(message = ParamError.FIELD_NAME)
    private String district;

    @NotBlank(message = ParamError.FIELD_NAME)
    private String address;

    @NotBlank(message = ParamError.FIELD_NAME)
    private String zipCode;

    @NotBlank(message = ParamError.FIELD_NAME)
    private Boolean defaultAddress;

    @NotBlank(message = ParamError.FIELD_NAME)
    private String country;

}
