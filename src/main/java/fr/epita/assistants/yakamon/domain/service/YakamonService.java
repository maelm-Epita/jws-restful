package fr.epita.assistants.yakamon.domain.service;

import fr.epita.assistants.yakamon.data.model.PlayerModel;
import fr.epita.assistants.yakamon.data.model.YakadexEntryModel;
import fr.epita.assistants.yakamon.data.model.YakamonModel;
import fr.epita.assistants.yakamon.data.repository.YakamonRepository;
import fr.epita.assistants.yakamon.utils.ElementType;
import fr.epita.assistants.yakamon.utils.ErrorCode;
import fr.epita.assistants.yakamon.utils.Point;
import fr.epita.assistants.yakamon.utils.tile.TileType;
import fr.epita.assistants.yakamon.utils.tile.YakamonInfo;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.Setter;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@ApplicationScoped
public class YakamonService {
    @Inject
    YakamonRepository yakamonRepository;
    @Inject
    YakadexEntryService yakadexEntryService;
    @Inject
    PlayerService playerService;
    @Inject
    ItemService itemService;
    @Inject
    YakamonService yakamonService;
    @Inject
    GameService gameService;

    @ConfigProperty(name = "JWS_FEED_DELAY") Long feedDelay;

    @Transactional
    public void reset() {
        yakamonRepository.deleteAll();
    }

    public Boolean isTeamFull() {
        return yakamonRepository.listAll().size() >= 3;
    }

    public YakamonModel getWithUUID(UUID uuid) {
        List<YakamonModel> matchingYakamons = yakamonRepository.listAll().stream()
                .filter(yakamonModel -> yakamonModel.getUuid().equals(uuid))
                .toList();
        if (matchingYakamons.isEmpty())
            return null;
        return matchingYakamons.getFirst();
    }

    @Transactional
    public YakamonModel createYakamon(YakamonInfo yakamonInfo) {
        YakamonModel yakamonModel = new YakamonModel();
        YakadexEntryModel yakadexEntryModel = yakadexEntryService.getWithId(yakamonInfo.getYakadexId());
        yakamonModel.setYakadexEntry(yakadexEntryModel);
        yakamonModel.setNickname(yakadexEntryModel.getName());
        yakamonModel.setEnergyPoints(0);
        yakamonRepository.persist(yakamonModel);
        return yakamonModel;
    }

    public List<YakamonModel> getTeam() {
        return yakamonRepository.listAll().stream().limit(3).toList();
    }

    @Transactional
    public YakamonModel renameYakamon(String newNickname, UUID uuid) {
        YakamonModel yakamonModel = getWithUUID(uuid);
        if (yakamonModel == null)
            ErrorCode.YAKAMON_NOT_FOUND_ERROR.throwException();
        yakamonModel.setNickname(newNickname);
        return yakamonModel;
    }

    @Transactional
    public void releaseYakamon(UUID uuid) {
        YakamonModel yakamonModel = getWithUUID(uuid);
        if (yakamonModel == null)
            ErrorCode.YAKAMON_NOT_FOUND_ERROR.throwException();
        PlayerModel playerModel = playerService.getPlayer();
        TileType tile = gameService.getGameEntity().getMap().getTileAt(new Point(playerModel.getPosX(), playerModel.getPosY()));
        if (!tile.getTerrainType().isWalkable() &&
                !teamExceptYakamonHasCompatibleType(yakamonModel, tile.getTerrainType().getCompatibleType()))
            ErrorCode.RELEASE_UNABLE_ERROR.throwException();
        yakamonRepository.delete(yakamonModel);
    }

    @Transactional
    public YakamonModel feedYakamon(Integer quantity, UUID uuid) {
        PlayerModel playerModel = playerService.getPlayer();
        LocalDateTime current = LocalDateTime.now();
        if (!playerService.isDelayValid(current, playerModel.getLastFeed(), this.feedDelay))
            ErrorCode.FFED_DELAY_ERROR.throwException();
        YakamonModel yakamonModel = getWithUUID(uuid);
        if (yakamonModel == null)
            ErrorCode.YAKAMON_NOT_FOUND_ERROR.throwException();
        itemService.removeScrooge(quantity);
        playerModel.setLastFeed(current);
        yakamonModel.setEnergyPoints(yakamonModel.getEnergyPoints()+quantity);
        return yakamonModel;
    }

    @Transactional
    public YakamonModel evolveYakamon(UUID uuid) {
        YakamonModel yakamonModel = getWithUUID(uuid);
        if (yakamonModel == null)
            ErrorCode.YAKAMON_NOT_FOUND_ERROR.throwException();
        YakadexEntryModel evolutionEntryModel = yakamonModel.getYakadexEntry().getEvolution();
        if (evolutionEntryModel == null)
            ErrorCode.YAKAMON_EVOLUTION_NOT_EXIST_ERROR.throwException();
        if (yakamonModel.getEnergyPoints() < yakamonModel.getYakadexEntry().getEvolveThreshold())
            ErrorCode.EVOLVE_INVALID_ERROR.throwException();
        evolutionEntryModel.setCaught(true);
        if (yakamonModel.getNickname().equals(yakamonModel.getYakadexEntry().getName()))
            yakamonModel.setNickname(evolutionEntryModel.getName());
        yakamonModel.setEnergyPoints(yakamonModel.getEnergyPoints() - yakamonModel.getYakadexEntry().getEvolveThreshold());
        yakamonModel.setYakadexEntry(evolutionEntryModel);
        return yakamonModel;
    }

    public boolean teamHasCompatibleType(Set<ElementType> types) {
        List<YakamonModel> team = this.getTeam();
        team = team.stream()
                .filter(
                        yakamonModel -> types.contains(yakamonModel.getYakadexEntry().getFirstType()) ||
                                (yakamonModel.getYakadexEntry().getSecondType() != null &&
                                        types.contains(yakamonModel.getYakadexEntry().getSecondType()))
                ).toList();
        return !team.isEmpty();
    }

    public boolean teamExceptYakamonHasCompatibleType(YakamonModel y, Set<ElementType> types) {
        List<YakamonModel> team = this.getTeam();
        team = team.stream().filter(yakamonModel-> !yakamonModel.getUuid().equals(y.getUuid())).toList();
        team = team.stream()
                .filter(
                        yakamonModel -> types.contains(yakamonModel.getYakadexEntry().getFirstType()) ||
                                (yakamonModel.getYakadexEntry().getSecondType() != null &&
                                        types.contains(yakamonModel.getYakadexEntry().getSecondType()))
                ).toList();
        return !team.isEmpty();
    }
}
