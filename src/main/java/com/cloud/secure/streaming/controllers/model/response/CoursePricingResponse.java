package com.cloud.secure.streaming.controllers.model.response;

import com.cloud.secure.streaming.common.enums.CourseInformationType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CoursePricingResponse {

    private String userId;
    private Double point;
    private Date createdDate;
    private CourseInformationType courseInformationType;
}
