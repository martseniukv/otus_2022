package ru.otus.repository;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import ru.otus.model.entity.Client;

import java.util.List;


public interface ClientRepository extends CrudRepository<Client, Long> {

    @Override
    @Query(value = """
    SELECT c.id     AS id,
           c.name   AS name,
           a.id     AS address_id,
           a.street AS address_street,
           p.id     AS phone_id,
           p.number AS phone_number
    FROM client c
    LEFT OUTER JOIN address a ON a.client_id = c.id
    LEFT OUTER JOIN phone p ON p.client_id = c.id
    order by c.id
    """, resultSetExtractorClass = ClientResultSetExtractorClass.class)
    List<Client> findAll();
}