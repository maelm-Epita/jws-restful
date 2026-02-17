package fr.epita.assistants.yakamon.converter;

import fr.epita.assistants.yakamon.data.model.GameModel;
import fr.epita.assistants.yakamon.domain.entity.GameEntity;
import fr.epita.assistants.yakamon.utils.Map;

public class GameConverter {
    public static GameEntity modelToEntity(GameModel model) {
        return new GameEntity(new Map(model.getMap()));
    }
}
