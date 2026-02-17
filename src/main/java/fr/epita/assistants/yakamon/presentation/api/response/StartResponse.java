package fr.epita.assistants.yakamon.presentation.api.response;

import fr.epita.assistants.yakamon.utils.tile.TileType;

import java.util.List;

public class StartResponse {
    public final List<List<TileType>> tiles;

    public StartResponse(List<List<TileType>> tiles) {
        this.tiles = tiles;
    }
}
