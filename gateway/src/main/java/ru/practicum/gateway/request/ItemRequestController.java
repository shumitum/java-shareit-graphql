package ru.practicum.gateway.request;

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
import ru.practicum.mainsrv.request.ItemRequest;
import ru.practicum.mainsrv.request.dto.ItemRequestDto;

import java.util.List;

import static ru.practicum.mainsrv.interceptor.RequestHeaderInterceptor.USER_ID_HEADER;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ItemRequestController {
    private final HttpGraphQlClient httpGraphQlClient;

    @MutationMapping
    public Mono<ItemRequest> createItemRequest(@Argument @Valid ItemRequestDto itemRequestDto,
                                               @ContextValue @Positive Long userId,
                                               @ContextValue @NotBlank String query,
                                               @ContextValue @NotBlank String methodName) {
        log.info("Новый запрос на добавление item: {}", itemRequestDto);
        return httpGraphQlClient.mutate()
                .header(USER_ID_HEADER, userId.toString())
                .build()
                .document(query)
                .retrieve(methodName)
                .toEntity(ItemRequest.class);
    }

    @QueryMapping
    public Mono<ItemRequest> getItemRequestById(@Argument @Positive Long requestId,
                                                @ContextValue @Positive Long userId,
                                                @ContextValue @NotBlank String query,
                                                @ContextValue @NotBlank String methodName) {
        log.info("Пользователем с ID={} запрошен request на вещь с ID={}", userId, requestId);
        return httpGraphQlClient.mutate()
                .header(USER_ID_HEADER, userId.toString())
                .build()
                .document(query)
                .retrieve(methodName)
                .toEntity(ItemRequest.class);
    }

    @QueryMapping
    public Mono<List<ItemRequest>> getUserItemRequests(@ContextValue @Positive Long userId,
                                                       @ContextValue @NotBlank String query,
                                                       @ContextValue @NotBlank String methodName) {
        log.info("Пользователем с ID={} запрошены собственные requests на добавление items", userId);
        return httpGraphQlClient.mutate()
                .header(USER_ID_HEADER, userId.toString())
                .build()
                .document(query)
                .retrieve(methodName)
                .toEntityList(ItemRequest.class);
    }

    @QueryMapping
    public Mono<List<ItemRequest>> getOthersItemRequests(@Argument @PositiveOrZero Integer from,
                                                         @Argument @Positive Integer size,
                                                         @ContextValue @Positive Long userId,
                                                         @ContextValue @NotBlank String query,
                                                         @ContextValue @NotBlank String methodName) {
        log.info("Пользователь с ID={} запросил список запросов на вещи других пользователей, from={}, size={}",
                userId, from, size);
        return httpGraphQlClient.mutate()
                .header(USER_ID_HEADER, userId.toString())
                .build()
                .document(query)
                .retrieve(methodName)
                .toEntityList(ItemRequest.class);
    }
}
