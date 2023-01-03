package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingCreationDto;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

public interface BookingService {
    BookingDto create(Long userId, BookingCreationDto bookingCreationDto);

    BookingDto approve(Long bookingId, Boolean approved, Long userId);

    BookingDto getById(Long bookingId, Long userId);

    List<BookingDto> getAllByBooker(String state, Long userId);

    List<BookingDto> getAllByOwner(String state, Long userId);
}
