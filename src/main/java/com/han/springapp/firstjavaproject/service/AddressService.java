package com.han.springapp.firstjavaproject.service;

import com.han.springapp.firstjavaproject.shared.dto.AddressDto;

import java.util.List;

public interface AddressService {
    List<AddressDto> getAddresses(String userId);
    AddressDto getAddress(String addressId);
}
