package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.Instant;

@Builder
@Data
public class CommentCreationDto {
    @NotBlank
    private String text;
    private Instant created;
}
