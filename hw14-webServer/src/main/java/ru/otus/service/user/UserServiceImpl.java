package ru.otus.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.otus.core.repository.DataTemplate;
import ru.otus.core.sessionmanager.TransactionManager;
import ru.otus.model.entity.User;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final DataTemplate<User> clientDataTemplate;
    private final TransactionManager transactionManager;

    @Override
    public Optional<User> findByLogin(String login) {
       return transactionManager.doInTransaction(session ->
               clientDataTemplate.findByUniqueEntityField(session, "login", login));
    }

    @Override
    public User save(User user) {
        return transactionManager.doInTransaction(session -> {
            var userCloned = user.copy();
            if (user.getId() == null) {
                clientDataTemplate.insert(session, userCloned);
                log.info("created user: {}", userCloned);
                return userCloned;
            }
            clientDataTemplate.update(session, userCloned);
            log.info("updated user: {}", userCloned);
            return userCloned;
        });
    }

    @Override
    public Optional<User> getById(long id) {
        return transactionManager.doInTransaction(session ->
                clientDataTemplate.findById(session, id));
    }

    @Override
    public List<User> findAll() {
        return transactionManager.doInReadOnlyTransaction(session -> {
            var users = clientDataTemplate.findAll(session);
            log.info("users:{}", users);
            return users;
        });
    }
}