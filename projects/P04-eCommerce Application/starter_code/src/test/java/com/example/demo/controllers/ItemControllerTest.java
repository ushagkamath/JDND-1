package com.example.demo.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.example.demo.TestResourcesUtils;
import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ItemControllerTest {

    private ItemController itemController;

    private ItemRepository itemRepo = mock(ItemRepository.class);

    TestResourcesUtils testResourcesUtils;

    @Before
    public void setUp(){
        itemController = new ItemController();
        TestUtils.injectObjects(itemController, "itemRepository", itemRepo);
        testResourcesUtils = new TestResourcesUtils();
    }

    @Test
    public void get_all_items() {
        when(itemRepo.findAll()).thenReturn(testResourcesUtils.testItemsList());
        final ResponseEntity<List<Item>> responseEntity = itemController.getItems();

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(2, responseEntity.getBody().size());
    }

    @Test
    public void get_item_by_id() {
        Item setItem = testResourcesUtils.getItem(0L, "TestItem1", "This is TestItem1", 1.88);
        when(itemRepo.findById(0L)).thenReturn(Optional
                .of(setItem));
        final ResponseEntity<Item> responseEntity = itemController.getItemById(0L);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Item testItem = responseEntity.getBody();
        assertNotNull(testItem);
        assertEquals(setItem, testItem);
    }

    @Test
    public void get_item_by_name() {
        Item setItem = testResourcesUtils.getItem(0L, "TestItem1", "This is TestItem1", 1.88);
        List setItems = new ArrayList();
        setItems.add(setItem);
        when(itemRepo.findByName("TestItem1")).thenReturn(setItems);
        final ResponseEntity<List<Item>> responseEntity = itemController.getItemsByName("TestItem1");

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        List<Item> testItems = responseEntity.getBody();
        assertNotNull(testItems);
        assertEquals(setItems, testItems);
    }

}
