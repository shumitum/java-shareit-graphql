package ru.practicum.gateway.booking;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
import reactor.core.publisher.Mono;
import ru.practicum.mainsrv.booking.Booking;
import ru.practicum.mainsrv.booking.BookingState;

import java.util.List;

import static ru.practicum.mainsrv.interceptor.RequestHeaderInterceptor.USER_ID_HEADER;

@Slf4j
@Controller
@RequiredArgsConstructor
public class BookingController {

    private final HttpGraphQlClient httpGraphQlClient;

    @MutationMapping
    public Mono<Booking> createBooking(@Argument @Valid BookingDto bookingDto,
                                       @ContextValue @Positive Long userId,
                                       @ContextValue @NotBlank String query,
                                       @ContextValue @NotBlank String methodName) {
        log.info("Новый запрос на бронирование: {}", bookingDto);
        return httpGraphQlClient.mutate()
                .header(USER_ID_HEADER, userId.toString())
                .build()
                .document(query)
                .retrieve(methodName)
                .toEntity(Booking.class);
    }

    @MutationMapping
    public Mono<Booking> approveBooking(@Argument @Positive Long bookingId,
                                        @Argument @NotNull Boolean approved,
                                        @ContextValue @Positive Long userId,
                                        @ContextValue @NotBlank String query,
                                        @ContextValue @NotBlank String methodName) {
        log.info("Запрос на изменение статуса заявки на бронирование с ID={} на: {}", bookingId, approved);
        return httpGraphQlClient.mutate()
                .header(USER_ID_HEADER, userId.toString())
                .build()
                .document(query)
                .retrieve(methodName)
                .toEntity(Booking.class);
    }

    @QueryMapping
    public Mono<Booking> getBookingById(@Argument @Positive Long bookingId,
                                        @ContextValue @Positive Long userId,
                                        @ContextValue @NotBlank String query,
                                        @ContextValue @NotBlank String methodName) {
        log.info("Пользователем с ID={} запрошена заявка на бронирование с ID={}", userId, bookingId);
        return httpGraphQlClient.mutate()
                .header(USER_ID_HEADER, userId.toString())
                .build()
                .document(query)
                .retrieve(methodName)
                .toEntity(Booking.class);
    }

    @QueryMapping
    public Mono<List<Booking>> getUserBookingsByState(@Argument @NotNull BookingState state,
                                                      @Argument @PositiveOrZero Integer from,
                                                      @Argument @Positive Integer size,
                                                      @ContextValue @Positive Long userId,
                                                      @ContextValue @NotBlank String query,
                                                      @ContextValue @NotBlank String methodName) {
        log.info("Пользователем с ID={} запрошены заявки на бронирование со статусом: {}, from={}, size={}", userId, state, from, size);
        return httpGraphQlClient.mutate()
                .header(USER_ID_HEADER, userId.toString())
                .build()
                .document(query)
                .retrieve(methodName)
                .toEntityList(Booking.class);
    }

    @QueryMapping
    public Mono<List<Booking>> getUserItemsBookingsByState(@Argument @NotNull BookingState state,
                                                           @Argument @PositiveOrZero Integer from,
                                                           @Argument @Positive Integer size,
                                                           @ContextValue @Positive Long userId,
                                                           @ContextValue @NotBlank String query,
                                                           @ContextValue @NotBlank String methodName) {
        log.info("Пользователь с ID={} запросил список бронирований всех своих вещей со статусом: {}, from={}, size={}",
                userId, state, from, size);
        return httpGraphQlClient.mutate()
                .header(USER_ID_HEADER, userId.toString())
                .build()
                .document(query)
                .retrieve(methodName)
                .toEntityList(Booking.class);
    }
}