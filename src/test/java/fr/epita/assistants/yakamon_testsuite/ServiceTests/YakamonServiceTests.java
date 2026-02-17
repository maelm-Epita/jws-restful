package fr.epita.assistants.yakamon_testsuite.ServiceTests;

import fr.epita.assistants.yakamon.data.model.YakamonModel;
import fr.epita.assistants.yakamon.data.repository.YakadexEntryRepository;
import fr.epita.assistants.yakamon.domain.service.*;
import fr.epita.assistants.yakamon.utils.Item;
import fr.epita.assistants.yakamon.utils.tile.ItemType;
import fr.epita.assistants.yakamon.utils.tile.YakamonInfo;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import junit.framework.Assert;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@QuarkusTest
public class YakamonServiceTests {
    @Inject
    PlayerService playerService;
    @Inject
    GameService gameService;
    @Inject
    ItemService itemService;
    @Inject
    YakadexEntryService yakadexEntryService;
    @Inject
    YakamonService yakamonService;
    @Inject
    YakadexEntryRepository yakadexEntryRepository;

    String name = "mael";
    String mapPath = "/home/mael/School/ing1/s6/javapiscine/epita-ing-assistants-yaka-jws-2028-mael.minard/yakamon/src/test/resources/maps/testmap.epimap";

    @ConfigProperty(name = "JWS_TICK_DURATION") Long tickDuration;
    @ConfigProperty(name = "JWS_MOVEMENT_DELAY") Long moveDelay;

    @Transactional
    @BeforeEach
    public void setup() {
        playerService.reset();
        itemService.reset();
        gameService.reset();
        playerService.reset();
        yakamonService.reset();
        gameService.startGame(name, mapPath);
    }

    @Transactional
    @Test
    public void isTeamFullEmptyTest() {
        Assert.assertFalse(yakamonService.isTeamFull());
    }

    @Transactional
    @Test
    public void isTeamFullFullTest() {
        yakamonService.createYakamon(new YakamonInfo('y').withYakadexId(1));
        yakamonService.createYakamon(new YakamonInfo('y').withYakadexId(1));
        yakamonService.createYakamon(new YakamonInfo('y').withYakadexId(1));
        Assert.assertTrue(yakamonService.isTeamFull());
    }

    @Transactional
    @Test
    public void getWithUUIDPresentTest() {
        YakamonModel yakamon = yakamonService.createYakamon(new YakamonInfo('y').withYakadexId(1));
        YakamonModel actual = yakamonService.getWithUUID(yakamon.getUuid());
        Assert.assertEquals(yakamon, actual);
    }

    @Transactional
    @Test
    public void getWithUUIDNonExistingTest() {
        YakamonModel yakamon = yakamonService.createYakamon(new YakamonInfo('y').withYakadexId(1));
        YakamonModel actual = yakamonService.getWithUUID(null);
        Assert.assertNull(actual);

    }

    @Transactional
    @Test
    public void createYakamonTest() {
        YakamonModel yakamonModel = yakamonService.createYakamon(new YakamonInfo('y').withYakadexId(1));
        Assert.assertNotNull(yakamonModel);
        Assert.assertEquals(yakamonModel.getYakadexEntry().getName(), yakamonModel.getNickname());
        Assert.assertEquals(0, yakamonModel.getEnergyPoints().intValue());
    }

    @Transactional
    @Test
    public void renameYakamonValidTest() {
        String nickname = "rox";
        YakamonModel yakamonModel = yakamonService.createYakamon(new YakamonInfo('y').withYakadexId(1));
        yakamonService.renameYakamon(nickname, yakamonModel.getUuid());
        Assert.assertEquals(nickname, yakamonModel.getNickname());
    }

    @Transactional
    @Test
    public void renameYakamonNotExistTest() {
        String nickname = "rox";
        assertThrows(Exception.class, () -> yakamonService.renameYakamon(nickname, null));
    }

    @Transactional
    @Test
    public void feedYakamonValidTest() {
        YakamonModel yakamonModel = yakamonService.createYakamon(new YakamonInfo('y').withYakadexId(1));
        itemService.addItem(new Item(ItemType.SCROOGE, 1000));
        yakamonService.feedYakamon(100, yakamonModel.getUuid());
        Assert.assertEquals(100, yakamonModel.getEnergyPoints().intValue());
    }

    @Transactional
    @Test
    public void feedYakamonNotExistTest() {
        itemService.addItem(new Item(ItemType.SCROOGE, 1000));
        assertThrows(Exception.class, () -> yakamonService.feedYakamon(100, null));
    }

    @Transactional
    @Test
    public void feedYakamonNotEnoughScroogeTest() {
        YakamonModel yakamonModel = yakamonService.createYakamon(new YakamonInfo('y').withYakadexId(1));
        itemService.addItem(new Item(ItemType.SCROOGE, 1));
        assertThrows(Exception.class, () -> yakamonService.feedYakamon(10, yakamonModel.getUuid()));
    }

    @Transactional
    @Test
    public void feedYakamonDelayTest() {
        YakamonModel yakamonModel = yakamonService.createYakamon(new YakamonInfo('y').withYakadexId(1));
        itemService.addItem(new Item(ItemType.SCROOGE, 1000));
        yakamonService.feedYakamon(100, yakamonModel.getUuid());
        assertThrows(Exception.class, () -> yakamonService.feedYakamon(100, yakamonModel.getUuid()));
    }
    @Transactional
    @Test
    public void evolveYakamonValidTest() {
        YakamonModel yakamonModel = yakamonService.createYakamon(new YakamonInfo('y').withYakadexId(1));
        itemService.addItem(new Item(ItemType.SCROOGE, 1000));
        yakamonService.feedYakamon(100, yakamonModel.getUuid());
        yakamonService.evolveYakamon(yakamonModel.getUuid());
        Assert.assertTrue(yakamonModel.getEnergyPoints() < 100);
    }

    @Transactional
    @Test
    public void evolveYakamonNotExistTest() {
        assertThrows(Exception.class, () -> yakamonService.evolveYakamon(null));
    }

    @Transactional
    @Test
    public void evolveYakamonNotEnoughEnergyTest() {
        YakamonModel yakamonModel = yakamonService.createYakamon(new YakamonInfo('y').withYakadexId(1));
        itemService.addItem(new Item(ItemType.SCROOGE, 1));
        yakamonService.feedYakamon(1, yakamonModel.getUuid());
        assertThrows(Exception.class, () -> yakamonService.evolveYakamon(yakamonModel.getUuid()));
    }

    @Transactional
    @Test
    public void evoleYakamonNoEvolutionTest() {
        YakamonModel yakamonModel = yakamonService.createYakamon(new YakamonInfo('y').withYakadexId(1));
        yakamonModel.getYakadexEntry().setEvolution(null);
        itemService.addItem(new Item(ItemType.SCROOGE, 1000));
        yakamonService.feedYakamon(100, yakamonModel.getUuid());
        assertThrows(Exception.class, () -> yakamonService.evolveYakamon(yakamonModel.getUuid()));
    }
}
