package ru.otus.homework.service;

import ru.otus.homework.model.Person;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface PersonService {

    Optional<Person> getById(long id);

    List<Person> findAll();

    Map<Integer, List<Person>> groupByAge();
}