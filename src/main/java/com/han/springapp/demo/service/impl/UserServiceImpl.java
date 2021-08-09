package com.han.springapp.demo.service.impl;

import com.han.springapp.demo.exception.UserServiceException;
import com.han.springapp.demo.io.entity.UserEntity;
import com.han.springapp.demo.io.repository.UserRepository;
import com.han.springapp.demo.service.UserService;
import com.han.springapp.demo.shared.Utils;
import com.han.springapp.demo.shared.dto.UserDto;
import com.han.springapp.demo.ui.model.response.ErrorMessages;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

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

        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(user, userEntity);

        String publicUserId = utils.generateUserId(30);
        userEntity.setUserId(publicUserId);
        userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        UserEntity storedUser = userRepository.save(userEntity);
        UserDto retVal = new UserDto();
        BeanUtils.copyProperties(storedUser, retVal);

        return retVal;
    }

    public UserDto getUser(String email) {
        UserEntity userEntity = userRepository.findByEmail(email);
        if (userEntity == null) {
            throw new UsernameNotFoundException(email);
        }
        UserDto retVal = new UserDto();
        BeanUtils.copyProperties(userEntity, retVal);

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
        UserDto retVal = new UserDto();
        UserEntity userEntity = userRepository.findByUserId(userId);
        if (userEntity == null) {
            throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
        }
        BeanUtils.copyProperties(userEntity, retVal);
        return retVal;
    }

    public UserDto updateUser(String userId, UserDto user) {
        UserDto retVal = new UserDto();
        UserEntity userEntity = userRepository.findByUserId(userId);
        if (userEntity == null) {
            throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
        }

        userEntity.setFirstName(user.getFirstName());
        userEntity.setLastName(user.getLastName());
        UserEntity updatedUser = userRepository.save(userEntity);

        BeanUtils.copyProperties(updatedUser, retVal);
        return retVal;
    }

    public void deleteUser(String userId) {
        UserEntity userEntity = userRepository.findByUserId(userId);
        if (userEntity == null) {
            throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
        }
        userRepository.delete(userEntity);
    }
}
