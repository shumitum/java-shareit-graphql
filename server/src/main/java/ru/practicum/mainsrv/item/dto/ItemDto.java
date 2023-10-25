package ru.practicum.mainsrv.item.dto;

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
    //@NotBlank(groups = Create.class, message = "Поле Имя не должно быть пустым")
    @Size(max = 255)
    private String name;
    //@NotBlank(groups = Create.class, message = "Поле Описание не должно быть пустым")
    @Size(max = 255)
    private String description;
    //@NotNull(groups = Create.class, message = "Поле Доступность к аренде не должно быть пустым")
    private Boolean available;
    private Long requestId;
}