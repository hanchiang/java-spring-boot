package com.han.springapp.demo.ui.controller;

import com.han.springapp.demo.service.UserService;
import com.han.springapp.demo.shared.dto.UserDto;
import com.han.springapp.demo.ui.model.request.UserSignupRequest;
import com.han.springapp.demo.ui.model.response.UserResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping(path="/{id}")
    public UserResponse getUser(@PathVariable String id) {
        UserResponse retVal = new UserResponse();
        UserDto userDto = userService.getUserByUserId(id);
        BeanUtils.copyProperties(userDto, retVal);

        return retVal;
    }

    @PostMapping
    public UserResponse createUser(@RequestBody UserSignupRequest userDetails) {
        UserResponse retVal = new UserResponse();

        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userDetails, userDto);

        UserDto createdUser = userService.createUser(userDto);
        BeanUtils.copyProperties(createdUser, retVal);

        return retVal;
    }

    @PutMapping
    public String updateUser() {
        return "update user was called";
    }

    @DeleteMapping
    public String deleteUser() {
        return "delete user was called";
    }
}
