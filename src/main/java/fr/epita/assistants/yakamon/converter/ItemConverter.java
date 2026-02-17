package fr.epita.assistants.yakamon.converter;

import fr.epita.assistants.yakamon.data.model.ItemModel;
import fr.epita.assistants.yakamon.presentation.api.response.InventoryResponse;
import fr.epita.assistants.yakamon.utils.Item;

import java.util.List;

public class ItemConverter {
    public static Item modelToItem(ItemModel model) {
        return new Item(model.getType(), model.getQuantity());
    }

    public static InventoryResponse modelsToResponse(List<ItemModel> itemsModels) {
        List<Item> items = itemsModels.stream().map(ItemConverter::modelToItem).toList();
        return new InventoryResponse(items);
    }
}
