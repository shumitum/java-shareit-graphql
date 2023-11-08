package ru.practicum.mainsrv.request;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.ContextValue;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;
import ru.practicum.mainsrv.request.dto.ItemRequestDto;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ItemRequestController {
    private final ItemRequestService itemRequestService;
    private final HttpServletRequest request;

    @MutationMapping
    public Mono<ItemRequest> createItemRequest(@Argument ItemRequestDto itemRequestDto,
                                               @ContextValue Long userId) {
        log.info("Запрос на создание нового itemа: {}", itemRequestDto);
        ItemRequest itemRequest = itemRequestService.createItemRequest(itemRequestDto, userId);
        log.info("Создан новый item: {}", itemRequest);
        return Mono.justOrEmpty(itemRequest);
    }

    @QueryMapping
    public Mono<ItemRequest> getItemRequestById(@Argument @Positive Long requestId,
                                                @ContextValue Long userId) {
        log.info("Пользователем с ID={} запрошен request на вещь с ID={}", userId, requestId);
        ItemRequest itemRequest = itemRequestService.getItemRequestById(requestId, userId);
        log.info("Запрошенный request с ID={}: {}", requestId, itemRequest);
        return Mono.justOrEmpty(itemRequest);
    }

    @QueryMapping
    public Mono<List<ItemRequest>> getUserItemRequests(@ContextValue Long userId) {
        log.info("Пользователем с ID={} запрошены собственные requests на добавление items", userId);
        return Mono.justOrEmpty(itemRequestService.getUserItemRequests(userId));
    }

    @QueryMapping
    public Mono<List<ItemRequest>> getOthersItemRequests(@Argument @PositiveOrZero Integer from,
                                                         @Argument @Positive Integer size,
                                                         @ContextValue Long userId) {
        log.info("Пользователь с ID={} запросил список запросов на вещи других пользователей", userId);
        return Mono.justOrEmpty(itemRequestService.getOthersItemRequests(userId, from, size));
    }
}
