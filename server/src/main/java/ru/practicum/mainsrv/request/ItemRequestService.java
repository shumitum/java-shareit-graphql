package ru.practicum.mainsrv.request;

import ru.practicum.mainsrv.request.dto.ItemRequestDto;
import java.util.List;

public interface ItemRequestService {

    ItemRequest createItemRequest(ItemRequestDto itemRequestDto, Long requesterId);

    ItemRequest getItemRequestById(Long requestId, Long userId);

    List<ItemRequest> getUserItemRequests(Long userId);

    List<ItemRequest> getOthersItemRequests(Long userId, Integer from, Integer size);
}