package ru.otus.model.entity;

import jakarta.annotation.Nonnull;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Set;

@Data
@Table("client")
public class Client {

    @Id
    private Long id;

    @Nonnull
    private String name;

    @MappedCollection(idColumn = "client_id")
    private Address address;

    @MappedCollection(idColumn = "client_id")
    private Set<Phone> phones;

    @PersistenceCreator
    public Client(Long id, String name, Address address, Set<Phone> phones) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phones = phones;
    }

    public Client(Long id, String name, Set<Phone> phones) {
        this.id = id;
        this.name = name;
        this.phones = phones;
    }

    public Client(String name, Address address, Set<Phone> phones) {
        this(null, name, address, phones);
    }
}