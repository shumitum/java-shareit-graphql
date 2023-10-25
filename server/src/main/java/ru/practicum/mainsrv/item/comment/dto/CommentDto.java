package ru.practicum.mainsrv.item.comment.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {
    //@NotBlank(groups = Create.class, message = "Поле Комментарий не должно быть пустым")
    @Size(max = 255)
    private String text;
}