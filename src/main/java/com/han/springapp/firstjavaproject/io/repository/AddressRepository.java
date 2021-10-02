package com.han.springapp.firstjavaproject.io.repository;

import com.han.springapp.firstjavaproject.io.entity.AddressEntity;
import com.han.springapp.firstjavaproject.io.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends CrudRepository<AddressEntity, Long> {
    List<AddressEntity> findAllByUserDetails(UserEntity userEntity);
}
