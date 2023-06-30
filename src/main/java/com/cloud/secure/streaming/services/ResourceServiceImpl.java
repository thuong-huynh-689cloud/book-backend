package com.cloud.secure.streaming.services;

import com.cloud.secure.streaming.entities.Resource;
import com.cloud.secure.streaming.repositories.ResourceRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResourceServiceImpl implements ResourceService {
    final ResourceRepository resourceRepository;

    public ResourceServiceImpl(ResourceRepository resourceRepository) {
        this.resourceRepository = resourceRepository;
    }

    @Override
    public Resource save(Resource resource) {
        return resourceRepository.save(resource);
    }

    @Override
    public void delete(Resource resource) {
        resourceRepository.delete(resource);
    }

    @Override
    public Resource getById(String id) {
        return resourceRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteResourceByIdIn(List<String> resourceId) {
        resourceRepository.deleteResourceByIdIn(resourceId);
    }

    @Override
    public List<Resource> getAllByIdIn(List<String> ids) {
        return resourceRepository.findAllByIdIn(ids);
    }

    @Override
    public List<Resource> getAllByLectureIdOrderByCreatedDateDesc(String lectureId) {
        return resourceRepository.findAllByLectureIdOrderByCreatedDateDesc(lectureId);
    }
}
