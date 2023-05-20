package ru.otus.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.otus.model.Copyable;

import java.util.List;

import static java.util.Optional.ofNullable;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "users")
public class User implements Copyable<User> {

    @Id
    @SequenceGenerator(name = "user_gen", sequenceName = "user_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_gen")
    @Column(name = "id")
    private Long id;

    @Column(name = "login", nullable = false, unique = true, length = 50)
    private String login;

    @Column(name = "password", nullable = false, length = 50)
    private String password;

    @ManyToMany(cascade = {CascadeType.PERSIST}, fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id")}
    )
    private List<Role> roles;

    public User(String login, String password, List<Role> roles) {

        this.login = login;
        this.password = password;
        this.roles = roles;
        ofNullable(this.roles).ifPresent(roleList -> roleList.forEach(role -> role.addUser(this)));
    }

    public User(Long id, String login, String password, List<Role> roles) {

        this.login = login;
        this.password = password;
        this.roles = roles;
        ofNullable(this.roles).ifPresent(roleList -> roleList.forEach(role -> role.addUser(this)));
    }

    @Override
    public User copy() {
        return new User(this.id, this.login, this.password, this.roles);
    }
}