package fr.epita.assistants.yakamon.presentation.api.response;

import lombok.Getter;

@Getter
public class MoveResponse {
    private final Integer posX;
    private final Integer posY;

    public MoveResponse(Integer posX, Integer posY) {
        this.posX = posX;
        this.posY = posY;
    }
}
