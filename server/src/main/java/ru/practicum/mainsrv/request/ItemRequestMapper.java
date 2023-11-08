package ru.practicum.mainsrv.request;

import org.mapstruct.Mapper;
import ru.practicum.mainsrv.request.dto.ItemRequestDto;

@Mapper(componentModel = "spring")
public interface ItemRequestMapper {
    ItemRequest toItemRequest(ItemRequestDto itemRequestDto);
}