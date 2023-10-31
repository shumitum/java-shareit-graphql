package ru.practicum.mainsrv.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.ContextValue;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;
import ru.practicum.mainsrv.item.comment.Comment;
import ru.practicum.mainsrv.item.comment.dto.CommentDto;
import ru.practicum.mainsrv.item.dto.ItemDto;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @MutationMapping
    public Mono<Item> createItem(@Argument ItemDto itemDto,
                                 @ContextValue Long userId) {
        Item item = itemService.createItem(itemDto, userId);
        log.info("Создан item: {}", item);
        return Mono.justOrEmpty(item);
    }

    @QueryMapping
    public Mono<Item> getItemById(@Argument Long id,
                                  @ContextValue Long userId) {
        return Mono.justOrEmpty(itemService.getItemById(id, userId));
    }

    @MutationMapping
    public Mono<Item> updateItem(@Argument Long itemId,
                                 @Argument ItemDto itemDto,
                                 @ContextValue Long userId) {
        Item item = itemService.updateItem(itemId, itemDto, userId);
        log.info("Item c ID={} обновлен: {}", itemId, item);
        return Mono.justOrEmpty(item);
    }

    @QueryMapping
    public Mono<List<Item>> getUserItems(@Argument Integer from,
                                         @Argument Integer size,
                                         @ContextValue Long userId) {
        return Mono.justOrEmpty(itemService.getUserItems(userId, from, size));
    }

    @QueryMapping
    public Mono<List<Item>> searchItem(@Argument String searchRequest,
                                       @Argument Integer from,
                                       @Argument Integer size,
                                       @ContextValue Long userId) {
        return Mono.justOrEmpty(itemService.searchItem(searchRequest, userId, from, size));
    }

    @MutationMapping
    public Boolean deleteItemById(@Argument Long itemId,
                                  @ContextValue Long userId) {
        Boolean isItemDelete = itemService.deleteItemById(itemId, userId);
        log.info("Item с ID={} удален", itemId);
        return isItemDelete;

    }

    @MutationMapping
    public Mono<Comment> createComment(@Argument CommentDto commentDto,
                                       @Argument Long itemId,
                                       @ContextValue Long userId) {
        Comment comment = itemService.createComment(commentDto, itemId, userId);
        log.info("Пользователь с ID={} оставил комментарий вещи с ID={}", userId, itemId);
        log.info("Комментарий сервер: {}", comment);
        return Mono.justOrEmpty(comment);
    }
}
