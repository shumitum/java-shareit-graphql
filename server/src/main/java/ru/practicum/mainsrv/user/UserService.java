package ru.practicum.mainsrv.user;

import ru.practicum.mainsrv.user.dto.UserDto;

import java.util.List;

public interface UserService {
    User createUser(UserDto user);

    User getUserById(Long id);

    List<User> getAllUsers(Integer from, Integer size);

    User updateUser(Long userId, UserDto userDto);

    Boolean deleteUserById(Long id);

    void checkUserExistence(Long userId);

    User findUserById(Long userId);
}