package fr.epita.assistants.yakamon.presentation.api.request;

import fr.epita.assistants.yakamon.utils.Direction;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FeedRequest {
    private final Integer quantity;

    public FeedRequest(Integer quantity) {
        this.quantity = quantity;
    }
}
