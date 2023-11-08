package ru.practicum.mainsrv.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import ru.practicum.mainsrv.booking.BookingState;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingsParam {
    private Long userId;
    private BookingState state;
    private Integer from;
    private Integer size;
    private PageRequest page;

    public static BookingsParam of(long userId, BookingState state, Integer from, Integer size) {
        return BookingsParam.builder()
                .userId(userId)
                .state(state)
                .from(from)
                .size(size)
                .build();
    }

    public PageRequest getPage() {
        return PageRequest.of(from > 0 ? from / size : 0, size);
    }
}