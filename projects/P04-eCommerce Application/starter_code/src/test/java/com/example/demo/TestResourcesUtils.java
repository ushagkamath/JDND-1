/*
 * Copyright 2018 VMware, Inc.
 * All rights reserved.
 */

package com.example.demo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.example.demo.controllers.UserController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class TestResourcesUtils {

    private UserController userController;

    private UserRepository userRepo = mock(UserRepository.class);
    private CartRepository cartRepo = mock(CartRepository.class);
    private BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);

    private void userControllerSetUp() {
        userController = new UserController();
        TestUtils.injectObjects(userController, "userRepository", userRepo);
        TestUtils.injectObjects(userController, "cartRepository", cartRepo);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", encoder);
    }

    public User createUserForTest(){
        userControllerSetUp();
        when(encoder.encode(anyString())).thenReturn("thisIsHashed");
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("Test");
        createUserRequest.setPassword("TestPassword");
        createUserRequest.setConfirmPassword("TestPassword");
        User testUser = userController.createUser(createUserRequest).getBody();
        testUser.setCart(setNewCart(testUser));
        return testUser;
    }

    private Cart setNewCart(User testUser) {
        Cart cart = new Cart();
        cart.setId(1L);
        cart.setItems(testItemsList());
        cart.setUser(testUser);
        cart.setTotal(BigDecimal.valueOf(112L));
        return cart;
    }


    public List<Item> testItemsList() {
        List<Item> itemsList = new ArrayList<>();
        Item item1 = getItem(0L, "TestItem1", "This is TestItem1", 1.88);
        Item item2 = getItem(1L, "TestItem2", "This is TestItem2", 2.5);
        itemsList.add(item1);
        itemsList.add(item2);
        return itemsList;
    }

    public Item getItem(long l, String testItem1, String s, double v) {
        Item item1 = new Item();
        item1.setId(l);
        item1.setName(testItem1);
        item1.setDescription(s);
        item1.setPrice(BigDecimal.valueOf(v));
        return item1;
    }
}
