package com.cloud.secure.streaming.controllers.helper;

import com.cloud.secure.streaming.common.enums.ResourceType;
import com.cloud.secure.streaming.common.enums.UploadStatus;
import com.cloud.secure.streaming.common.utilities.DateUtil;
import com.cloud.secure.streaming.common.utilities.UniqueID;
import com.cloud.secure.streaming.controllers.model.request.CreateResourceRequest;
import com.cloud.secure.streaming.controllers.model.request.UpdateResourceRequest;
import com.cloud.secure.streaming.entities.Resource;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ResourceHelper {

    /**
     * Create Resource
     *
     * @param id
     * @param createResourceRequest
     * @return
     */
    public Resource createResource(String id, CreateResourceRequest createResourceRequest) {

        Resource resource = new Resource();
        // add id
        resource.setId(UniqueID.getUUID());
        // add title
        if (createResourceRequest.getTitle() != null && !createResourceRequest.getTitle().isEmpty()) {
            resource.setTitle(createResourceRequest.getTitle());
        }
        // add file name
        if (createResourceRequest.getFileName() != null && !createResourceRequest.getFileName().isEmpty()){
            resource.setFileName(createResourceRequest.getFileName());
        }
        // add original file name
        if (createResourceRequest.getOriginalFileName() != null && !createResourceRequest.getOriginalFileName().isEmpty()){
            resource.setOriginalFileName(createResourceRequest.getOriginalFileName());
        }
        // add type
        resource.setType(createResourceRequest.getType());
        // add external link
        if (createResourceRequest.getExternalLink() != null && !createResourceRequest.getExternalLink().isEmpty()) {
            resource.setExternalLink(createResourceRequest.getExternalLink());
        }
        // add upload status
        if (createResourceRequest.getType().equals(ResourceType.FILE)
                || createResourceRequest.getType().equals(ResourceType.SOURCE_CODE)) {
            resource.setUploadStatus(UploadStatus.SUCCESS);
        }
//        // add downloadable
//        resource.setDownloadable(createResourceRequest.getDownloadable());
        // add lecture id
        resource.setLectureId(id);
        // set created date
        resource.setCreatedDate(DateUtil.convertToUTC(new Date()));
        return resource;
    }

    /**
     * Update Resource
     *
     * @param resource
     * @param updateResourceRequest
     * @return
     */
    public Resource updateResource(Resource resource, UpdateResourceRequest updateResourceRequest) {
        // check Title
        if (updateResourceRequest.getTitle() != null && !updateResourceRequest.getTitle().trim().isEmpty() &&
                !updateResourceRequest.getTitle().trim().equals(resource.getTitle())) {
            resource.setTitle(updateResourceRequest.getTitle().trim());
        }
        // check File Name
        if (resource.getFileName() != null && !resource.getFileName().isEmpty()){
            if (updateResourceRequest.getFileName() != null && !updateResourceRequest.getFileName().trim().isEmpty() &&
                    !updateResourceRequest.getFileName().trim().equals(resource.getFileName())){
                resource.setFileName(updateResourceRequest.getFileName().trim());
            }
        }
        // check Original File Name
        if (resource.getOriginalFileName() != null && !resource.getOriginalFileName().isEmpty()){
            if (updateResourceRequest.getOriginalFileName() != null && !updateResourceRequest.getOriginalFileName().trim().isEmpty() &&
                    !updateResourceRequest.getOriginalFileName().trim().equals(resource.getOriginalFileName())){
                resource.setOriginalFileName(updateResourceRequest.getOriginalFileName().trim());
            }
        }
        // check Type
        if (updateResourceRequest.getType() != null &&
                !updateResourceRequest.getType().equals(resource.getType())) {
            resource.setType(updateResourceRequest.getType());
        }
        // check ExternalLink
        if (updateResourceRequest.getExternalLink() != null && !updateResourceRequest.getExternalLink().trim().isEmpty() &&
                !updateResourceRequest.getExternalLink().trim().equals(resource.getExternalLink())) {
            resource.setExternalLink(updateResourceRequest.getExternalLink().trim());
        }
//        // check downloadable
//        if (updateResourceRequest.getDownloadable() != null &&
//                !updateResourceRequest.getDownloadable().equals(resource.isDownloadable())) {
//            resource.setDownloadable(updateResourceRequest.getDownloadable());
//        }
        return resource;
    }
}
