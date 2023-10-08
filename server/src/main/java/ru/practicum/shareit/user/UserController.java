package ru.practicum.shareit.user;

import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.validationgroup.Create;
import ru.practicum.shareit.validationgroup.Update;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @MutationMapping
    public User createUser(@Argument @Validated(Create.class) UserDto user) {
        log.info("Запрос на создание пользователя {}", user);
        User newUser = userService.createUser(user);
        log.info("Создан пользователь: {}", newUser);
        return newUser;
    }

    @QueryMapping
    public User getUserById(@Argument @Positive Long id) {
        log.info("Запрошен пользователь с ID={}", id);
        return userService.getUserById(id);
    }

    @QueryMapping
    public List<User> getAllUsers() {
        log.info("Запрошен список всех пользователей");
        return userService.getAllUsers();
    }

    @MutationMapping
    public User updateUser(@Argument @Positive Long id,
                           @Argument @Validated(Update.class) UserDto user) {
        return userService.updateUser(id, user);
    }

    @MutationMapping
    public void deleteUserById(@Argument @Positive Long id) {
        userService.deleteUserById(id);
    }
}
