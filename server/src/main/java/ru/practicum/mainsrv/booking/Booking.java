package ru.practicum.mainsrv.booking;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import ru.practicum.mainsrv.item.Item;
import ru.practicum.mainsrv.user.User;

import java.time.LocalDateTime;

import static ru.practicum.mainsrv.utils.DateTimeFormat.DATE_TIME_PATTERN;

@Data
@Builder
@Entity
@Table(name = "bookings", schema = "public")
@AllArgsConstructor
@NoArgsConstructor
public class Booking {
    @Id
    @Column(name = "booking_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @Column(name = "start_time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_TIME_PATTERN)
    private LocalDateTime start;

    @NotNull
    @Column(name = "end_time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_TIME_PATTERN)
    private LocalDateTime end;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", referencedColumnName = "item_id")
    @ToString.Exclude
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booker_id", referencedColumnName = "user_id")
    @ToString.Exclude
    private User booker;

    @NotNull
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private BookingStatus status;
}