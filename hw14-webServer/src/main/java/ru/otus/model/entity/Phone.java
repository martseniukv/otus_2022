package ru.otus.model.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.otus.model.Copyable;

@Getter
@Setter
@Entity
@Table(name = "phone")
@AllArgsConstructor
@NoArgsConstructor
public class Phone implements Copyable<Phone> {

    @Id
    @SequenceGenerator(name = "phone_gen", sequenceName = "phone_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "phone_gen")
    @Column(name = "id")
    private Long id;

    @Column(name = "number")
    private String number;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private Client client;

    public Phone(Long id, String number) {
        this.id = id;
        this.number = number;
    }

    @Override
    public Phone copy() {
        return new Phone(this.id, this.number, this.client);
    }

    public void addClient(Client client) {
        if (client != null) {
            setClient(client);
        }
    }
}