package ru.practicum.shareit.item.dto;

import jdk.jfr.BooleanFlag;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ItemRequestDto {
    private int id;
    @NotBlank(message = "name field cannot be empty")
    private String name;
    @NotBlank(message = "description field cannot be empty")
    private String description;
    private int owner;
    @BooleanFlag
    @NotNull(message = "available field cannot be empty")
    private Boolean available;
    @Positive
    private Long requestId;
}
