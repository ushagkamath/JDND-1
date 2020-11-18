package com.example.demo.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.example.demo.TestResourcesUtils;
import com.example.demo.TestUtils;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class UserControllerTest {

    private UserController userController;

    private UserRepository userRepo = mock(UserRepository.class);
    private CartRepository cartRepo = mock(CartRepository.class);
    private BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setUp(){
        userController = new UserController();
        TestUtils.injectObjects(userController, "userRepository", userRepo);
        TestUtils.injectObjects(userController, "cartRepository", cartRepo);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", encoder);
    }

    @Test
    public void create_user_happy_path() {
        when(encoder.encode(anyString())).thenReturn("thisIsHashed");
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("Usha");
        createUserRequest.setPassword("WorksWell");
        createUserRequest.setConfirmPassword("WorksWell");

        final ResponseEntity<User> responseEntity = userController.createUser(createUserRequest);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        User user = responseEntity.getBody();
        assertNotNull(user);
        assertEquals(0, user.getId());
        assertEquals("Usha", user.getUsername());
        assertEquals("thisIsHashed", user.getPassword());
    }

    @Test
    public void create_user_password_complexity() {
        when(encoder.encode(anyString())).thenReturn("thisIsHashed");
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("Usha");
        createUserRequest.setPassword("Short");
        createUserRequest.setConfirmPassword("Short");

        final ResponseEntity<User> responseEntity = userController.createUser(createUserRequest);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void create_user_passwords_mismatch() {
        when(encoder.encode(anyString())).thenReturn("thisIsHashed");
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("Usha");
        createUserRequest.setPassword("WorksWell");
        createUserRequest.setConfirmPassword("WorkWell");

        final ResponseEntity<User> responseEntity = userController.createUser(createUserRequest);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void find_user_by_valid_name() {
        TestResourcesUtils createUserUtils = new TestResourcesUtils();
        User userForTest = createUserUtils.createUserForTest();
        when(userRepo.findByUsername(userForTest.getUsername())).thenReturn(userForTest);

        ResponseEntity<User> responseEntity = userController.findByUserName("Test");

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        User user = responseEntity.getBody();

        assertNotNull(user);
        assertEquals("Test", user.getUsername());
        assertEquals("thisIsHashed", user.getPassword());
    }

    @Test
    public void find_user_by_valid_id() {
        TestResourcesUtils createUserUtils = new TestResourcesUtils();
        User userForTest = createUserUtils.createUserForTest();
        when(userRepo.findById(anyLong())).thenReturn(Optional.of(userForTest));

        ResponseEntity<User> responseEntity = userController.findById(0L);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        User user = responseEntity.getBody();

        assertNotNull(user);
        assertEquals("Test", user.getUsername());
        assertEquals("thisIsHashed", user.getPassword());
    }

    @Test
    public void find_user_by_invalid_username() {
        when(userRepo.findByUsername("DoesNotExist")).thenReturn(null);
        ResponseEntity<User> responseEntity = userController.findByUserName("DoesNotExist");

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());

        User user = responseEntity.getBody();

        assertNull(user);
    }

}
