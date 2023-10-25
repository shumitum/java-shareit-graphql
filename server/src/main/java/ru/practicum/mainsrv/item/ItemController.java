package ru.practicum.mainsrv.item;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.ContextValue;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import ru.practicum.mainsrv.item.comment.Comment;
import ru.practicum.mainsrv.item.comment.dto.CommentDto;
import ru.practicum.mainsrv.item.dto.ItemDto;

import java.util.Collections;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ItemController {

    public static final String USER_ID_HEADER = "X-Sharer-User-Id";
    private final ItemService itemService;

    @MutationMapping
    public Item createItem(@Argument /*@Validated(Create.class)*/ ItemDto itemDto, @ContextValue Long userId) {
        log.info("Запрос на создание item {}", itemDto);
        Item item = itemService.createItem(itemDto, userId);
        log.info("Создан item: {}", item);
        return item;
    }

    @QueryMapping
    public Item getItemById(@Argument @Positive Long id, @ContextValue Long userId) {
        log.info("Запрошен item с ID={}", id);
        return itemService.getItemById(id, userId);
    }

    @MutationMapping
    public Item updateItem(@Argument @Positive Long itemId,
                           @Argument @Valid ItemDto itemDto,
                           @ContextValue Long userId) {
        log.info("Запрос на обновление item {}", itemDto);
        Item item = itemService.updateItem(itemId, itemDto, userId);
        log.info("Item обновлен: {}", item);
        return item;
    }

    @QueryMapping
    public List<Item> getUserItems(@Argument @PositiveOrZero Integer from,
                                   @Argument @Positive Integer size,
                                   @ContextValue Long userId) {
        log.info("Запрошен список всех itemов");
        return itemService.getUserItems(userId, from, size);
    }

    @QueryMapping
    public List<Item> searchItem(@Argument String searchRequest,
                                 @Argument @PositiveOrZero Integer from,
                                 @Argument @Positive Integer size,
                                 @ContextValue Long userId) {
        log.info("Поисковый запрос: '{}'", searchRequest);
        if (searchRequest.isBlank()) {
            return Collections.emptyList();
        }
        return itemService.searchItem(searchRequest, userId, from, size);
    }

    @MutationMapping
    public void deleteItemById(@Argument @Positive Long itemId, @ContextValue Long userId) {
        log.info("Запрос на удаление itema с ID={}", itemId);
        itemService.deleteItemById(itemId, userId);
        log.info("Item с ID={} удален", itemId);
    }

    @MutationMapping
    public Comment createComment(@Argument @Valid CommentDto commentDto,
                                 @Argument Long itemId,
                                 @ContextValue Long userId) {
        log.info("Запрос на создание комментария к item с ID={} от user с ID={}", itemId, userId);
        Comment comment = itemService.createComment(commentDto, itemId, userId);
        log.info("Пользователь с ID={} оставил комментарий вещи с ID={}", userId, itemId);
        return comment;
    }
}
