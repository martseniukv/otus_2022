package ru.otus.model.entity;

import jakarta.annotation.Nonnull;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("address")
public class Address {

    @Id
    private Long id;
    @Nonnull
    private String street;

    private Long clientId;

    @PersistenceCreator
    public Address(Long id, String street, Long clientId) {
        this.id = id;
        this.street = street;
        this.clientId = clientId;
    }
    public Address(String street) {
        this(null, street, null);
    }
}