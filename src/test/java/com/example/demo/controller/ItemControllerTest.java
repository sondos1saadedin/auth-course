package com.example.demo.controller;

import com.example.demo.TestUtils;
import com.example.demo.controllers.ItemController;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {
    private ItemController itemController;
    private final ItemRepository itemRepo = mock(ItemRepository.class);

    @Before
    public void setUp() {
        itemController = new ItemController();
        TestUtils.injectObject(itemController, "itemRepository", itemRepo);
    }

    @Test
    public void getItemByIdHappyPath() {
        Item item = new Item();
        item.setId(0L);
        item.setPrice(BigDecimal.valueOf(150.00));
        item.setName("A B C");
        item.setDescription("ABCD");

        when(itemRepo.findById(0L)).thenReturn(Optional.of(item));
        ResponseEntity<Item> response = itemController.getItemById(0L);

        Item responseItem = response.getBody();

        assert responseItem != null;
        assertEquals(BigDecimal.valueOf(156.78), responseItem.getPrice());
        assertEquals("A B C", responseItem.getName());
        assertEquals("ABCD", responseItem.getDescription());
    }

    @Test
    public void getItemsHappyPath() {
        List<Item> items = new ArrayList<>();

        Item item = new Item();
        item.setId(0L);
        item.setPrice(BigDecimal.valueOf(150.99));
        item.setName("A B");
        item.setDescription("ABC");

        Item item2 = new Item();
        item2.setId(1L);
        item2.setPrice(BigDecimal.valueOf(150.00));
        item2.setName("A B C");
        item2.setDescription("ABCD");

        Item item3 = new Item();
        item3.setId(1L);
        item3.setPrice(BigDecimal.valueOf(133.09));
        item3.setName("XYZ Z");
        item3.setDescription("XYZE");

        items.add(item);
        items.add(item2);
        items.add(item3);

        when(itemRepo.findAll()).thenReturn(items);
        ResponseEntity<List<Item>> response = itemController.getItems();
        List<Item> itemsResponse = response.getBody();

        assertNotNull(itemsResponse);
        assertEquals(3, itemsResponse.size());
    }
}