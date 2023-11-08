package ru.practicum.mainsrv.booking;

import org.mapstruct.Mapper;
import ru.practicum.mainsrv.booking.dto.BookingDto;
import ru.practicum.mainsrv.item.ItemMapper;
import ru.practicum.mainsrv.user.UserMapper;


@Mapper(componentModel = "spring", uses = {ItemMapper.class, UserMapper.class})
public interface BookingMapper {
    Booking toBooking(BookingDto bookingDto);
}