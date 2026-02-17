package fr.epita.assistants.yakamon.domain.entity;

import fr.epita.assistants.yakamon.utils.Map;
import fr.epita.assistants.yakamon.utils.tile.TileType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.List;

@Getter @Setter
public class GameEntity {
    private final Map map;

    public GameEntity(Map map) {
        this.map = map;
    }
}
