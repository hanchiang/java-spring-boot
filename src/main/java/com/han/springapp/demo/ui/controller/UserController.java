package com.han.springapp.demo.ui.controller;

import com.han.springapp.demo.exception.UserServiceException;
import com.han.springapp.demo.service.UserService;
import com.han.springapp.demo.shared.dto.UserDto;
import com.han.springapp.demo.ui.model.request.UserSignupRequest;
import com.han.springapp.demo.ui.model.request.UserUpdateRequest;
import com.han.springapp.demo.ui.model.response.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * https://spring.io/guides/tutorials/rest/
 * https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/bind/annotation/RestController.html
 * Indicates that the data returned by each method will be written straight into the response body instead of rendering a template.
 */
@RestController
// https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/bind/annotation/RequestMapping.html
@RequestMapping("users") // http://localhost:8080/users
public class UserController {
    // https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/beans/factory/annotation/Autowired.html
    @Autowired
    UserService userService;

    // Default response type is XML
    @GetMapping(path="/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public UserResponse getUser(@PathVariable String id) {
        UserResponse retVal = new UserResponse();
        UserDto userDto = userService.getUserByUserId(id);
        BeanUtils.copyProperties(userDto, retVal);

        return retVal;
    }

    @PostMapping(
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
            )
    public UserResponse createUser(@RequestBody UserSignupRequest userDetails) throws UserServiceException {
        UserResponse retVal = new UserResponse();

        if (
                userDetails.getFirstName() == null || userDetails.getFirstName().isEmpty() ||
                        userDetails.getLastName() == null || userDetails.getLastName().isEmpty() ||
                        userDetails.getEmail() == null || userDetails.getEmail().isEmpty() ||
                        userDetails.getPassword() == null || userDetails.getPassword().isEmpty()
        ) {
            throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());
        }

        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userDetails, userDto);

        UserDto createdUser = userService.createUser(userDto);
        BeanUtils.copyProperties(createdUser, retVal);

        return retVal;
    }

    @PutMapping(
            path="/{id}",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    public UserResponse updateUser(@PathVariable String id, @RequestBody UserUpdateRequest userDetails) {
        UserResponse retVal = new UserResponse();

        if (
                (userDetails.getFirstName() == null || userDetails.getFirstName().isEmpty()) &&
                        (userDetails.getLastName() == null || userDetails.getLastName().isEmpty())
        ) {
            throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());
        }

        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userDetails, userDto);

        UserDto updatedUser = userService.updateUser(id, userDto);
        BeanUtils.copyProperties(updatedUser, retVal);

        return retVal;
    }

    @DeleteMapping(path = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public OperationStatusResponse deleteUser(@PathVariable String id) {
        OperationStatusResponse retVal = new OperationStatusResponse();

        retVal.setOperationName(OperationNames.DELETE.name());
        userService.deleteUser(id);

        retVal.setOperationResult(OperationStatus.SUCCESS.name());
        return retVal;
    }
}
