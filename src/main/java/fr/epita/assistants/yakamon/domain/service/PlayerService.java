package fr.epita.assistants.yakamon.domain.service;

import fr.epita.assistants.yakamon.data.model.PlayerModel;
import fr.epita.assistants.yakamon.data.model.YakamonModel;
import fr.epita.assistants.yakamon.data.repository.PlayerRepository;
import fr.epita.assistants.yakamon.domain.entity.GameEntity;
import fr.epita.assistants.yakamon.presentation.rest.InventoryResource;
import fr.epita.assistants.yakamon.utils.*;
import fr.epita.assistants.yakamon.utils.tile.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.Setter;
import org.apache.velocity.tools.struts.TilesTool;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@ApplicationScoped
public class PlayerService {
    @Inject
    PlayerRepository playerRepository;
    @Inject
    GameService gameService;
    @Inject
    ItemService itemService;
    @Inject
    YakamonService yakamonService;
    @Inject
    YakadexEntryService yakadexEntryService;

    @ConfigProperty(name = "JWS_TICK_DURATION") Long tickDuration;
    @ConfigProperty(name = "JWS_MOVEMENT_DELAY") Long moveDelay;
    @ConfigProperty(name = "JWS_CATCH_DELAY") Long catchDelay;
    @ConfigProperty(name = "JWS_COLLECT_DELAY") Long collectDelay;

    @ConfigProperty(name = "JWS_COLLECT_MULTIPLIER") Long collectMultiplier;

    @Transactional
    public void reset() {
        playerRepository.deleteAll();
    }

    @Transactional
    public void createPlayer(String name) {
        PlayerModel playerModel = new PlayerModel();
        playerModel.setName(name);
        playerModel.setPosX(0);
        playerModel.setPosY(0);
        playerModel.setLastFeed(null);
        playerModel.setLastCatch(null);
        playerModel.setLastMove(null);
        playerModel.setLastCollect(null);
        playerRepository.persist(playerModel);
    }

    public PlayerModel getPlayer() {
        return playerRepository.findAll().list().getFirst();
    }

    public Boolean isDelayValid(LocalDateTime current, LocalDateTime last, Long delay) {
        if (last == null)
            return true;
        Duration threshold = Duration.ofMillis(tickDuration * delay);
        Duration actual = Duration.ofMillis(last.until(current, ChronoUnit.MILLIS));
        return actual.compareTo(threshold) >= 0;
    }

    @Transactional
    public Point movePlayer(Direction direction) {
        PlayerModel playerModel = this.getPlayer();
        LocalDateTime current = LocalDateTime.now();
        if (!isDelayValid(current, playerModel.getLastMove(), this.moveDelay))
            ErrorCode.MOVE_DELAY_ERROR.throwException();
        Point position = new Point(playerModel.getPosX(), playerModel.getPosY());
        switch (direction) {
            case UP:
                position.setPosY(position.getPosY()-1);
                break;
            case DOWN:
                position.setPosY(position.getPosY()+1);
                break;
            case LEFT:
                position.setPosX(position.getPosX()-1);
                break;
            case RIGHT:
                position.setPosX(position.getPosX()+1);
                break;
            default:
                ErrorCode.MOVE_DIRECTION_ERROR.throwException();
        }
        TileType tile = gameService.getGameEntity().getMap().getTileAt(position);
        if (tile == null)
            ErrorCode.MOVE_DIRECTION_ERROR.throwException();
        if (!tile.getTerrainType().isWalkable()) {
            Set<ElementType> types = tile.getTerrainType().getCompatibleType();
            if (!yakamonService.teamHasCompatibleType(types))
                ErrorCode.MOVE_DIRECTION_ERROR.throwException();
        }
        playerModel.setLastMove(current);
        playerModel.setPosX(position.getPosX());
        playerModel.setPosY(position.getPosY());
        return position;
    }

    @Transactional
    public YakamonModel catchYakamon() {
        gameService.updateMap();
        PlayerModel playerModel = this.getPlayer();
        LocalDateTime current = LocalDateTime.now();
        if (!isDelayValid(current, playerModel.getLastCatch(), this.catchDelay))
            ErrorCode.CATCH_DELAY_ERROR.throwException();
        if (!itemService.hasYakaball() || yakamonService.isTeamFull())
            ErrorCode.CATCH_INVALID_ERROR.throwException();
        Point position = new Point(playerModel.getPosX(), playerModel.getPosY());
        TileType tile = gameService.getGameEntity().getMap().getTileAt(position);
        if (tile == null || !tile.getCollectible().getCollectibleType().equals(CollectibleType.YAKAMON))
            ErrorCode.CATCH_INVALID_ERROR.throwException();
        playerModel.setLastCatch(current);
        itemService.removeYakaball(1);
        YakamonModel yakamonModel = yakamonService.createYakamon((YakamonInfo) tile.getCollectible().getCollectibleInfo());
        yakamonModel.getYakadexEntry().setCaught(true);
        gameService.removeCollectible(position);
        return yakamonModel;
    }

    @Transactional
    public TileType collectItem() {
        gameService.updateMap();
        PlayerModel playerModel = this.getPlayer();
        LocalDateTime current = LocalDateTime.now();
        if (!isDelayValid(current, playerModel.getLastCollect(), this.collectDelay))
            ErrorCode.COLLECT_DELAY_ERROR.throwException();
        Point position = new Point(playerModel.getPosX(), playerModel.getPosY());
        TileType tile = gameService.getGameEntity().getMap().getTileAt(position);
        if (tile == null || !tile.getCollectible().getCollectibleType().equals(CollectibleType.ITEM))
            ErrorCode.COLLECT_INVALID_ERROR.throwException();
        ItemType itemType = (ItemType) tile.getCollectible();
        if (itemType.getCollectibleInfo().getValue() == 'N')
            ErrorCode.COLLECT_INVALID_ERROR.throwException();
        playerModel.setLastCollect(current);
        itemService.addItem(new Item(itemType, Math.toIntExact(this.collectMultiplier)));
        gameService.removeCollectible(position);
        return tile;
    }

}
