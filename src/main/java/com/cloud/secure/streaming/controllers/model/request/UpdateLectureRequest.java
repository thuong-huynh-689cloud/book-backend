package com.cloud.secure.streaming.controllers.model.request;

import com.cloud.secure.streaming.common.enums.ContentType;
import com.cloud.secure.streaming.common.utilities.ParamError;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.sql.Time;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateLectureRequest {

    @Size(max = 64, message = ParamError.MAX_LENGTH)
    private String lectureName;

    private ContentType contentType;

    private String fileName;

    private String originalFileName;

    private Long fileSize;

    @Min(value = 1, message = ParamError.MIN_VALUE)
    @Max(value = 999, message = ParamError.MAX_VALUE)
    private Integer duration;

    @Min(value = 1, message = ParamError.MIN_VALUE)
    @Max(value = 999, message = ParamError.MAX_VALUE)
    private Integer numberOfQuestion;

    private Boolean downloadable = false;

    private Boolean publicView = false;
}
