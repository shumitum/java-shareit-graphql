package ru.practicum.gateway.booking;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class BookingDto {
    @NotNull(message = "Поле ID вещи бронирования не должно быть пустым")
    private long itemId;
    @FutureOrPresent
    @NotNull(message = "Поле Время начала бронирования не должно быть пустым")
    private LocalDateTime start;
    @Future
    @NotNull(message = "Поле Время конца бронирования не должно быть пустым")
    private LocalDateTime end;
}