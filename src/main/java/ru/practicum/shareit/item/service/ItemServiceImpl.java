package ru.practicum.shareit.item.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final UserService userService;
    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;

    @Override
    public ItemDto save(long userId, ItemDto itemDto) {
        userService.getById(userId);
        Item item = itemRepository.save(userId, itemMapper.toItem(itemDto));
        return itemMapper.toItemDto(item);
    }

    @Override
    public List<ItemDto> getAllUserItems(long userId) {
        userService.getById(userId);
        return itemRepository.findAllUserItems(userId).stream()
                .map(itemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public @Valid ItemDto update(long itemId, long userId, ItemDto itemDto) {
        ItemDto oldItemDto = getById(itemId, userId);

        if (itemDto.getName() == null) {
            itemDto.setName(oldItemDto.getName());
        }
        if (itemDto.getDescription() == null) {
            itemDto.setDescription(oldItemDto.getDescription());
        }
        if (itemDto.getAvailable() == null) {
            itemDto.setAvailable(oldItemDto.getAvailable());
        }

        itemRepository.findAllUserItems(userId).stream()
                .map(item -> item.getId())
                .filter(id -> id == itemId)
                .findFirst()
                .orElseThrow(() -> new NotFoundException(String.format("item with id: %d " +
                        "does not belong to the user with id: %d", itemId, userId)));

        Item item = itemRepository.update(itemId, userId, itemMapper.toItem(itemDto));

        return itemMapper.toItemDto(item);
    }

    @Override
    public ItemDto getById(long itemId, long userId) {
        userService.getById(userId);

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException(String.format("item with id: %d not found", itemId)));

        return itemMapper.toItemDto(item);
    }

    @Override
    public List<ItemDto> search(long userId, String text) {
        userService.getById(userId);

        if (text == null || text.isBlank()) {
            return Collections.emptyList();
        }

        return itemRepository.search(text).stream()
                .map(itemMapper::toItemDto)
                .collect(Collectors.toList());
    }
}
