package com.cloud.secure.streaming.controllers.model.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class SignUpFbResponse {
        private String id;
        private String firstName;
        private String lastName;
        private String email;
        private DataPictureReponse picture;


}
