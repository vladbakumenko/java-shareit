package ru.practicum.shareit.user.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class UserRepositoryImpl implements UserRepository {

    private int lastId = 0;
    private List<User> users = new ArrayList<>();

    @Override
    public User save(User user) {
        user.setId(getId());
        users.add(user);
        return user;
    }

    @Override
    public Optional<User> findById(long id) {
        return users.stream()
                .filter(user -> user.getId() == id)
                .findFirst();
    }

    @Override
    public List<User> findAll() {
        return users;
    }

    @Override
    public User update(long id, User user) {
        user.setId(id);

        users.removeIf(u -> u.getId() == id);
        users.add(user);

        return user;
    }

    @Override
    public void delete(long id) {
        users.removeIf(user -> user.getId() == id);
    }

    private long getId() {
        return ++lastId;
    }
}
