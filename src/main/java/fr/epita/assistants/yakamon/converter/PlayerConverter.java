package fr.epita.assistants.yakamon.converter;

import fr.epita.assistants.yakamon.data.model.PlayerModel;
import fr.epita.assistants.yakamon.presentation.api.response.MoveResponse;
import fr.epita.assistants.yakamon.presentation.api.response.PlayerResponse;
import fr.epita.assistants.yakamon.utils.Point;

public class PlayerConverter {
    public static PlayerResponse modelToResponse(PlayerModel playerModel) {
        return new PlayerResponse(
                playerModel.getUuid(),
                playerModel.getName(),
                playerModel.getPosX(),
                playerModel.getPosY(),
                playerModel.getLastMove(),
                playerModel.getLastCatch(),
                playerModel.getLastCollect(),
                playerModel.getLastFeed()
        );
    }

    public static MoveResponse pointToMoveResponse(Point point) {
        return new MoveResponse(point.getPosX(), point.getPosY());
    }
}
