package fr.epita.assistants.yakamon.presentation.api.response;

import fr.epita.assistants.yakamon.utils.Item;
import lombok.Getter;

import java.util.List;

@Getter
public class InventoryResponse {
    private final List<Item> items;

    public InventoryResponse(List<Item> items) {
        this.items = items;
    }
}
