package com.cloud.secure.streaming.services;

import com.cloud.secure.streaming.entities.Resource;

import java.util.List;

/**
 * @author 689Cloud
 */
public interface ResourceService {

    Resource save(Resource resource);

    void delete(Resource resource);

    Resource getById(String id);

    void deleteResourceByIdIn(List<String> resourceId);

    List<Resource> getAllByIdIn(List<String> ids);

    List<Resource> getAllByLectureIdOrderByCreatedDateDesc(String lectureId);
}
