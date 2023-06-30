package com.cloud.secure.streaming.controllers.model.request;

import com.cloud.secure.streaming.common.enums.*;
import com.cloud.secure.streaming.common.utilities.ParamError;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;


import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateCourseRequest {

    @Size(max = 64, message = ParamError.MAX_LENGTH)
    private String title;

    @Size(max = 120, message = ParamError.MAX_LENGTH)
    private String subtitle;

    private String description;  // TEXT

    private Language language;

    private CourseLevel level;

    private String image; // TEXT

    private String promotionVideo;  // TEXT

    private List<String> categoryIds; // remove category old , add new category
}
