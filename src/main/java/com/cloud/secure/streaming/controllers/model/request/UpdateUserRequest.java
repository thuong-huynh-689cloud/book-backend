package com.cloud.secure.streaming.controllers.model.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import javax.validation.constraints.Max;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateUserRequest {

    @Max(value = 64)
    private String firstName;
    
    @Max(value = 64)
    private String lastName;

    private String address;

    private String phoneNumber;
}
