package ru.practicum.gateway.user;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.client.HttpGraphQlClient;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.ContextValue;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import reactor.core.publisher.Mono;
import ru.practicum.gateway.validationgroup.Create;
import ru.practicum.gateway.validationgroup.Update;
import ru.practicum.mainsrv.user.User;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class UserController {

    private final HttpGraphQlClient httpGraphQlClient;

    @MutationMapping
    public Mono<User> createUser(@Argument @Validated(Create.class) UserDto user,
                                 @ContextValue String query,
                                 @ContextValue String methodName) {
        log.info("Запрос на создание пользователя {}", user);
        return httpGraphQlClient.document(query)
                .retrieve(methodName)
                .toEntity(User.class);
    }

    @QueryMapping
    public Mono<User> getUserById(@Argument @Positive Long id,
                                  @ContextValue String query,
                                  @ContextValue String methodName) {
        log.info("Запрошен пользователь с ID={}", id);
        return httpGraphQlClient.document(query)
                .retrieve(methodName)
                .toEntity(User.class);
    }

    @QueryMapping
    public Mono<List<User>> getAllUsers(@Argument @PositiveOrZero Integer from,
                                        @Argument @Positive Integer size,
                                        @ContextValue String query,
                                        @ContextValue String methodName) {
        log.info("Запрошен список всех пользователей, from={}, size={}", from, size);
        return httpGraphQlClient.document(query)
                .retrieve(methodName)
                .toEntityList(User.class);
    }

    @MutationMapping
    public Mono<User> updateUser(@Argument @Positive Long id,
                                 @Argument @Validated(Update.class) UserDto user,
                                 @ContextValue String query,
                                 @ContextValue String methodName) {
        log.info("Запрос на обновление пользователя с ID={}, {}", id, user);
        return httpGraphQlClient.document(query)
                .retrieve(methodName)
                .toEntity(User.class);
    }

    @MutationMapping
    public Mono<Boolean> deleteUserById(@Argument @Positive Long id,
                                        @ContextValue String query,
                                        @ContextValue String methodName) {
        log.info("Запрос на удаление пользователя с ID={}", id);
        return httpGraphQlClient.document(query)
                .retrieve(methodName)
                .toEntity(Boolean.class);
    }
}