package fr.epita.assistants.yakamon.presentation.api.request;

import fr.epita.assistants.yakamon.utils.Direction;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MoveRequest {
    private final Direction direction;

    public MoveRequest(Direction direction) {
        this.direction = direction;
    }
}
