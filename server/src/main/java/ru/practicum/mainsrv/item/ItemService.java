package ru.practicum.mainsrv.item;

import ru.practicum.mainsrv.item.comment.Comment;
import ru.practicum.mainsrv.item.comment.dto.CommentDto;
import ru.practicum.mainsrv.item.dto.ItemDto;
import ru.practicum.mainsrv.item.dto.OutputItemDto;

import java.util.List;

public interface ItemService {
    Item createItem(ItemDto itemDto, Long userId);

    Item updateItem(Long itemId, ItemDto itemDto, Long ownerId);

    OutputItemDto getItemById(Long itemId, Long userId);

    List<OutputItemDto> getUserItems(Long userId, Integer from, Integer size);

    List<Item> searchItem(String searchRequest, Long userId, Integer from, Integer size);

    Boolean deleteItemById(Long itemId, Long userId);

    Comment createComment(CommentDto commentDto, Long itemId, Long commentatorId);

    Item findItemById(Long itemId);
}