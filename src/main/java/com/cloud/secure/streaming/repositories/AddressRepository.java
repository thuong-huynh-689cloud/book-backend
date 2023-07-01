package com.cloud.secure.ecommerce.repositories;

import com.cloud.secure.ecommerce.entities.Address;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface AddressRepository extends PagingAndSortingRepository<Address, String> {

    List<Address> findAllByIdIn(List<String> ids);

    List<Address> findByUserId(String userId);

    Address findByIdAndUserId(String id, String userId);

}
