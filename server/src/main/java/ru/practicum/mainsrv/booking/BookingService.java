package ru.practicum.mainsrv.booking;

import ru.practicum.mainsrv.booking.dto.BookingDto;
import ru.practicum.mainsrv.booking.dto.BookingsParam;

import java.util.List;

public interface BookingService {
    Booking createBooking(BookingDto bookingDto, Long bookerId);

    Booking approveBooking(Long bookingId, Boolean approved, Long userId);

    Booking getBookingById(Long bookingId, Long userId);

    List<Booking> getBookingByUserIdAndState(BookingsParam params);

    List<Booking> getOwnerItemsByState(BookingsParam params);

    Booking findBookingById(Long bookingId);
}