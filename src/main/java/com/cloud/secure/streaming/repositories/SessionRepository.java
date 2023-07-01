package com.cloud.secure.ecommerce.repositories;

import com.cloud.secure.ecommerce.common.enums.Status;
import com.cloud.secure.ecommerce.entities.Session;
import com.cloud.secure.ecommerce.entities.User;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;


@Repository
@Transactional
public interface SessionRepository extends PagingAndSortingRepository<Session, String> {

    List<Session> findAllByUserId(String userId);


}
