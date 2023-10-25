package ru.practicum.mainsrv.user;

import org.mapstruct.Mapper;
import ru.practicum.mainsrv.user.dto.UserDto;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserDto userDto);

    UserDto toUserDto(User user);
}