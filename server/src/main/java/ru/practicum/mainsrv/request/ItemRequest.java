package ru.practicum.mainsrv.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import ru.practicum.mainsrv.user.User;
import ru.practicum.mainsrv.utils.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@Builder
@Entity
@Table(name = "requests", schema = "public")
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequest {
    @Id
    @Column(name = "request_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank
    @Column(name = "description")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester_id", referencedColumnName = "user_id")
    @ToString.Exclude
    private User requester;

    @NotNull
    @Column(name = "created")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateTimeFormat.DATE_TIME_PATTERN)
    private LocalDateTime created;
}