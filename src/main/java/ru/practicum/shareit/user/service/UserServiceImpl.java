package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto getById(long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("user with id: %d does not exist yet", id)));

        return userMapper.toUserDto(user);
    }

    @Override
    public UserDto save(UserDto userDto) {
        throwIfEmailDuplicate(userDto);

        User user = userRepository.save(userMapper.toUser(userDto));

        return userMapper.toUserDto(user);
    }

    @Override
    public @Valid UserDto update(long id, UserDto userDto) {
        UserDto oldUser = getById(id);

        throwIfEmailDuplicate(userDto);

        if (userDto.getEmail() == null) {
            userDto.setEmail(oldUser.getEmail());
        }
        if (userDto.getName() == null) {
            userDto.setName(oldUser.getName());
        }

        User user = userRepository.update(id, userMapper.toUser(userDto));

        return userMapper.toUserDto(user);
    }

    @Override
    public void delete(long id) {
        getById(id);
        userRepository.delete(id);
    }

    private void throwIfEmailDuplicate(UserDto userDto) {
        userRepository.findAll()
                .stream()
                .map(User::getEmail)
                .filter(email -> email.equals(userDto.getEmail()))
                .findFirst()
                .ifPresent(email -> {
                    throw new BadRequestException(String.format("user with email: %s already exist", email));
                });
    }
}
