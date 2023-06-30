package com.cloud.secure.streaming.services;

import com.cloud.secure.streaming.entities.PointPackage;
import com.cloud.secure.streaming.repositories.BuyPointRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 689Cloud
 */
@Service
public class PointPackageServiceImpl implements PointPackageService {
    final BuyPointRepository buyPointRepository;

    public PointPackageServiceImpl(BuyPointRepository buyPointRepository) {
        this.buyPointRepository = buyPointRepository;
    }

    @Override
    public PointPackage save(PointPackage pointPackage) {
        return buyPointRepository.save(pointPackage);
    }

    @Override
    public void delete(PointPackage pointPackage) {
        buyPointRepository.delete(pointPackage);
    }

    @Override
    public PointPackage getById(String id) {
        return buyPointRepository.findById(id).orElse(null);
    }

    @Override
    public List<PointPackage> getAll() {
        return buyPointRepository.findAll();
    }

    @Override
    public void deleteAllByIdIn(List<String> ids) {
        buyPointRepository.deleteAllByIdIn(ids);
    }
}
