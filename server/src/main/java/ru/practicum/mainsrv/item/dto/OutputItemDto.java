package ru.practicum.mainsrv.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.mainsrv.item.comment.dto.CommentDto;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OutputItemDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    //private BookingInfoDto lastBooking;
    //private BookingInfoDto nextBooking;
    private List<CommentDto> comments;
    private Long requestId;
}