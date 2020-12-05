package com.example.demo.controller;

import com.example.demo.TestUtils;
import com.example.demo.controllers.OrderController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {
    private OrderController orderController;

    private final UserRepository userRepo = mock(UserRepository.class);
    private final OrderRepository orderRepo = mock(OrderRepository.class);

    @Before
    public void setUp() {
        orderController = new OrderController();
        TestUtils.injectObject(orderController, "userRepository", userRepo);
        TestUtils.injectObject(orderController, "orderRepository", orderRepo);
    }

    @Test
    public void submitOrderHappyPath() {
        Cart cart = new Cart();
        cart.setId(0L);

        User user = new User();
        user.setUsername("Sondos");
        user.setCart(cart);
        cart.setUser(user);

        Item item = new Item();
        item.setId(0L);
        item.setName("Test item");
        item.setPrice(BigDecimal.valueOf(50.00));

        cart.addItem(item);

        when(userRepo.findByUsername(user.getUsername())).thenReturn(user);

        final ResponseEntity<UserOrder> response = orderController.submit(user.getUsername());
        UserOrder order = response.getBody();

        assertNotNull(order);
        assertEquals(BigDecimal.valueOf(50.00), order.getTotal());
    }
}