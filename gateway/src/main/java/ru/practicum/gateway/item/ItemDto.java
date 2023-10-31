package ru.practicum.gateway.item;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemDto {
    @NotBlank(message = "Поле Имя не должно быть пустым")
    @Size(max = 255)
    private String name;
    @NotBlank(message = "Поле Описание не должно быть пустым")
    @Size(max = 255)
    private String description;
    @NotNull(message = "Поле Доступность к аренде не должно быть пустым")
    private Boolean available;
    private Long requestId;
}