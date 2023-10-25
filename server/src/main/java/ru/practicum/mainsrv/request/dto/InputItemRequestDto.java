package ru.practicum.mainsrv.request.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InputItemRequestDto {
    @Size(max = 255)
    //@NotBlank(groups = Create.class, message = "Поле описание не должно быть пустым")
    private String description;
}