package fr.epita.assistants.yakamon_testsuite.UtilsTests;

import fr.epita.assistants.yakamon.utils.Map;
import fr.epita.assistants.yakamon.utils.tile.TileType;
import junit.framework.Assert;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class MapTests {
    String mapPath = "/home/mael/School/ing1/s6/javapiscine/epita-ing-assistants-yaka-jws-2028-mael.minard/yakamon/src/main/resources/maps/walkable.epimap";

    @Test
    public void parseWalkableTest() {
        String map = null;
        try {
            map = Files.readString(Path.of(mapPath));
        } catch (IOException e) {
            throw new RuntimeException("Invalid test config, map path not found");
        }
        map = map.replace('\n', ';');
        List<List<TileType>> tileTypes = Map.parseMap(map);
        Assert.assertEquals(11, tileTypes.size());
        Assert.assertEquals(21, tileTypes.getFirst().size());
    }

    @Test
    public void parseThenToStringTest() {
        String map = null;
        try {
            map = Files.readString(Path.of(mapPath));
        } catch (IOException e) {
            throw new RuntimeException("Invalid test config, map path not found");
        }
        map = map.replace('\n', ';');
        Map mapObject = new Map(map);
        String mapActual = mapObject.toStringMap();
        Assert.assertEquals(map, mapActual);
    }
}
