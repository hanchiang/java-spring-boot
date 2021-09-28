package com.han.springapp.firstjavaproject.service.impl;

import com.han.springapp.firstjavaproject.exception.UserServiceException;
import com.han.springapp.firstjavaproject.io.entity.UserEntity;
import com.han.springapp.firstjavaproject.io.repository.UserRepository;
import com.han.springapp.firstjavaproject.service.UserService;
import com.han.springapp.firstjavaproject.shared.Utils;
import com.han.springapp.firstjavaproject.shared.dto.UserDto;
import com.han.springapp.firstjavaproject.ui.model.response.ErrorMessages;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

// https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/stereotype/Service.html
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    Utils utils;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserDto createUser(UserDto user) throws UserServiceException {
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new UserServiceException("Record already exists");
        }

        ModelMapper modelMapper = new ModelMapper();
        UserEntity userEntity = modelMapper.map(user, UserEntity.class);

        String publicUserId = utils.generateUserId(30);
        userEntity.setUserId(publicUserId);
        userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        UserEntity storedUser = userRepository.save(userEntity);

        UserDto retVal = modelMapper.map(storedUser, UserDto.class);

        return retVal;
    }

    public UserDto getUser(String email) {
        UserEntity userEntity = userRepository.findByEmail(email);
        if (userEntity == null) {
            throw new UsernameNotFoundException(email);
        }

        ModelMapper modelMapper = new ModelMapper();
        UserDto retVal = modelMapper.map(userEntity, UserDto.class);

        return retVal;
    }

    // Spring security framework will call this method, after attemptAuthentication()
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(email);
        if (userEntity == null) {
            throw new UsernameNotFoundException(email);
        }
        return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), new ArrayList<>());
    }

    public UserDto getUserByUserId(String userId) {

        UserEntity userEntity = userRepository.findByUserId(userId);
        if (userEntity == null) {
            throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
        }
        ModelMapper modelMapper = new ModelMapper();
        UserDto retVal = modelMapper.map(userEntity, UserDto.class);
        return retVal;
    }

    public UserDto updateUser(String userId, UserDto user) {

        UserEntity userEntity = userRepository.findByUserId(userId);
        if (userEntity == null) {
            throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
        }

        userEntity.setFirstName(user.getFirstName());
        userEntity.setLastName(user.getLastName());
        UserEntity updatedUser = userRepository.save(userEntity);

        ModelMapper modelMapper = new ModelMapper();
        UserDto retVal = modelMapper.map(updatedUser, UserDto.class);
        return retVal;
    }

    public void deleteUser(String userId) {
        UserEntity userEntity = userRepository.findByUserId(userId);
        if (userEntity == null) {
            throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
        }
        userRepository.delete(userEntity);
    }

    public List<UserDto> getUsers(int page, int limit) {
        List<UserDto> retVal = new ArrayList<>();

        Pageable pageableRequest = PageRequest.of(page, limit);
        Page<UserEntity> usersPage = userRepository.findAll(pageableRequest);
        List<UserEntity> users = usersPage.getContent();

        for (UserEntity userEntity: users) {
            ModelMapper modelMapper = new ModelMapper();
            UserDto userDto = modelMapper.map(userEntity, UserDto.class);
            retVal.add(userDto);
        }

        return retVal;
    }
}
