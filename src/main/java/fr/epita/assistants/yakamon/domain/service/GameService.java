package fr.epita.assistants.yakamon.domain.service;

import fr.epita.assistants.yakamon.converter.GameConverter;
import fr.epita.assistants.yakamon.data.model.GameModel;
import fr.epita.assistants.yakamon.data.repository.GameRepository;
import fr.epita.assistants.yakamon.domain.entity.GameEntity;
import fr.epita.assistants.yakamon.utils.Item;
import fr.epita.assistants.yakamon.utils.Map;
import fr.epita.assistants.yakamon.utils.Point;
import fr.epita.assistants.yakamon.utils.tile.CollectibleUtils;
import fr.epita.assistants.yakamon.utils.tile.ItemType;
import fr.epita.assistants.yakamon.utils.tile.TileType;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Getter
@Setter
@ApplicationScoped
public class GameService {
    @Inject
    GameRepository gameRepository;
    private GameEntity gameEntity;

    @Inject
    private ItemService itemService;
    @Inject
    private PlayerService playerService;
    @Inject
    private YakadexEntryService yakadexEntryService;
    @Inject
    private YakamonService yakamonService;

    @Transactional
    public void updateMap() {
        GameModel gameModel = gameRepository.getGame();
        this.gameEntity = GameConverter.modelToEntity(gameModel);
    }

    @Transactional
    public void reset() {
        this.gameEntity = null;
        this.gameRepository.deleteAll();
    }

    @Transactional
    public List<List<TileType>> createGame(String mapPath) {
        GameModel gameModel = new GameModel();
        try {
            gameModel.setMap(Files.readString(Path.of(mapPath)).replace('\n', ';'));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.gameRepository.persist(gameModel);
        this.gameEntity = GameConverter.modelToEntity(gameModel);
        return this.gameEntity.getMap().getTiles();
    }

    public Boolean hasGameStarted() {
        return gameRepository.hasGame();
    }

    public List<List<TileType>> startGame(String playerName, String mapPath) {
        this.reset();
        itemService.reset();
        playerService.reset();
        yakadexEntryService.reset();
        yakamonService.reset();
        List<List<TileType>> tiles = this.createGame(mapPath);
        playerService.createPlayer(playerName);
        itemService.addItem(new Item(ItemType.YAKABALL, 5));
        return tiles;
    }

    @Transactional
    public void removeCollectible(Point position) {
        Map map = gameEntity.getMap();
        TileType tile = map.getTileAt(position);
        if (tile.getCollectible() == null)
            return;
        map.setTileAt(position, new TileType(tile.getTerrainType(), CollectibleUtils.getCollectible('N')));
        GameModel gameModel = gameRepository.getGame();
        gameModel.setMap(map.toStringMap());
    }
}