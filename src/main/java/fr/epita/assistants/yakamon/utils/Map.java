package fr.epita.assistants.yakamon.utils;

import fr.epita.assistants.yakamon.utils.tile.*;
import lombok.Getter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

@Getter
public class Map {

    private final List<List<TileType>> tiles;

    public Map(String map) {
        this.tiles = parseMap(map);
    }

    private static List<TileType> parseRow(String mapRow) {
        ArrayList<TileType> tileRow = new ArrayList<>();
        Integer idx = 0;
        while (idx < mapRow.length()) {
            Integer count = mapRow.charAt(idx) - '0';
            TerrainType terrainType = TerrainType.getTerrain(mapRow.charAt(idx+1));
            Collectible collectible = CollectibleUtils.getCollectible(mapRow.charAt(idx+2));
            for (int c=0; c<count; c++) {
                TileType type = new TileType(terrainType, collectible);
                tileRow.add(type);
            }
            idx+=3;
        }
        return tileRow;
    }

    public static List<List<TileType>> parseMap(String map) {
        List<String> lines = List.of(map.split(";"));

        ArrayList<List<TileType>> tileGrid = new ArrayList<>();
        lines.forEach(l -> tileGrid.add(parseRow(l)));
        return tileGrid;
    }

    public TileType getTileAt(Point point) {
        if (point.getPosX() < 0 || point.getPosY() < 0 ||
                point.getPosX() >= tiles.getFirst().size() || point.getPosY() >= tiles.size())
            return null;
        return tiles.get(point.getPosY()).get(point.getPosX());
    }

    public void setTileAt(Point point, TileType tileType) {
        if (point.getPosX() < 0 || point.getPosY() < 0 ||
                point.getPosX() >= tiles.getFirst().size() || point.getPosY() >= tiles.size())
            return;
        TileType tile = tiles.get(point.getPosY()).get(point.getPosX());
        tile.setCollectible(tileType.getCollectible());
        tile.setTerrainType(tileType.getTerrainType());
    }

    public String toStringMap() {
        StringBuilder sb = new StringBuilder();
        for (List<TileType> tileRow : this.tiles) {
            Integer idx = 0;
            while (idx < tileRow.size()) {
                TileType tileType = tileRow.get(idx);
                Integer nbEqual = 1;
                while (idx+nbEqual < tileRow.size()) {
                    TileType otherTileType = tileRow.get(idx+nbEqual);
                    if (!tileType.getTerrainType().equals(otherTileType.getTerrainType()) ||
                            !tileType.getCollectible().equals(otherTileType.getCollectible()) ||
                            nbEqual == 9) {
                        break;
                    }
                    nbEqual++;
                }
                sb.append(nbEqual);
                sb.append(tileType.getTerrainType().getValue());
                sb.append(tileType.getCollectible().getCollectibleInfo().getValue());
                idx+=nbEqual;
            }
            sb.append(';');
        }
        return sb.substring(0, sb.length()-1);
    }

}
