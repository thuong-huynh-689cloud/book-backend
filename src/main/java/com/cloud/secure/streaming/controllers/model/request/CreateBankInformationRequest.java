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


@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateBankInformationRequest {
    @Size(max = 32, message = ParamError.MAX_LENGTH)
    private String userId;

    @NotBlank
    @Size(max = 255, message = ParamError.MAX_LENGTH)
    private String bankName;

    @NotBlank
    @Size(max = 255, message = ParamError.MAX_LENGTH)
    private String branchName;

    @NotBlank
    @Size(max = 20, message = ParamError.MAX_LENGTH)
    private String accountNumber;

    @NotBlank
    @Size(max = 255, message = ParamError.MAX_LENGTH)
    private String accountHolder;
}
