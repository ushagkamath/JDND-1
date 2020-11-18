package com.example.demo.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.example.demo.TestResourcesUtils;
import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class CartControllerTest {

    private CartController cartController;

    private UserRepository userRepo = mock(UserRepository.class);
    private CartRepository cartRepo = mock(CartRepository.class);
    private ItemRepository itemRepo = mock(ItemRepository.class);

    TestResourcesUtils testResourcesUtils;

    @Before
    public void setUp(){
        cartController = new CartController();
        TestUtils.injectObjects(cartController, "userRepository", userRepo);
        TestUtils.injectObjects(cartController, "cartRepository", cartRepo);
        TestUtils.injectObjects(cartController, "itemRepository", itemRepo);
        testResourcesUtils = new TestResourcesUtils();
    }

    @Test
    public void add_to_cart_invalid_user() {
        when(userRepo.findByUsername(anyString())).thenReturn(null);
        ModifyCartRequest request = cartRequestForTest();

        final ResponseEntity<Cart> responseEntity = cartController.addTocart(request);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void add_to_cart_invalid_item() {
        TestResourcesUtils createUserUtils = new TestResourcesUtils();
        User userForTest = createUserUtils.createUserForTest();
        ModifyCartRequest request = cartRequestForTest();
        when(userRepo.findByUsername("Test")).thenReturn(userForTest);
        final ResponseEntity<Cart> responseEntity = cartController.addTocart(request);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void add_to_cart_happy_path() {
        TestResourcesUtils createUserUtils = new TestResourcesUtils();
        User userForTest = createUserUtils.createUserForTest();
        ModifyCartRequest request = cartRequestForTest();
        when(userRepo.findByUsername("Test")).thenReturn(userForTest);
        when(itemRepo.findById(1L)).thenReturn(Optional.of(testResourcesUtils.getItem(1L, "testItem1","this is a test item", 2.4)));
        final ResponseEntity<Cart> responseEntity = cartController.addTocart(request);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Cart testCart = responseEntity.getBody();
        assertNotNull(testCart);
    }



    private ModifyCartRequest cartRequestForTest() {
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setItemId(1);
        modifyCartRequest.setQuantity(3);
        modifyCartRequest.setUsername("Test");
        return modifyCartRequest;
    }

}
