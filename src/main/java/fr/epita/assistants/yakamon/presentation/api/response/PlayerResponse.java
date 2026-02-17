package fr.epita.assistants.yakamon.presentation.api.response;

import fr.epita.assistants.yakamon.data.model.PlayerModel;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class PlayerResponse {
    private final UUID uuid;
    private final String name;
    private final Integer posX;
    private final Integer posY;
    private final LocalDateTime lastMove;
    private final LocalDateTime lastCatch;
    private final LocalDateTime lastCollect;
    private final LocalDateTime lastFeed;

    public PlayerResponse(UUID uuid, String name, Integer posX, Integer posY, LocalDateTime lastMove, LocalDateTime lastCatch, LocalDateTime lastCollect, LocalDateTime lastFeed) {
        this.uuid = uuid;
        this.name = name;
        this.posX = posX;
        this.posY = posY;
        this.lastMove = lastMove;
        this.lastCatch = lastCatch;
        this.lastCollect = lastCollect;
        this.lastFeed = lastFeed;
    }
}
