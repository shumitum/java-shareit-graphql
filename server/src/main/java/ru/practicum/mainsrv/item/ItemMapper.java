package ru.practicum.mainsrv.item;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.mainsrv.item.dto.ItemDto;
import ru.practicum.mainsrv.item.dto.OutputItemDto;

@Mapper(componentModel = "spring")
public interface ItemMapper {
    Item toItem(ItemDto itemDto);

    @Mapping(target = "requestId", source = "request.id")
    OutputItemDto toOutputItemDto(Item item);
}