package com.han.springapp.firstjavaproject.service.impl;

import com.han.springapp.firstjavaproject.exception.UserServiceException;
import com.han.springapp.firstjavaproject.io.entity.UserEntity;
import com.han.springapp.firstjavaproject.io.repository.UserRepository;
import com.han.springapp.firstjavaproject.shared.Utils;
import com.han.springapp.firstjavaproject.shared.dto.AddressDto;
import com.han.springapp.firstjavaproject.shared.dto.UserDto;
import com.han.springapp.firstjavaproject.ui.model.request.AddressSignupRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.*;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

class UserServiceImplTest {
    @Mock
    UserRepository userRepository;

    @Mock
    Utils utils;

    @Mock
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @InjectMocks
    UserServiceImpl userService;

    UserDto userDto = new UserDto();
    UserEntity userEntity = new UserEntity();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userDto = new UserDto();
        userDto.setFirstName("first name");
        userDto.setLastName("last name");
        userDto.setEmail("email");
        userDto.setPassword("password");

        userEntity = new UserEntity();
        userEntity.setUserId("user id");
        userEntity.setFirstName("first name");
        userEntity.setLastName("last name");
        userEntity.setEmail("email");
        userEntity.setEncryptedPassword("encrypted password");

        userDto.setAddresses(getAddressesDto());
    }

    @AfterEach
    void tearDown() {
    }

    List<AddressDto> getAddressesDto() {
        List<AddressDto> addresses = new ArrayList<>();
        AddressDto address1 = new AddressDto();
        address1.setAddressId("address 1 id");
        address1.setCity("Vancouver");
        address1.setCountry("Canada");
        address1.setStreetName("Street name");
        address1.setPostalCode("123456");
        address1.setType("billing");

        AddressDto address2 = new AddressDto();
        address2.setAddressId("address 2 id");
        address2.setCity("Vancouver");
        address2.setCountry("Canada");
        address2.setStreetName("Street name");
        address2.setPostalCode("123456");
        address2.setType("shipping");

        return addresses;
    }

    @Test
    void createUser() {
        when(userRepository.findByEmail(anyString())).thenReturn(null);
        when(utils.generateUserId(anyInt())).thenReturn("user id");
        when(bCryptPasswordEncoder.encode(anyString())).thenReturn("encrypted password");

        when(userRepository.save(any())).thenReturn(userEntity);

        UserDto outputUserDto = userService.createUser(userDto);
        assertEquals(userEntity.getUserId(), outputUserDto.getUserId());
        assertEquals(userEntity.getEncryptedPassword(), outputUserDto.getEncryptedPassword());
        assertEquals(userDto.getFirstName(), outputUserDto.getFirstName());
        assertEquals(userDto.getLastName(), outputUserDto.getLastName());
        assertEquals(userDto.getEmail(), outputUserDto.getEmail());
    }

    @Test
    void createUserAlreadyExist() {
        when(userRepository.findByEmail(anyString())).thenReturn(userEntity);
        assertThrows(UserServiceException.class, () -> userService.createUser(userDto));
    }

    @Test
    void getUser() {
        when(userRepository.findByEmail(anyString())).thenReturn(userEntity);

        UserDto userDto = userService.getUser("test@test.com");

        assertNotNull(userDto);
        assertEquals(userEntity.getUserId(), userDto.getUserId());
        assertEquals(userEntity.getFirstName(), userDto.getFirstName());
        assertEquals(userEntity.getLastName(), userDto.getLastName());
        assertEquals(userEntity.getEmail(), userDto.getEmail());
        assertEquals(userEntity.getEncryptedPassword(), userDto.getEncryptedPassword());
    }

    @Test
    void getUserNotFound() {
        when(userRepository.findByEmail(anyString())).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () -> userService.getUser("test@test.com"));
    }

    @Test
    void loadUserByUsername() {
        when(userRepository.findByEmail(anyString())).thenReturn(userEntity);
        UserDetails userDetails = userService.loadUserByUsername("email");

        assertEquals(User.class, userDetails.getClass());
        assertEquals(userEntity.getEmail(), userDetails.getUsername());
        assertEquals(userEntity.getEncryptedPassword(), userDetails.getPassword());
        assertIterableEquals(new ArrayList<>(), userDetails.getAuthorities());
    }

    @Test
    void loadUserByUsernameNotFound() {
        when(userRepository.findByEmail(anyString())).thenReturn(null);
        assertThrows(UsernameNotFoundException.class, () -> {
            userService.loadUserByUsername("email");
        });
    }

    @Test
    void getUserByUserId() {
        when(userRepository.findByUserId(anyString())).thenReturn(userEntity);

        UserDto actualDto = userService.getUserByUserId("email");
        assertEquals(userEntity.getUserId(), actualDto.getUserId());
        assertEquals(userEntity.getFirstName(), actualDto.getFirstName());
        assertEquals(userEntity.getLastName(), actualDto.getLastName());
        assertEquals(userEntity.getEmail(), actualDto.getEmail());
        assertEquals(userEntity.getEncryptedPassword(), actualDto.getEncryptedPassword());
    }

    @Test
    void getUserByUserIdNotFound() {
        when(userRepository.findByUserId(anyString())).thenReturn(null);

        assertThrows(UserServiceException.class, () -> {
            userService.getUserByUserId("email");
        });
    }

    @Test
    void updateUser() {
        when(userRepository.findByUserId(anyString())).thenReturn(userEntity);

        UserDto newUserDto = new UserDto();
        newUserDto.setFirstName("new first name");
        newUserDto.setLastName("new last name");

        ModelMapper modelMapper = new ModelMapper();
        UserEntity newUserEntity = modelMapper.map(userEntity, UserEntity.class);
        newUserEntity.setFirstName("new first name");
        newUserEntity.setLastName("new last name");
        when(userRepository.save(any())).thenReturn(newUserEntity);

        UserDto actualUserDto = userService.updateUser("user id", newUserDto);

        assertEquals(newUserDto.getFirstName(), actualUserDto.getFirstName());
        assertEquals(newUserDto.getLastName(), actualUserDto.getLastName());
        assertEquals(userEntity.getEmail(), actualUserDto.getEmail());
        assertEquals(userEntity.getUserId(), actualUserDto.getUserId());
        assertEquals(userEntity.getEncryptedPassword(), actualUserDto.getEncryptedPassword());
    }

    @Test
    void updateUserNotFound() {
        when(userRepository.findByUserId(anyString())).thenReturn(null);
        assertThrows(UserServiceException.class, () -> {
            userService.updateUser("user id", new UserDto());
        });
    }

    @Test
    void deleteUser() {
        when(userRepository.findByUserId(anyString())).thenReturn(userEntity);
        Mockito.doNothing().when(userRepository).delete(any(UserEntity.class));
    }

    @Test
    void deleteUserNotFound() {
        when(userRepository.findByUserId(anyString())).thenReturn(null);
        assertThrows(UserServiceException.class, () -> {
            userService.deleteUser("user id");
        });
    }

    @Test
    void getUsers() {
        List<UserEntity> userEntities = getUserEntities();
        Page<UserEntity> usersPage = new PageImpl<>(userEntities);

        when(userRepository.findAll(any(Pageable.class))).thenReturn(usersPage);
        List<UserDto> expectedUserDtos = getExpectedUserDtos();

        List<UserDto> actualUserDtos = userService.getUsers(1, 10);

        assertEquals(expectedUserDtos.size(), actualUserDtos.size());
        assertEquals(expectedUserDtos.get(0).getEmail(), actualUserDtos.get(0).getEmail());
        assertEquals(expectedUserDtos.get(0).getUserId(), actualUserDtos.get(0).getUserId());
        assertEquals(expectedUserDtos.get(0).getFirstName(), actualUserDtos.get(0).getFirstName());
        assertEquals(expectedUserDtos.get(0).getLastName(), actualUserDtos.get(0).getLastName());

        assertEquals(expectedUserDtos.get(1).getEmail(), actualUserDtos.get(1).getEmail());
        assertEquals(expectedUserDtos.get(1).getUserId(), actualUserDtos.get(1).getUserId());
        assertEquals(expectedUserDtos.get(1).getFirstName(), actualUserDtos.get(1).getFirstName());
        assertEquals(expectedUserDtos.get(1).getLastName(), actualUserDtos.get(1).getLastName());
    }


    private List<UserEntity> getUserEntities() {
        ModelMapper modelMapper = new ModelMapper();
        UserEntity userEntity2 = modelMapper.map(userEntity, UserEntity.class);
        return Arrays.asList(userEntity, userEntity2);
    }

    private List<UserDto> getExpectedUserDtos() {
        List<UserEntity> userEntities = getUserEntities();
        List<UserDto> expectedUserDtos = new ArrayList<>();

        ModelMapper modelMapper = new ModelMapper();
        UserDto expectedUserDto = modelMapper.map(userEntities.get(0), UserDto.class);
        expectedUserDtos.add(expectedUserDto);

        expectedUserDto = modelMapper.map(userEntities.get(1), UserDto.class);
        expectedUserDtos.add(expectedUserDto);

        return expectedUserDtos;
    }
}