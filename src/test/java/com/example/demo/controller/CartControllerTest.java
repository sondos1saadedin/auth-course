package com.example.demo.controller;


import com.example.demo.TestUtils;
import com.example.demo.controllers.CartController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest {
    private CartController cartController;

    private final UserRepository userRepo = mock(UserRepository.class);
    private final CartRepository cartRepo = mock(CartRepository.class);
    private final ItemRepository itemRepo = mock(ItemRepository.class);

    @Before
    public void setUp() {
        cartController = new CartController();
        TestUtils.injectObject(cartController, "userRepository", userRepo);
        TestUtils.injectObject(cartController, "cartRepository", cartRepo);
        TestUtils.injectObject(cartController, "itemRepository", itemRepo);
    }

    @Test
    public void addToCartHappyPath() {
        ModifyCartRequest request = new ModifyCartRequest();
        request.setItemId(0L);
        request.setQuantity(1);
        request.setUsername("Sondos");

        Cart cart = new Cart();
        cart.setId(0L);

        User user = new User();
        user.setUsername("Sondos");
        user.setCart(cart);
        cart.setUser(user);

        Item item = new Item();
        item.setId(0L);
        item.setName("New Test item");
        item.setPrice(BigDecimal.valueOf(20.00));

        when(userRepo.findByUsername(request.getUsername())).thenReturn(user);
        when(itemRepo.findById(item.getId())).thenReturn(Optional.of(item));

        final ResponseEntity<Cart> response = cartController.addTocart(request);
        Cart cart2 = response.getBody();

        assertNotNull(cart2);
        assertEquals("Sondos", cart2.getUser().getUsername());
        assertEquals(1, cart2.getItems().size());
    }

    @Test
    public void removeFromCartHappyPath() {
        ModifyCartRequest request = new ModifyCartRequest();
        request.setItemId(0L);
        request.setQuantity(1);
        request.setUsername("Sondos");

        Cart cart = new Cart();
        cart.setId(0L);

        User user = new User();
        user.setUsername("Sondos");
        user.setCart(cart);
        cart.setUser(user);

        Item item = new Item();
        item.setId(0L);
        item.setName("New item");
        item.setPrice(BigDecimal.valueOf(11.22));

        when(userRepo.findByUsername(request.getUsername())).thenReturn(user);
        when(itemRepo.findById(item.getId())).thenReturn(Optional.of(item));

        final ResponseEntity<Cart> response = cartController.removeFromcart(request);

        Cart cart2 = response.getBody();

        assertNotNull(cart2);
        assertEquals(0, cart2.getItems().size());
    }
}