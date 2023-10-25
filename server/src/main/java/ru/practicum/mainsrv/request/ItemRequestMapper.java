package ru.practicum.mainsrv.request;

import org.mapstruct.Mapper;
import ru.practicum.mainsrv.request.dto.InputItemRequestDto;
import ru.practicum.mainsrv.request.dto.ItemRequestDto;

@Mapper(componentModel = "spring")
public interface ItemRequestMapper {
    ItemRequestDto toItemRequestDto(ItemRequest itemRequest);

    ItemRequest toItemRequest(ItemRequestDto itemRequestDto);

    ItemRequest toItemRequest(InputItemRequestDto inputItemRequestDto);
}