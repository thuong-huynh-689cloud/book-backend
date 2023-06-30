package com.cloud.secure.streaming.controllers.model.response;

import com.cloud.secure.streaming.common.enums.ResourceType;
import com.cloud.secure.streaming.entities.Resource;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Date;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResourceResponse {

    private String id;
    private String title;
    private ResourceType type;
    private String fileName;
    private String originalFileName;
    private boolean downloadable;
    private String lectureId;
    private Date createdDate;
    private Date updatedDate;

    public ResourceResponse(Resource resource){
        this.id = resource.getId();
        this.title = resource.getTitle();
        this.type = resource.getType();
        this.fileName = resource.getFileName();
        this.originalFileName = resource.getOriginalFileName();
        this.downloadable = resource.isDownloadable();
        this.lectureId = resource.getLectureId();
        this.createdDate = resource.getCreatedDate();
        this.updatedDate = resource.getUpdatedDate();
    }
}
