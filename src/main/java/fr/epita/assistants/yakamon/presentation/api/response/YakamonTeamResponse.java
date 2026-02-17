package fr.epita.assistants.yakamon.presentation.api.response;

import lombok.Getter;

import java.util.List;

@Getter
public class YakamonTeamResponse {
    private final List<YakamonResponse> yakamons;

    public YakamonTeamResponse(List<YakamonResponse> yakamons) {
        this.yakamons = yakamons;
    }
}
