package com.han.springapp.firstjavaproject.io.repository;

import com.han.springapp.firstjavaproject.io.entity.AddressEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends CrudRepository<AddressEntity, Long> {
    AddressEntity findByAddressId(String addressId);
}
