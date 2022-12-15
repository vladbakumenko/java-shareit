package ru.practicum.shareit.item.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.*;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ItemRepositoryImpl implements ItemRepository {

    private int lastId = 0;
    private Map<Long, List<Item>> items = new HashMap<>();

    @Override
    public Item save(long userId, Item item) {
        item.setId(getId());
        item.setOwner(userId);

        items.compute(userId, (id, list) -> {
            if (list == null) {
                list = new ArrayList<>();
            }
            list.add(item);
            return list;
        });

        return item;
    }

    @Override
    public List<Item> findAllUserItems(long userId) {
        return items.getOrDefault(userId, Collections.emptyList());
    }

    @Override
    public Item update(long itemId, long userId, Item item) {
        item.setId(itemId);
        item.setOwner(userId);

        items.get(userId).removeIf(i -> i.getId() == itemId);
        items.get(userId).add(item);

        return item;
    }

    @Override
    public Optional<Item> findById(long itemId) {
        return items.values().stream()
                .flatMap(List::stream)
                .filter(item -> item.getId() == itemId)
                .findFirst();
    }

    @Override
    public List<Item> search(String text) {
        return items.values().stream()
                .flatMap(List::stream)
                .filter(item -> {
                    if (item.getAvailable().equals(true)) {
                        return item.getName().toLowerCase().contains(text.toLowerCase())
                                || item.getDescription().toLowerCase().contains(text.toLowerCase());
                    }
                    return false;
                }).collect(Collectors.toList());
    }

    private int getId() {
        return ++lastId;
    }
}
