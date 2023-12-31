package ru.practicum.gateway.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.gateway.validationgroup.Create;
import ru.practicum.gateway.validationgroup.Update;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    @NotBlank(groups = Create.class, message = "Поле Имя не должно быть пустым")
    @Size(max = 255)
    private String name;
    @NotBlank(groups = Create.class, message = "Поле email не должно быть пустым")
    @Email(groups = {Create.class, Update.class})
    @Size(max = 255)
    private String email;
}