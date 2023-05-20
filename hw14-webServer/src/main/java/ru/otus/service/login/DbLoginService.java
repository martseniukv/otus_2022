package ru.otus.service.login;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.security.AbstractLoginService;
import org.eclipse.jetty.security.RolePrincipal;
import org.eclipse.jetty.security.UserPrincipal;
import org.eclipse.jetty.util.security.Password;
import ru.otus.model.entity.User;
import ru.otus.service.user.UserService;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class DbLoginService extends AbstractLoginService {

    private final UserService userService;

    @Override
    protected List<RolePrincipal> loadRoleInfo(UserPrincipal user) {
        return userService.findByLogin(user.getName())
                .map(User::getRoles)
                .map(roles -> roles.stream()
                        .map(role -> new RolePrincipal(role.getName()))
                        .toList())
                .orElse(new ArrayList<>());
    }

    @Override
    protected UserPrincipal loadUserInfo(String login) {

        log.info("DbLoginService load user info");
        return userService.findByLogin(login)
                .map(user -> new UserPrincipal(user.getLogin(), new Password(user.getPassword())))
                .orElse(null);
    }
}