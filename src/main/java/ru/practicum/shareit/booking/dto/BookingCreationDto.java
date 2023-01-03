package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Future;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

@Data
@Builder
public class BookingCreationDto {
    @Positive
    private Long itemId;
    @Future
    private LocalDateTime start;
    @Future
    private LocalDateTime end;
}
