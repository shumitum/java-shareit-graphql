package ru.practicum.mainsrv.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.ContextValue;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;
import ru.practicum.mainsrv.booking.dto.BookingDto;
import ru.practicum.mainsrv.booking.dto.BookingsParam;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @MutationMapping
    public Mono<Booking> createBooking(@Argument BookingDto bookingDto,
                                       @ContextValue Long userId) {

        Booking newBooking = bookingService.createBooking(bookingDto, userId);
        log.info("Создано новое бронирование: {}", newBooking);
        return Mono.justOrEmpty(newBooking);
    }

    @MutationMapping
    public Mono<Booking> approveBooking(@Argument Long bookingId,
                                        @Argument Boolean approved,
                                        @ContextValue Long userId) {
        Booking uprovedBooking = bookingService.approveBooking(bookingId, approved, userId);
        log.info("Статус бронирования вещи с ID={} изменен на: {}", bookingId, approved);
        return Mono.justOrEmpty(uprovedBooking);
    }

    @QueryMapping
    public Mono<Booking> getBookingById(@Argument Long bookingId,
                                        @ContextValue Long userId) {
        log.info("Пользователем с ID={} запрошено бронирование с ID={}", userId, bookingId);
        Booking booking = bookingService.getBookingById(bookingId, userId);
        log.info("Запрошенное по ID={} бронирование: {}", bookingId, booking);
        return Mono.justOrEmpty(booking);
    }

    @QueryMapping
    public Mono<List<Booking>> getUserBookingsByState(@Argument BookingState state,
                                                      @Argument Integer from,
                                                      @Argument Integer size,
                                                      @ContextValue Long userId) {
        log.info("Пользователем с ID={} запрошены бронирования со статусом: {}, from={}, size={}", userId, state, from, size);
        return Mono.justOrEmpty(bookingService.getBookingByUserIdAndState(BookingsParam.of(userId, state, from, size)));
    }

    @QueryMapping
    public Mono<List<Booking>> getUserItemsBookingsByState(@Argument BookingState state,
                                                           @Argument Integer from,
                                                           @Argument Integer size,
                                                           @ContextValue Long userId) {
        log.info("Пользователь с ID={} запросил список бронирований всех своих вещей со статусом: {}, from={}, size={}",
                userId, state, from, size);
        return Mono.justOrEmpty(bookingService.getOwnerItemsByState(BookingsParam.of(userId, state, from, size)));
    }
}