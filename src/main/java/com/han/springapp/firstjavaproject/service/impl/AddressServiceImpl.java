package com.han.springapp.firstjavaproject.service.impl;

import com.han.springapp.firstjavaproject.io.entity.AddressEntity;
import com.han.springapp.firstjavaproject.io.entity.UserEntity;
import com.han.springapp.firstjavaproject.io.repository.AddressRepository;
import com.han.springapp.firstjavaproject.io.repository.UserRepository;
import com.han.springapp.firstjavaproject.service.AddressService;
import com.han.springapp.firstjavaproject.shared.dto.AddressDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

// TODO: test
@Service
public class AddressServiceImpl implements AddressService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    AddressRepository addressRepository;

    @Transactional
    public List<AddressDto> getAddresses(String userId) {
        List<AddressDto> retVal = new ArrayList<>();

        UserEntity userEntity = userRepository.findByUserId(userId);
        if (userEntity == null) {
            return retVal;
        }

        ModelMapper modelMapper = new ModelMapper();
        for (AddressEntity addressEntity: userEntity.getAddresses()) {
            retVal.add(modelMapper.map(addressEntity, AddressDto.class));
        }
        return retVal;
    }

    @Transactional
    public AddressDto getAddress(String addressId) {
        AddressEntity addressEntity = addressRepository.findByAddressId(addressId);

        if (addressEntity != null) {
            ModelMapper modelMapper = new ModelMapper();
            return modelMapper.map(addressEntity, AddressDto.class);
        }

        return null;
    }
}
