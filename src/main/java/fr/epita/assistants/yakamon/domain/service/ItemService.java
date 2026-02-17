package fr.epita.assistants.yakamon.domain.service;

import fr.epita.assistants.yakamon.data.model.ItemModel;
import fr.epita.assistants.yakamon.data.repository.GameRepository;
import fr.epita.assistants.yakamon.data.repository.ItemRepository;
import fr.epita.assistants.yakamon.utils.ErrorCode;
import fr.epita.assistants.yakamon.utils.Item;
import fr.epita.assistants.yakamon.utils.tile.ItemType;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Getter
@Setter
@ApplicationScoped
public class ItemService {
    @Inject
    ItemRepository itemRepository;

    @Transactional
    public void reset() {
        itemRepository.deleteAll();
    }

    public List<ItemModel> getItems() {
        return itemRepository.listAll();
    }

    @Transactional
    public void addItem(Item item) {
        List<ItemModel> existingItemModels = getItems().stream()
                .filter(itemModel -> itemModel.getType().equals(item.getItemType()))
                .toList();
        if (!existingItemModels.isEmpty()) {
            ItemModel itemModel = existingItemModels.getFirst();
            itemModel.setQuantity(itemModel.getQuantity()+item.getQuantity());
        } else {
            ItemModel itemModel = new ItemModel();
            itemModel.setType(item.getItemType());
            itemModel.setQuantity(item.getQuantity());
            itemRepository.persist(itemModel);
        }
    }

    public Boolean hasYakaball() {
        return !getItems().stream()
                .filter(itemModel -> itemModel.getType().equals(ItemType.YAKABALL))
                .filter(itemModel -> itemModel.getQuantity() > 0)
                .toList().isEmpty();
    }

    public ItemModel getScroogeWithQuantity(Integer count) {
        List<ItemModel> itemModels = getItems().stream()
                .filter(itemModel -> itemModel.getType().equals(ItemType.SCROOGE))
                .filter(itemModel -> itemModel.getQuantity() >= count)
                .toList();
        if (itemModels.isEmpty())
            return null;
        return itemModels.getFirst();
    }

    @Transactional
    public void removeYakaball(Integer count) {
        if (!hasYakaball())
            return;
       ItemModel yakaball = getItems().stream()
                .filter(itemModel -> itemModel.getType().equals(ItemType.YAKABALL)).toList().getFirst();
       yakaball.setQuantity(Math.max(yakaball.getQuantity()-count, 0));
    }

    @Transactional
    public void removeScrooge(Integer count) {
        ItemModel scrooge = getScroogeWithQuantity(count);
        if (scrooge == null)
            ErrorCode.FEED_INVALID_ERROR.throwException();
        scrooge.setQuantity(scrooge.getQuantity()-count);
    }
}
