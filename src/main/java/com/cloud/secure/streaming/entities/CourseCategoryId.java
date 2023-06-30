package com.cloud.secure.streaming.entities;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class CourseCategoryId implements Serializable {

    @Column(name = "course_id", nullable = false, length = 32)
    private String courseId;

    @Column(name = "category_id", nullable = false, length = 32)
    private String categoryId;
}
