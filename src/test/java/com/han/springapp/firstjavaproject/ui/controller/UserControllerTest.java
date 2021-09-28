package com.han.springapp.firstjavaproject.ui.controller;

import com.fasterxml.jackson.databind.util.BeanUtil;
import com.han.springapp.firstjavaproject.exception.UserServiceException;
import com.han.springapp.firstjavaproject.service.UserService;
import com.han.springapp.firstjavaproject.shared.dto.UserDto;
import com.han.springapp.firstjavaproject.ui.model.request.UserSignupRequest;
import com.han.springapp.firstjavaproject.ui.model.request.UserUpdateRequest;
import com.han.springapp.firstjavaproject.ui.model.response.OperationNames;
import com.han.springapp.firstjavaproject.ui.model.response.OperationStatus;
import com.han.springapp.firstjavaproject.ui.model.response.OperationStatusResponse;
import com.han.springapp.firstjavaproject.ui.model.response.UserResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

class UserControllerTest {
    @Mock
    UserService userService;

    @InjectMocks
    UserController userController;

    UserDto userDto;
    UserSignupRequest signupRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        userDto = new UserDto();
        userDto.setUserId("user id");
        userDto.setFirstName("first name");
        userDto.setLastName("last name");
        userDto.setEmail("email");

        signupRequest = new UserSignupRequest();
        signupRequest.setEmail("email");
        signupRequest.setFirstName("first name");
        signupRequest.setLastName("last name");
        signupRequest.setPassword("password");
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getUser() {
        when(userService.getUserByUserId(anyString())).thenReturn(userDto);

        UserResponse actualUserResponse = userController.getUser("user id");
        assertEquals(userDto.getUserId(), actualUserResponse.getUserId());
        assertEquals(userDto.getFirstName(), actualUserResponse.getFirstName());
        assertEquals(userDto.getLastName(), actualUserResponse.getLastName());
        assertEquals(userDto.getEmail(), actualUserResponse.getEmail());
    }

    @Test
    void createUser() {
        when(userService.createUser(any(UserDto.class))).thenReturn(userDto);

        UserResponse actualUserResponse = userController.createUser(signupRequest);
        assertEquals(userDto.getUserId(), actualUserResponse.getUserId());
        assertEquals(userDto.getFirstName(), actualUserResponse.getFirstName());
        assertEquals(userDto.getLastName(), actualUserResponse.getLastName());
        assertEquals(userDto.getEmail(), actualUserResponse.getEmail());
    }

    @Test
    void createUserFirstNameInvalid() {
        UserSignupRequest signupRequest = new UserSignupRequest();
        signupRequest.setEmail("email");
        signupRequest.setLastName("last name");
        signupRequest.setPassword("password");

        assertThrows(UserServiceException.class, () -> userController.createUser(signupRequest));

        signupRequest.setFirstName("");
        assertThrows(UserServiceException.class, () -> userController.createUser(signupRequest));
    }

    @Test
    void createUserLastNameInvalid() {
        UserSignupRequest signupRequest = new UserSignupRequest();
        signupRequest.setEmail("email");
        signupRequest.setFirstName("first name");
        signupRequest.setPassword("password");

        assertThrows(UserServiceException.class, () -> userController.createUser(signupRequest));

        signupRequest.setLastName("");
        assertThrows(UserServiceException.class, () -> userController.createUser(signupRequest));
    }

    @Test
    void createUserEmailInvalid() {
        UserSignupRequest signupRequest = new UserSignupRequest();
        signupRequest.setLastName("last name");
        signupRequest.setFirstName("first name");
        signupRequest.setPassword("password");

        assertThrows(UserServiceException.class, () -> userController.createUser(signupRequest));

        signupRequest.setEmail("");
        assertThrows(UserServiceException.class, () -> userController.createUser(signupRequest));
    }

    @Test
    void createUserPasswordInvalid() {
        UserSignupRequest signupRequest = new UserSignupRequest();
        signupRequest.setLastName("last name");
        signupRequest.setFirstName("first name");
        signupRequest.setEmail("email");

        assertThrows(UserServiceException.class, () -> userController.createUser(signupRequest));

        signupRequest.setPassword("");
        assertThrows(UserServiceException.class, () -> userController.createUser(signupRequest));
    }

    @Test
    void updateUser() {
        UserUpdateRequest userUpdateRequest = new UserUpdateRequest();
        userUpdateRequest.setFirstName("new first name");
        userUpdateRequest.setLastName("new last name");

        ModelMapper modelMapper = new ModelMapper();
        UserDto updatedUserDto = modelMapper.map(userDto, UserDto.class);
        updatedUserDto.setFirstName(userUpdateRequest.getFirstName());
        updatedUserDto.setLastName(userUpdateRequest.getLastName());

        when(userService.updateUser(anyString(), any(UserDto.class))).thenReturn(updatedUserDto);

        UserResponse actualUserResponse = userController.updateUser("user id", userUpdateRequest);
        assertEquals(updatedUserDto.getFirstName(), actualUserResponse.getFirstName());
        assertEquals(updatedUserDto.getLastName(), actualUserResponse.getLastName());
        assertEquals(updatedUserDto.getEmail(), actualUserResponse.getEmail());
        assertEquals(updatedUserDto.getUserId(), actualUserResponse.getUserId());
    }

    @Test
    void updateUserFirstNameLastNameInvalid() {
        UserUpdateRequest userUpdateRequest = new UserUpdateRequest();

        assertThrows(UserServiceException.class, () -> userController.updateUser("user id", userUpdateRequest));

        userUpdateRequest.setFirstName("");
        userUpdateRequest.setLastName("");
        assertThrows(UserServiceException.class, () -> userController.updateUser("user id", userUpdateRequest));
    }

    @Test
    void deleteUser() {
        Mockito.doNothing().when(userService).deleteUser(anyString());

        OperationStatusResponse expectedResponse = new OperationStatusResponse();
        expectedResponse.setOperationName(OperationNames.DELETE.name());
        expectedResponse.setOperationResult(OperationStatus.SUCCESS.name());

        OperationStatusResponse actualResponse = userController.deleteUser("user id");
        assertEquals(expectedResponse.getOperationName(), actualResponse.getOperationName());
        assertEquals(expectedResponse.getOperationResult(), actualResponse.getOperationResult());
    }

    @Test
    void getUsers() {
        List<UserDto> userDtos = getUserDtos();
        when(userService.getUsers(anyInt(), anyInt())).thenReturn(userDtos);

        List<UserResponse> userResponses = userController.getUsers(0, 10);

        assertEquals(userDtos.size(), userResponses.size());
        for (int i = 0; i < userResponses.size(); i++) {
            assertEquals(userDtos.get(i).getUserId(), userResponses.get(i).getUserId());
            assertEquals(userDtos.get(i).getFirstName(), userResponses.get(i).getFirstName());
            assertEquals(userDtos.get(i).getLastName(), userResponses.get(i).getLastName());
            assertEquals(userDtos.get(i).getEmail(), userResponses.get(i).getEmail());
        }
    }

    private List<UserDto> getUserDtos() {
        List<UserDto> userDtos = new ArrayList<>();
        userDtos.add(userDto);

        ModelMapper modelMapper = new ModelMapper();
        UserDto userDto2 = modelMapper.map(userDto, UserDto.class);
        userDto2.setUserId("user id 2");
        userDto2.setFirstName("first naem 2");
        userDto2.setLastName("last name 2");
        userDto2.setEmail("email 2");

        userDtos.add(userDto2);

        return userDtos;
    }
}