package fr.epita.assistants.yakamon_testsuite.RepositoryTests;

import fr.epita.assistants.yakamon.data.model.GameModel;
import fr.epita.assistants.yakamon.data.repository.GameRepository;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import junit.framework.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class GameRepositoryTests {

    @Inject
    GameRepository gameRepository;

    @BeforeEach
    @Transactional
    public void setUp() {
        gameRepository.deleteAll();
    }

    @Test
    @Transactional
    public void hasGameTest() {
        Assert.assertFalse(gameRepository.hasGame());
        GameModel gameModel = new GameModel();
        gameRepository.persist(gameModel);
        Assert.assertTrue(gameRepository.hasGame());
    }

    @Test
    @Transactional
    public void getGameTest() {
        GameModel gameModel = new GameModel();
        gameRepository.persist(gameModel);
        GameModel got = gameRepository.getGame();
        Assert.assertEquals(gameModel, got);
    }
}
