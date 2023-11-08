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
import ru.practicum.mainsrv.item.dto.OutputItemDto;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @MutationMapping
    public Mono<Item> createItem(@Argument ItemDto itemDto,
                                 @ContextValue Long userId) {
        log.info("Новый запрос на создание itemа: {}", itemDto);
        Item item = itemService.createItem(itemDto, userId);
        log.info("Создан новый item: {}", item);
        return Mono.justOrEmpty(item);
    }

    @QueryMapping
    public Mono<OutputItemDto> getItemById(@Argument Long id,
                                           @ContextValue Long userId) {
        log.info("Пользователем с ID={} запрошен item с ID={}", userId, id);
        OutputItemDto item = itemService.getItemById(id, userId);
        log.info("Запрошенный пользователем с ID={} item: {}", userId, item);
        return Mono.justOrEmpty(item);
    }

    @MutationMapping
    public Mono<Item> updateItem(@Argument Long itemId,
                                 @Argument ItemDto itemDto,
                                 @ContextValue Long userId) {
        log.info("Запрос на обновление itema c ID={} itemDTO: {}", itemId, itemDto);
        Item item = itemService.updateItem(itemId, itemDto, userId);
        log.info("Item c ID={} обновленный item: {}", itemId, item);
        return Mono.justOrEmpty(item);
    }

    @QueryMapping
    public Mono<List<OutputItemDto>> getUserItems(@Argument Integer from,
                                                  @Argument Integer size,
                                                  @ContextValue Long userId) {
        log.info("Запрошен список ItemОВ пользователя c ID={}, from={}, size={}", userId, from, size);
        return Mono.justOrEmpty(itemService.getUserItems(userId, from, size));
    }

    @QueryMapping
    public Mono<List<Item>> searchItem(@Argument String searchRequest,
                                       @Argument Integer from,
                                       @Argument Integer size,
                                       @ContextValue Long userId) {
        log.info("Пользователь с ID={} пытался найти: \"{}\". from={}, size={}", userId, searchRequest, from, size);
        return Mono.justOrEmpty(itemService.searchItem(searchRequest, userId, from, size));
    }

    @MutationMapping
    public Boolean deleteItemById(@Argument Long itemId,
                                  @ContextValue Long userId) {
        log.info("Запрос на удаление вещи с ID={}", itemId);
        Boolean isItemDelete = itemService.deleteItemById(itemId, userId);
        log.info("Item с ID={} удален? {}", itemId, isItemDelete);
        return isItemDelete;

    }

    @MutationMapping
    public Mono<Comment> createComment(@Argument CommentDto commentDto,
                                       @Argument Long itemId,
                                       @ContextValue Long userId) {
        log.info("Пользователь с ID={} оставил комментарий к itemу с ID={}, текст комментария: {}",
                userId, itemId, commentDto.getText());
        Comment comment = itemService.createComment(commentDto, itemId, userId);
        log.info("Комментарий для itemа с ID={} создан: {}", itemId, comment);
        return Mono.justOrEmpty(comment);
    }
}
