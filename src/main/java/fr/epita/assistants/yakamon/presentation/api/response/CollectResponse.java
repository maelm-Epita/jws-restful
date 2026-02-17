package fr.epita.assistants.yakamon.presentation.api.response;

import fr.epita.assistants.yakamon.utils.tile.TileType;
import lombok.Getter;

@Getter
public class CollectResponse {
    private final TileType tileType;

    public CollectResponse(TileType tileType) {
        this.tileType = tileType;
    }
}
