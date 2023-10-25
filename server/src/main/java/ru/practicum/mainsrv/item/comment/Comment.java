package ru.practicum.mainsrv.item.comment;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import ru.practicum.mainsrv.item.Item;
import ru.practicum.mainsrv.user.User;
import ru.practicum.mainsrv.utils.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@Builder
@Entity
@Table(name = "comments", schema = "public")
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    @Id
    @Column(name = "comment_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank
    @Column(name = "text")
    private String text;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", referencedColumnName = "item_id")
    @ToString.Exclude
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", referencedColumnName = "user_id")
    @ToString.Exclude
    private User author;

    @NotNull
    @Column(name = "created")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateTimeFormat.DATE_TIME_PATTERN)
    private LocalDateTime created;
}