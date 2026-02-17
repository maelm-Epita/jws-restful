package fr.epita.assistants.yakamon_testsuite.ServiceTests;

import fr.epita.assistants.yakamon.data.model.ItemModel;
import fr.epita.assistants.yakamon.domain.service.ItemService;
import fr.epita.assistants.yakamon.domain.service.YakamonService;
import fr.epita.assistants.yakamon.utils.Item;
import fr.epita.assistants.yakamon.utils.tile.ItemType;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import junit.framework.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@QuarkusTest
public class ItemServiceTests {
    @Inject
    ItemService itemService;

    @Transactional
    @BeforeEach
    public void setUp() {
        itemService.reset();
    }

    @Transactional
    @Test
    public void addItemNewTest() {
        itemService.addItem(new Item(ItemType.YAKABALL, 2));
        List<ItemModel> items = itemService.getItems();
        Assert.assertEquals(1, items.size());
        ItemModel item = items.getFirst();
        Assert.assertEquals(ItemType.YAKABALL, item.getType());
        Assert.assertEquals(2, item.getQuantity().intValue());
    }

    @Transactional
    @Test
    public void addItemExistingTest() {
        itemService.addItem(new Item(ItemType.YAKABALL, 2));
        itemService.addItem(new Item(ItemType.SCROOGE, 2));
        itemService.addItem(new Item(ItemType.YAKABALL, 4));
        List<ItemModel> items = itemService.getItems();
        Assert.assertEquals(2, items.size());
        ItemModel item = items.stream()
                .filter(itemModel -> itemModel.getType().equals(ItemType.YAKABALL)).toList().getFirst();
        Assert.assertEquals(ItemType.YAKABALL, item.getType());
        Assert.assertEquals(6, item.getQuantity().intValue());
        ItemModel item2 = items.stream()
                .filter(itemModel -> itemModel.getType().equals(ItemType.SCROOGE)).toList().getFirst();
        Assert.assertEquals(ItemType.SCROOGE, item2.getType());
        Assert.assertEquals(2, item2.getQuantity().intValue());
    }

    @Transactional
    @Test
    public void hasYakaballEmptyTest() {
        Assert.assertFalse(itemService.hasYakaball());
    }

    @Transactional
    @Test
    public void hasYakaballScroogeTest() {
        itemService.addItem(new Item(ItemType.SCROOGE, 3));
        Assert.assertFalse(itemService.hasYakaball());
    }

    @Transactional
    @Test
    public void hasYakaballOnlyTest() {
        itemService.addItem(new Item(ItemType.YAKABALL, 1));
        Assert.assertTrue(itemService.hasYakaball());
    }

    @Transactional
    @Test
    public void getScroogeWithQuantityEmptyTest() {
        ItemModel scrooge = itemService.getScroogeWithQuantity(0);
        Assert.assertNull(scrooge);
    }

    @Transactional
    @Test
    public void getScroogeWithQuantityNotEnoughTest() {
        itemService.addItem(new Item(ItemType.SCROOGE, 2));
        ItemModel scrooge = itemService.getScroogeWithQuantity(3);
        Assert.assertNull(scrooge);
    }

    @Transactional
    @Test
    public void getScroogeWithQuantityEnoughTest() {
        itemService.addItem(new Item(ItemType.SCROOGE, 2));
        ItemModel scrooge = itemService.getScroogeWithQuantity(2);
        Assert.assertNotNull(scrooge);
        itemService.addItem(new Item(ItemType.SCROOGE, 2));
        ItemModel scrooge2 = itemService.getScroogeWithQuantity(2);
        Assert.assertNotNull(scrooge2);
    }

    @Transactional
    @Test
    public void removeYakaballOneTest() {
        itemService.addItem(new Item(ItemType.YAKABALL, 2));
        itemService.removeYakaball(1);
        List<ItemModel> items = itemService.getItems();
        Assert.assertEquals(1, items.size());
        ItemModel item = items.getFirst();
        Assert.assertEquals(1, item.getQuantity().intValue());
    }

    @Transactional
    @Test
    public void removeYakaballEmptyTest() {
        itemService.removeYakaball(1);
        List<ItemModel> items = itemService.getItems();
        Assert.assertEquals(0, items.size());
    }

    @Transactional
    @Test
    public void removeYakaballMultipleTest() {
        itemService.addItem(new Item(ItemType.YAKABALL, 2));
        itemService.removeYakaball(3);
        List<ItemModel> items = itemService.getItems();
        Assert.assertEquals(1, items.size());
        ItemModel item = items.getFirst();
        Assert.assertEquals(0, item.getQuantity().intValue());
    }

    @Transactional
    @Test
    public void removeScroogeNotEnoughTest() {
        itemService.addItem(new Item(ItemType.SCROOGE, 3));
        assertThrows(Exception.class, ()->itemService.removeScrooge(4));
    }

    @Transactional
    @Test
    public void removeScroogeEnoughTest() {
        itemService.addItem(new Item(ItemType.SCROOGE, 4));
        assertDoesNotThrow(()->itemService.removeScrooge(4));
    }
}
