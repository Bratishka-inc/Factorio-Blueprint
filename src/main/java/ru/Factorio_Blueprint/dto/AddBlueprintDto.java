package ru.Factorio_Blueprint.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;


@Getter
@Setter
public class AddBlueprintDto {

    @NotBlank(message = "Название обязательно")
    @Size(min = 3, max = 100, message = "Название должно быть от 3 до 100 символов")
    private String name;

    @NotBlank(message = "Описание обязательно")
    private String description;

    @NotBlank(message = "Код чертежа обязателен")
    @Pattern(
            regexp = "^[A-Za-z0-9]+$",
            message = "Код чертежа может содержать только латинские буквы и цифры без пробелов"
    )
    private String blueprintString;

    private String imageUrl;
}
