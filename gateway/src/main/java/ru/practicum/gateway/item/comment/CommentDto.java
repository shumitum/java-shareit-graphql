package ru.practicum.gateway.item.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {
    @NotBlank(message = "Поле Комментарий не должно быть пустым")
    @Size(max = 255)
    private String text;
}