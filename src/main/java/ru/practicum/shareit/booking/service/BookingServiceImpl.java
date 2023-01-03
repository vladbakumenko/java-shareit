package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.booking.dto.BookingCreationDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import javax.validation.Valid;
import java.util.LinkedList;
import java.util.List;

@Validated
@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;

    @Override
    public @Valid BookingDto create(Long userId, BookingCreationDto bookingCreationDto) {
        Item item = itemRepository.findById(bookingCreationDto.getItemId())
                .orElseThrow(() -> new NotFoundException(String.format("item with id: %d not found",
                        bookingCreationDto.getItemId())));
        if (!item.getAvailable()) {
            throw new BadRequestException(String.format("item with id: %d is currently unavailable", item.getId()));
        }
        if (item.getOwner().equals(userId)) {
            throw new NotFoundException("the owner cannot book his own item");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("user with id: %d does not exist yet", userId)));
        Booking booking = bookingMapper.toBooking(bookingCreationDto, item, user);
        booking.setStatus(Status.WAITING);
        bookingRepository.save(booking);

        return bookingMapper.toBookingDto(booking);
    }

    @Override
    public @Valid BookingDto approve(Long bookingId, Boolean approved, Long userId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException(String.format("booking with id: %d does not found", bookingId)));

        if (!booking.getItem().getOwner().equals(userId)) {
            throw new NotFoundException(String.format("this item is not owned by the user with id: %d", userId));
        }
        if (booking.getStatus().equals(Status.APPROVED) && approved) {
            throw new BadRequestException("booking is already approved");
        }

        if (approved) {
            booking.setStatus(Status.APPROVED);
        } else {
            booking.setStatus(Status.REJECTED);
        }

        bookingRepository.save(booking);

        return bookingMapper.toBookingDto(booking);
    }

    @Override
    public @Valid BookingDto getById(Long bookingId, Long userId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException(String.format("booking with id: %d does not found", bookingId)));

        if (!booking.getBooker().getId().equals(userId) && !booking.getItem().getOwner().equals(userId)) {
            throw new NotFoundException("the booking cannot be viewed by a non-owner of the item" +
                    " or a non-creator of the booking");
        }

        return bookingMapper.toBookingDto(booking);
    }

    @Override
    public List<BookingDto> getAllByBooker(String state, Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException(String.format("user with id: %d does not exist yet", userId));
        }

        List<BookingDto> result = new LinkedList<>();

        switch (state) {
            case "ALL":
                result = bookingMapper.toListOfBookingDto(bookingRepository.findAllByBookerId(userId));
                break;
            case "CURRENT":
                result = bookingMapper.toListOfBookingDto(bookingRepository
                        .findAllByBookerIdAndCurrentState(userId));
                break;
            case "PAST":
                result = bookingMapper.toListOfBookingDto(bookingRepository
                        .findAllByBookerIdAndPastState(userId));
                break;
            case "FUTURE":
                result = bookingMapper.toListOfBookingDto(bookingRepository
                        .findAllByBookerIdAndFutureState(userId));
                break;
            case "WAITING":
                result = bookingMapper.toListOfBookingDto(bookingRepository
                        .findAllByBookerIdAndWaitingOrRejectedState(userId, Status.WAITING));
                break;
            case "REJECTED":
                result = bookingMapper.toListOfBookingDto(bookingRepository
                        .findAllByBookerIdAndWaitingOrRejectedState(userId, Status.REJECTED));
                break;
        }
        return result;
    }

    @Override
    public List<BookingDto> getAllByOwner(String state, Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException(String.format("user with id: %d does not exist yet", userId));
        }

        List<BookingDto> result = new LinkedList<>();

        switch (state) {
            case "ALL":
                result = bookingMapper.toListOfBookingDto(bookingRepository.findAllByOwnerId(userId));
                break;
            case "CURRENT":
                result = bookingMapper.toListOfBookingDto(bookingRepository
                        .findAllByOwnerIdAndCurrentState(userId));
                break;
            case "PAST":
                result = bookingMapper.toListOfBookingDto(bookingRepository
                        .findAllByOwnerIdAndPastState(userId));
                break;
            case "FUTURE":
                result = bookingMapper.toListOfBookingDto(bookingRepository
                        .findAllByOwnerIdAndFutureState(userId));
                break;
            case "WAITING":
                result = bookingMapper.toListOfBookingDto(bookingRepository
                        .findAllByOwnerIdAndWaitingOrRejectedState(userId, Status.WAITING));
                break;
            case "REJECTED":
                result = bookingMapper.toListOfBookingDto(bookingRepository
                        .findAllByOwnerIdAndWaitingOrRejectedState(userId, Status.REJECTED));
                break;
        }
        return result;
    }
}
