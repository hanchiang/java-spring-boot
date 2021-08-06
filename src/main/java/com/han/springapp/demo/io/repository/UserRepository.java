package com.han.springapp.demo.io.repository;

import com.han.springapp.demo.io.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * This class will persist entities to the database
 */
@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long> {
    /**
     * Need to follow the format: findBy<somefield>
     * @param email
     * @return
     */
    UserEntity findByEmail(String email);
}
