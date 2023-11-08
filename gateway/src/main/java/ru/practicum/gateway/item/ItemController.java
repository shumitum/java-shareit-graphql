package ru.practicum.gateway.item;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.client.HttpGraphQlClient;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.ContextValue;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;
import ru.practicum.gateway.item.comment.CommentDto;
import ru.practicum.mainsrv.item.Item;
import ru.practicum.mainsrv.item.comment.Comment;
import ru.practicum.mainsrv.item.dto.OutputItemDto;

import java.util.Collections;
import java.util.List;

import static ru.practicum.mainsrv.interceptor.RequestHeaderInterceptor.USER_ID_HEADER;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ItemController {

    private final HttpGraphQlClient httpGraphQlClient;

    @MutationMapping
    public Mono<Item> createItem(@Argument @Valid ItemDto itemDto,
                                 @ContextValue @Positive Long userId,
                                 @ContextValue @NotBlank String query,
                                 @ContextValue @NotBlank String methodName) {
        log.info("Запрос на создание item {}", itemDto);
        return httpGraphQlClient.mutate()
                .header(USER_ID_HEADER, userId.toString())
                .build()
                .document(query)
                .retrieve(methodName)
                .toEntity(Item.class);
    }

    @QueryMapping
    public Mono<OutputItemDto> getItemById(@Argument @Positive Long id,
                                           @ContextValue @Positive Long userId,
                                           @ContextValue @NotBlank String query,
                                           @ContextValue @NotBlank String methodName) {
        log.info("Запрошен item с ID={}", id);
        return httpGraphQlClient.mutate()
                .header(USER_ID_HEADER, userId.toString())
                .build()
                .document(query)
                .retrieve(methodName)
                .toEntity(OutputItemDto.class);
    }

    @MutationMapping
    public Mono<Item> updateItem(@Argument @Positive Long itemId,
                                 @Argument @Valid ItemDto itemDto,
                                 @ContextValue @Positive Long userId,
                                 @ContextValue @NotBlank String query,
                                 @ContextValue @NotBlank String methodName) {
        log.info("Запрос на обновление item с ID={}", itemId);
        return httpGraphQlClient.mutate()
                .header(USER_ID_HEADER, userId.toString())
                .build()
                .document(query)
                .retrieve(methodName)
                .toEntity(Item.class);
    }

    @QueryMapping
    public Mono<List<OutputItemDto>> getUserItems(@Argument @PositiveOrZero Integer from,
                                                  @Argument @Positive Integer size,
                                                  @ContextValue @Positive Long userId,
                                                  @ContextValue @NotBlank String query,
                                                  @ContextValue @NotBlank String methodName) {
        log.info("Запрошен список всех itemов пользователя, from={}, size={}", from, size);
        return httpGraphQlClient.mutate()
                .header(USER_ID_HEADER, userId.toString())
                .build()
                .document(query)
                .retrieve(methodName)
                .toEntityList(OutputItemDto.class);
    }

    @QueryMapping
    public Mono<List<Item>> searchItem(@Argument String searchRequest,
                                       @Argument @PositiveOrZero Integer from,
                                       @Argument @Positive Integer size,
                                       @ContextValue @Positive Long userId,
                                       @ContextValue @NotBlank String query,
                                       @ContextValue @NotBlank String methodName) {
        log.info("Поисковый запрос по items: '{}', from={}, size={}", searchRequest, from, size);
        if (searchRequest.isBlank()) {
            return Mono.justOrEmpty(Collections.emptyList());
        }
        return httpGraphQlClient.mutate()
                .header(USER_ID_HEADER, userId.toString())
                .build()
                .document(query)
                .retrieve(methodName)
                .toEntityList(Item.class);
    }

    @MutationMapping
    public Mono<Boolean> deleteItemById(@Argument @Positive Long itemId,
                                        @ContextValue @Positive Long userId,
                                        @ContextValue @NotBlank String query,
                                        @ContextValue @NotBlank String methodName) {
        log.info("Запрос на удаление itema с ID={}", itemId);
        return httpGraphQlClient.mutate()
                .header(USER_ID_HEADER, userId.toString())
                .build()
                .document(query)
                .retrieve(methodName)
                .toEntity(Boolean.class);
    }

    @MutationMapping
    public Mono<Comment> createComment(@Argument @Valid CommentDto commentDto,
                                       @Argument @Positive Long itemId,
                                       @ContextValue @Positive Long userId,
                                       @ContextValue @NotBlank String query,
                                       @ContextValue @NotBlank String methodName) {
        log.info("Запрос на создание комментария к item с ID={} от user с ID={}", itemId, userId);
        return httpGraphQlClient.mutate()
                .header(USER_ID_HEADER, userId.toString())
                .build()
                .document(query)
                .retrieve(methodName)
                .toEntity(Comment.class);
    }
}
