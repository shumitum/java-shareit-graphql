package ru.practicum.gateway.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.gateway.validationgroup.Create;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequestDto {
    @Size(max = 255)
    @NotBlank(groups = Create.class, message = "Поле описание не должно быть пустым")
    private String description;
}