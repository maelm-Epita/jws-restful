package fr.epita.assistants.yakamon_testsuite.ServiceTests;

import fr.epita.assistants.yakamon.data.model.GameModel;
import fr.epita.assistants.yakamon.data.model.ItemModel;
import fr.epita.assistants.yakamon.data.model.PlayerModel;
import fr.epita.assistants.yakamon.data.repository.GameRepository;
import fr.epita.assistants.yakamon.domain.entity.GameEntity;
import fr.epita.assistants.yakamon.domain.service.GameService;
import fr.epita.assistants.yakamon.domain.service.ItemService;
import fr.epita.assistants.yakamon.domain.service.PlayerService;
import fr.epita.assistants.yakamon.utils.Map;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import junit.framework.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class GameServiceTests {
    @Inject
    GameService gameService;
    @Inject
    ItemService itemService;
    @Inject
    PlayerService playerService;

    String name = "mael";
    String mapPath = "/home/mael/School/ing1/s6/javapiscine/epita-ing-assistants-yaka-jws-2028-mael.minard/yakamon/src/main/resources/maps/walkable.epimap";

    @Transactional
    @BeforeEach
    public void setUp() {
        gameService.reset();
    }

    @Transactional
    @Test
    public void startGameTest() {
        try {
            gameService.startGame(name, mapPath);
        } catch (Exception e) {
            throw new RuntimeException("Invalid test config, map path not found");
        }
        GameEntity gameEntity = gameService.getGameEntity();
        GameModel gameModel = gameService.getGameRepository().getGame();
        Assert.assertNotNull(gameEntity);
        Assert.assertNotNull(gameEntity.getMap());
        Map map = gameEntity.getMap();
        Assert.assertNotNull(map.getTiles());
        Assert.assertNotNull(gameModel);
        Assert.assertNotNull(gameModel.getMap());
        Assert.assertTrue(itemService.hasYakaball());
        PlayerModel playerModel = playerService.getPlayer();
        Assert.assertNotNull(playerModel);
        Assert.assertEquals(name, playerModel.getName());
    }

    @Transactional
    @Test
    public void startGameTwiceTest() {
        try {
            gameService.startGame(name, mapPath);
            gameService.startGame(name, mapPath);
        } catch (Exception e) {
            throw new RuntimeException("Invalid test config, map path not found");
        }
        Assert.assertEquals(1, gameService.getGameRepository().listAll().size());
    }
}
