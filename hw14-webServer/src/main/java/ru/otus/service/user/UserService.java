package ru.otus.service.user;

import ru.otus.model.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    Optional<User> findByLogin(String login);

    User save(User user);

    Optional<User> getById(long id);

    List<User> findAll();
}