package fr.epita.assistants.yakamon_testsuite.ServiceTests;

import fr.epita.assistants.yakamon.data.model.PlayerModel;
import fr.epita.assistants.yakamon.data.model.YakadexEntryModel;
import fr.epita.assistants.yakamon.data.model.YakamonModel;
import fr.epita.assistants.yakamon.data.repository.YakadexEntryRepository;
import fr.epita.assistants.yakamon.domain.service.*;
import fr.epita.assistants.yakamon.utils.Direction;
import fr.epita.assistants.yakamon.utils.ElementType;
import fr.epita.assistants.yakamon.utils.Point;
import fr.epita.assistants.yakamon.utils.tile.TileType;
import fr.epita.assistants.yakamon.utils.tile.YakamonInfo;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import junit.framework.Assert;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class PlayerServiceTests {
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
    }

    @Transactional
    @Test
    public void createPlayerTest() {
        playerService.createPlayer(name);
        PlayerModel playerModel = playerService.getPlayer();
        Assert.assertNotNull(playerModel);
        Assert.assertEquals(name, playerModel.getName());
        Assert.assertEquals(0, playerModel.getPosX().intValue());
        Assert.assertEquals(0, playerModel.getPosY().intValue());
        Assert.assertNull(playerModel.getLastCatch());
        Assert.assertNull(playerModel.getLastFeed());
        Assert.assertNull(playerModel.getLastMove());
        Assert.assertNull(playerModel.getLastCollect());
    }

    @Transactional
    @Test
    public void isDelayValidTest() {
        Long delay = 2L;
        playerService.createPlayer(name);
        PlayerModel playerModel = playerService.getPlayer();
        LocalDateTime localDateTime = LocalDateTime.now();
        Assert.assertTrue(playerService.isDelayValid(localDateTime, playerModel.getLastMove(), delay));
        playerModel.setLastMove(localDateTime);
        Assert.assertFalse(playerService.isDelayValid(localDateTime, playerModel.getLastMove(), delay));
        try {
            Thread.sleep(tickDuration * delay);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        localDateTime = LocalDateTime.now();
        Assert.assertTrue(playerService.isDelayValid(localDateTime, playerModel.getLastMove(), delay));
    }

    @Transactional
    @Test
    public void movePlayerValidTest() {
        gameService.startGame(name, mapPath);
        AtomicReference<Point> point = new AtomicReference<>();
        assertDoesNotThrow(() -> point.set(playerService.movePlayer(Direction.DOWN)));
        Assert.assertEquals(new Point(0, 1), point.get());
    }

    @Transactional
    @Test
    public void movePlayerDelayTest() {
        gameService.startGame(name, mapPath);
        playerService.movePlayer(Direction.DOWN);
        assertThrows(Exception.class, () -> playerService.movePlayer(Direction.UP));
    }

    @Transactional
    @Test
    public void movePlayerInvalidTest() {
        gameService.startGame(name, mapPath);
        assertThrows(Exception.class, () -> playerService.movePlayer(Direction.UP));
    }

    @Transactional
    @Test
    public void catchYakamonValidTest() {
        gameService.startGame(name, mapPath);
        YakamonModel yakamonModel = playerService.catchYakamon();
        Assert.assertNotNull(yakamonModel);
    }

    @Transactional
    @Test
    public void catchYakamonNotYakamonTest() {
        gameService.startGame(name, mapPath);
        playerService.movePlayer(Direction.RIGHT);
        assertThrows(Exception.class, () -> playerService.catchYakamon());
    }

    @Transactional
    @Test
    public void catchYakamonNoYakaballTest() {
        gameService.startGame(name, mapPath);
        itemService.reset();
        assertThrows(Exception.class, () -> playerService.catchYakamon());
    }

    @Transactional
    @Test
    public void catchYakamonTeamFullTest() {
        gameService.startGame(name, mapPath);
        yakamonService.createYakamon(new YakamonInfo('y').withYakadexId(1));
        yakamonService.createYakamon(new YakamonInfo('y').withYakadexId(1));
        yakamonService.createYakamon(new YakamonInfo('y').withYakadexId(1));
        assertThrows(Exception.class, () -> playerService.catchYakamon());
    }

    @Transactional
    @Test
    public void catchYakamonDelayTest() {
        gameService.startGame(name, mapPath);
        playerService.catchYakamon();
        assertThrows(Exception.class, () -> playerService.catchYakamon());
    }

    @Transactional
    @Test
    public void collectItemValidScroogeTest() {
        gameService.startGame(name, mapPath);
        playerService.movePlayer(Direction.DOWN);
        TileType tileType = playerService.collectItem();
        Assert.assertNotNull(tileType);
    }

    @Transactional
    @Test
    public void collectItemValidYakaballTest() {
        gameService.startGame(name, mapPath);
        playerService.movePlayer(Direction.RIGHT);
        TileType tileType = playerService.collectItem();
        Assert.assertNotNull(tileType);
    }

    @Transactional
    @Test
    public void collectItemNTest() {
        gameService.startGame(name, mapPath);
        playerService.movePlayer(Direction.RIGHT);
        try {
            Thread.sleep(moveDelay * tickDuration);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        playerService.movePlayer(Direction.RIGHT);
        assertThrows(Exception.class, () -> playerService.collectItem());
    }

    @Transactional
    @Test
    public void collectItemNotItemTest() {
        gameService.startGame(name, mapPath);
        assertThrows(Exception.class, () -> playerService.collectItem());
    }

    @Transactional
    @Test
    public void collectItemDelayTest() {
        gameService.startGame(name, mapPath);
        playerService.movePlayer(Direction.RIGHT);
        playerService.collectItem();
        assertThrows(Exception.class, () -> playerService.collectItem());
    }
}
