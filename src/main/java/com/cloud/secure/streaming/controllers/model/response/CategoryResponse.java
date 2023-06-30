package com.cloud.secure.streaming.controllers.model.response;

import com.cloud.secure.streaming.entities.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryResponse {
    private String id;
    private String nameEn;
    private String nameJa;
    private String nameVi;
    private String parentId;
    private Date createdDate;
    private List<Category> categories;

    public CategoryResponse(Category category, List<Category> categories){
        this.id = category.getId();
        this.nameEn = category.getNameEn();
        this.nameJa = category.getNameJa();
        this.nameVi = category.getNameVi();
        this.createdDate = category.getCreatedDate();
        this.categories = categories;
    }
}
