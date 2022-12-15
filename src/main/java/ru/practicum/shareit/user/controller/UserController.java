package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public UserDto save(@RequestBody @Valid UserDto userDto) {
        return userService.save(userDto);
    }

    @PatchMapping("/{id}")
    public UserDto update(@PathVariable long id,
                          @RequestBody UserDto userDto) {
        return userService.update(id, userDto);
    }

    @GetMapping("/{id}")
    public UserDto getById(@PathVariable long id) {
        return userService.getById(id);
    }

    @GetMapping
    public List<UserDto> getAll() {
        return userService.getAllUsers();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        userService.delete(id);
    }
}
