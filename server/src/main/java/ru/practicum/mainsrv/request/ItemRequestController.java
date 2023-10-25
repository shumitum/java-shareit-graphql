package ru.practicum.mainsrv.request;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import ru.practicum.mainsrv.request.dto.InputItemRequestDto;
import ru.practicum.mainsrv.request.dto.ItemRequestDto;

import java.util.List;

import static ru.practicum.mainsrv.item.ItemController.USER_ID_HEADER;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ItemRequestController {
    private final ItemRequestService itemRequestService;
    private final HttpServletRequest request;

    @MutationMapping
    public ItemRequest createItemRequest(@Argument @Valid InputItemRequestDto itemRequestDto) {
        Long userId = Long.parseLong(request.getHeader(USER_ID_HEADER));
        log.info("Новый запрос на добавление item: {}", itemRequestDto);
        ItemRequest itemRequest = itemRequestService.createItemRequest(itemRequestDto, userId);
        log.info("Создан новый item: {}", itemRequest);
        return itemRequest;
    }

    @QueryMapping
    public ItemRequestDto getItemRequestById(@Argument @Positive Long requestId) {
        Long userId = Long.parseLong(request.getHeader(USER_ID_HEADER));
        log.info("Пользователем с ID={} запрошен request на вещь с ID={}", userId, requestId);
        ItemRequestDto itemRequest = itemRequestService.getItemRequestById(requestId, userId);
        log.info("Запрошенный request с ID={}: {}", requestId, itemRequest);
        return itemRequest;
    }

    @QueryMapping
    public List<ItemRequestDto> getUserItemRequests() {
        Long userId = Long.parseLong(request.getHeader(USER_ID_HEADER));
        log.info("Пользователем с ID={} запрошены собственные requests на добавление items", userId);
        return itemRequestService.getUserItemRequests(userId);
    }

    @QueryMapping
    public  List<ItemRequestDto> getOthersItemRequests(@Argument @PositiveOrZero Integer from,
                                                       @Argument @Positive Integer size) {
        Long userId = Long.parseLong(request.getHeader(USER_ID_HEADER));
        log.info("Пользователь с ID={} запросил список запросов на вещи других пользователей", userId);
        return itemRequestService.getOthersItemRequests(userId, from, size);
    }
}
