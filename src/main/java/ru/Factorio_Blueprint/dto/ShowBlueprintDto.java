package ru.Factorio_Blueprint.dto;

import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;

@Getter
@Setter
public class ShowBlueprintDto implements Serializable {
    private Long id;
    private String name;
    private String description;
}
