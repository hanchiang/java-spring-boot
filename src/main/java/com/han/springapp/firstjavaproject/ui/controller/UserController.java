package com.han.springapp.firstjavaproject.ui.controller;

import com.han.springapp.firstjavaproject.exception.UserServiceException;
import com.han.springapp.firstjavaproject.service.UserService;
import com.han.springapp.firstjavaproject.shared.dto.UserDto;
import com.han.springapp.firstjavaproject.ui.model.request.UserSignupRequest;
import com.han.springapp.firstjavaproject.ui.model.request.UserUpdateRequest;
import com.han.springapp.firstjavaproject.ui.model.response.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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
        UserDto userDto = userService.getUserByUserId(id);
        ModelMapper modelMapper = new ModelMapper();
        UserResponse retVal = modelMapper.map(userDto, UserResponse.class);

        return retVal;
    }

    @PostMapping(
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
            )
    public UserResponse createUser(@RequestBody UserSignupRequest userDetails) throws UserServiceException {
        if (
                userDetails.getFirstName() == null || userDetails.getFirstName().isEmpty() ||
                        userDetails.getLastName() == null || userDetails.getLastName().isEmpty() ||
                        userDetails.getEmail() == null || userDetails.getEmail().isEmpty() ||
                        userDetails.getPassword() == null || userDetails.getPassword().isEmpty()
        ) {
            throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());
        }

        ModelMapper modelMapper = new ModelMapper();
        UserDto userDto = modelMapper.map(userDetails, UserDto.class);

        UserDto createdUser = userService.createUser(userDto);
        UserResponse retVal = modelMapper.map(createdUser, UserResponse.class);

        return retVal;
    }

    @PutMapping(
            path="/{id}",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    public UserResponse updateUser(@PathVariable String id, @RequestBody UserUpdateRequest userDetails) {
        if (
                (userDetails.getFirstName() == null || userDetails.getFirstName().isEmpty()) &&
                        (userDetails.getLastName() == null || userDetails.getLastName().isEmpty())
        ) {
            throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());
        }

        ModelMapper modelMapper = new ModelMapper();
        UserDto userDto = modelMapper.map(userDetails, UserDto.class);

        UserDto updatedUser = userService.updateUser(id, userDto);
        UserResponse retVal = modelMapper.map(updatedUser, UserResponse.class);

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

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public List<UserResponse> getUsers(
            @RequestParam(value="page", defaultValue="0") int page,
            @RequestParam(value="limit", defaultValue="10") int limit
    ) {
        List<UserResponse> retVal = new ArrayList<>();

        if (page > 0) page--;

        List<UserDto> userDtos = userService.getUsers(page, limit);
        for (UserDto userDto: userDtos) {
            ModelMapper modelMapper = new ModelMapper();
            UserResponse userResponse = modelMapper.map(userDto, UserResponse.class);
            retVal.add(userResponse);
        }
        return retVal;
    }
}
