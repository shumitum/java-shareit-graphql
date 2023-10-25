package ru.practicum.mainsrv.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;
import ru.practicum.mainsrv.user.dto.UserDto;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @MutationMapping
    public Mono<User> createUser(@Argument UserDto user) {
        log.info("Запрос на создание пользователя {}", user);
        User newUser = userService.createUser(user);
        log.info("Создан пользователь: {}", newUser);
        return Mono.justOrEmpty(newUser);
    }

    @QueryMapping
    public Mono<User> getUserById(@Argument Long id) {
        log.info("Запрошен пользователь с ID={}", id);
        return Mono.justOrEmpty(userService.getUserById(id));
    }

    @QueryMapping
    public Mono<List<User>> getAllUsers(@Argument Integer from,
                                        @Argument Integer size) {
        log.info("Запрошен список всех пользователей");
        return Mono.justOrEmpty(userService.getAllUsers(from, size));
    }

    @MutationMapping
    public Mono<User> updateUser(@Argument Long id,
                                 @Argument UserDto user) {
        log.info("Запрос на обновление пользователя с ID={}, {}", id, user);
        return Mono.justOrEmpty(userService.updateUser(id, user));
    }

    @MutationMapping
    public Mono<Boolean> deleteUserById(@Argument Long id) {
        log.info("Запрос на удаление пользователя с ID={}", id);
        return Mono.justOrEmpty(userService.deleteUserById(id));
    }
}
