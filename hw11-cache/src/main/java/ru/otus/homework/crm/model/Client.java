package ru.otus.homework.crm.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.util.Objects.isNull;
import static java.util.Optional.ofNullable;

@Getter
@Setter
@Entity
@ToString
@NoArgsConstructor
@Table(name = "client")
public class Client implements Copyable<Client> {

    @Id
    @SequenceGenerator(name = "client_gen", sequenceName = "client_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "client_gen")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @ToString.Exclude
    @OneToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "address_id")
    private Address address;

    @ToString.Exclude
    @Fetch(FetchMode.SUBSELECT)
    @OneToMany(mappedBy = "client", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Phone> phones = new ArrayList<>();

    public Client(String name) {
        this.id = null;
        this.name = name;
    }

    public Client(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Client(Long id, String name, Address address, List<Phone> phones) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phones = phones;
        ofNullable(this.phones).ifPresent(phoneList -> phoneList.forEach(phone -> phone.addClient(this)));
    }

    @Override
    public Client copy() {
        Address addressClone = isNull(this.address) ? null : this.address.copy();
        List<Phone> phoneClones = isNull(this.phones)
                ? new ArrayList<>()
                : this.phones.stream().filter(Objects::nonNull).map(Phone::copy).toList();
        return new Client(this.id, this.name, addressClone, phoneClones);
    }
}