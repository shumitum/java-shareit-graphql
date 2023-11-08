package ru.practicum.mainsrv.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.mainsrv.booking.dto.BookingDto;
import ru.practicum.mainsrv.booking.dto.BookingsParam;
import ru.practicum.mainsrv.exception.InvalidArgumentException;
import ru.practicum.mainsrv.item.Item;
import ru.practicum.mainsrv.item.ItemService;
import ru.practicum.mainsrv.user.User;
import ru.practicum.mainsrv.user.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserService userService;
    private final ItemService itemService;
    private final BookingMapper bookingMapper;

    @Transactional
    @Override
    public Booking createBooking(BookingDto bookingDto, Long bookerId) {
        final Item item = itemService.findItemById(bookingDto.getItemId());
        final User booker = userService.findUserById(bookerId);
        if (item.getAvailable().equals(Boolean.FALSE)) {
            throw new InvalidArgumentException("Вещь недоступна для бронирования");
        }
        if (bookingDto.getEnd().isBefore(bookingDto.getStart())) {
            throw new InvalidArgumentException("Конец бронирования должен быть позже времени начала бронирования");
        }
        if (bookingDto.getEnd().equals(bookingDto.getStart())) {
            throw new InvalidArgumentException("Время начала и конца бронирования не должны совпадать");
        }
        if (bookerId.equals(item.getOwner().getId())) {
            throw new NoSuchElementException("Бронирование собственных вещей невозможно");
        }
        final Booking newBooking = bookingMapper.toBooking(bookingDto);
        newBooking.setBooker(booker);
        newBooking.setItem(item);
        newBooking.setStatus(BookingStatus.WAITING);
        return bookingRepository.save(newBooking);
    }

    @Transactional
    @Override
    public Booking approveBooking(Long bookingId, Boolean approved, Long userId) {
        userService.checkUserExistence(userId);
        final Booking booking = findBookingById(bookingId);
        if (!Objects.equals(userId, booking.getItem().getOwner().getId())) {
            throw new NoSuchElementException("Только владелец вещи может подтверждать/отклонять бронирование");
        }
        if (booking.getStatus().equals(BookingStatus.WAITING)) {
            if (approved.equals(Boolean.TRUE)) {
                booking.setStatus(BookingStatus.APPROVED);
            } else {
                booking.setStatus(BookingStatus.REJECTED);
            }
        } else {
            throw new InvalidArgumentException(String.format("Заявка на бронирование уже %s владельцем", booking.getStatus()));
        }
        return booking;
    }

    @Transactional(readOnly = true)
    @Override
    public Booking getBookingById(Long bookingId, Long userId) {
        userService.checkUserExistence(userId);
        if (userId.equals(findBookingById(bookingId).getBooker().getId()) ||
                userId.equals(findBookingById(bookingId).getItem().getOwner().getId())) {
            return findBookingById(bookingId);
        } else {
            throw new NoSuchElementException("Получение данных о бронировании может только владелец или арендатор вещи");
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<Booking> getBookingByUserIdAndState(BookingsParam params) {
        final Long userId = params.getUserId();
        userService.checkUserExistence(userId);
        /*List<Booking> bookings =*/
        return switch (params.getState()) {
            case ALL -> bookingRepository.findByBookerIdOrderByStartDesc(userId, params.getPage());
            case CURRENT -> bookingRepository.findByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(userId,
                    LocalDateTime.now(), LocalDateTime.now(), params.getPage());
            case PAST -> bookingRepository.findByBookerIdAndEndBeforeOrderByStartDesc(userId,
                    LocalDateTime.now(), params.getPage());
            case FUTURE -> bookingRepository.findByBookerIdAndStartAfterOrderByStartDesc(userId,
                    LocalDateTime.now(), params.getPage());
            case WAITING -> bookingRepository.findByBookerIdAndStatusOrderByStartDesc(userId,
                    BookingStatus.WAITING, params.getPage());
            case REJECTED -> bookingRepository.findByBookerIdAndStatusOrderByStartDesc(userId,
                    BookingStatus.REJECTED, params.getPage());
            default -> throw new InvalidArgumentException("Unknown state: UNSUPPORTED_STATUS");
        };
    }

    @Override
    @Transactional(readOnly = true)
    public List<Booking> getOwnerItemsByState(BookingsParam params) {
        final Long userId = params.getUserId();
        userService.checkUserExistence(userId);
        /*        List<Booking> bookings =*/
        return switch (params.getState()) {
            case ALL -> bookingRepository.findByItemOwnerIdOrderByStartDesc(userId, params.getPage());
            case CURRENT -> bookingRepository.findByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(userId,
                    LocalDateTime.now(), LocalDateTime.now(), params.getPage());
            case PAST -> bookingRepository.findByItemOwnerIdAndEndBeforeOrderByStartDesc(userId,
                    LocalDateTime.now(), params.getPage());
            case FUTURE -> bookingRepository.findByItemOwnerIdAndStartAfterOrderByStartDesc(userId,
                    LocalDateTime.now(), params.getPage());
            case WAITING -> bookingRepository.findByItemOwnerIdAndStatusOrderByStartDesc(userId,
                    BookingStatus.WAITING, params.getPage());
            case REJECTED -> bookingRepository.findByItemOwnerIdAndStatusOrderByStartDesc(userId,
                    BookingStatus.REJECTED, params.getPage());
            default -> throw new InvalidArgumentException("Unknown state: UNSUPPORTED_STATUS");
        };
    }

    @Override
    public Booking findBookingById(Long bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NoSuchElementException(String.format("Бронирование с ID=%d не существует", bookingId)));
    }
}