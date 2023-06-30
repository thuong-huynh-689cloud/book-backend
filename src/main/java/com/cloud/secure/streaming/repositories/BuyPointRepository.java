package com.cloud.secure.streaming.repositories;
import com.cloud.secure.streaming.entities.PointPackage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author 689Cloud
 */
@Repository
@Transactional
public interface BuyPointRepository extends JpaRepository<PointPackage, String> {

    void deleteAllByIdIn(List<String> ids);
}
