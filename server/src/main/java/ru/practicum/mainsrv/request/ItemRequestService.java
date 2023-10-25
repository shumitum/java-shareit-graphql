package ru.practicum.mainsrv.request;

import ru.practicum.mainsrv.request.dto.InputItemRequestDto;
import ru.practicum.mainsrv.request.dto.ItemRequestDto;

import java.util.List;

public interface ItemRequestService {

    ItemRequest createItemRequest(InputItemRequestDto itemRequestDto, Long requesterId);

    ItemRequestDto getItemRequestById(Long requestId, Long userId);

    List<ItemRequestDto> getUserItemRequests(Long userId);

    List<ItemRequestDto> getOthersItemRequests(Long userId, Integer from, Integer size);
}