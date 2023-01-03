package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentCreationDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithBookingsAndComments;

import java.util.List;

public interface ItemService {

    ItemDto save(Long userId, ItemDto itemDto);

    List<ItemDtoWithBookingsAndComments> getAllUserItems(Long userId);

    ItemDto update(Long itemId, Long userId, ItemDto itemDto);

    ItemDtoWithBookingsAndComments getById(Long itemId, Long userId);

    List<ItemDto> search(Long userId, String text);

    CommentDto addComment(Long userId, Long itemId, CommentCreationDto commentCreationDto);
}
