package fr.epita.assistants.yakamon.presentation.api.response;

import lombok.Getter;

import java.util.UUID;

@Getter
public class YakamonResponse {
    private final UUID uuid;
    private final String nickname;
    private final Integer yakadexId;
    private final Integer energyPoints;

    public YakamonResponse(UUID uuid, String nickname, Integer yakadexId, Integer energyPoints) {
        this.uuid = uuid;
        this.nickname = nickname;
        this.yakadexId = yakadexId;
        this.energyPoints = energyPoints;
    }
}
