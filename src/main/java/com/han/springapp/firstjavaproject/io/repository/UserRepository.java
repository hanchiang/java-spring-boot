package com.han.springapp.firstjavaproject.io.repository;

import com.han.springapp.firstjavaproject.io.entity.UserEntity;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * This class will persist entities to the database
 */
@Repository
public interface UserRepository extends PagingAndSortingRepository<UserEntity, Long> {
    // Methods need to be named according to the format: findBy<field>
    UserEntity findByEmail(String email);
    UserEntity findByUserId(String userId);
}
