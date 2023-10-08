package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    User createUser(UserDto user);

    User getUserById(Long id);

    List<User> getAllUsers();

    User updateUser(Long userId, UserDto userDto);

    void deleteUserById(Long id);

    void checkUserExistence(Long userId);

    User findUserById(Long userId);
}