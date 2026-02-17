package fr.epita.assistants.yakamon.presentation.api.request;

public class StartRequest {
    public final String mapPath;
    public final String playerName;

    public StartRequest(String mapPath, String playerName) {
        this.mapPath = mapPath;
        this.playerName = playerName;
    }
}
