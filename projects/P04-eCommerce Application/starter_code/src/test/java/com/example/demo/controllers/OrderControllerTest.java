package com.example.demo.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.example.demo.TestResourcesUtils;
import com.example.demo.TestUtils;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import java.math.BigDecimal;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class OrderControllerTest {

    private OrderController orderController;

    private UserRepository userRepo = mock(UserRepository.class);
    private OrderRepository orderRepo = mock(OrderRepository.class);

    @Before
    public void setUp() {
        orderController = new OrderController();
        TestUtils.injectObjects(orderController, "userRepository", userRepo);
        TestUtils.injectObjects(orderController, "orderRepository", orderRepo);
    }

    @Test
    public void submit_order_for_invalid_user() {
        final ResponseEntity<UserOrder> responseEntity = orderController.submit(null);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void submit_order_for_valid_user() {
        TestResourcesUtils createUserUtils = new TestResourcesUtils();
        User userForTest = createUserUtils.createUserForTest();
        when(userRepo.findByUsername(userForTest.getUsername())).thenReturn(userForTest);
        final ResponseEntity<UserOrder> responseEntity = orderController.submit("Test");

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        UserOrder order = responseEntity.getBody();
        assertNotNull(order);
        assertEquals(2, order.getItems().size());
        assertEquals(userForTest, order.getUser());
        assertEquals(BigDecimal.valueOf(112), order.getTotal());
    }


}