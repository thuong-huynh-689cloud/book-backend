package com.cloud.secure.streaming.services;

import com.cloud.secure.streaming.entities.PointPackage;

import java.util.List;

/**
 * @author 689Cloud
 */
public interface PointPackageService {

    PointPackage save(PointPackage pointPackage);

    void delete(PointPackage pointPackage);

    PointPackage getById(String id);

    List<PointPackage> getAll();

    void deleteAllByIdIn(List<String> ids);
}
