package ru.otus.homework.service.impl;

import ru.otus.homework.model.Person;
import ru.otus.homework.service.PersonService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class SimplePersonService implements PersonService {

    private final List<Person> personList;

    public SimplePersonService(List<Person> personList) {
        this.personList = personList;
    }

    @Override
    public Optional<Person> getById(long id) {
        return personList.stream()
                .filter(person -> person.getId() == id)
                .findFirst();
    }

    @Override
    public List<Person> findAll() {
        return new ArrayList<>(personList);
    }

    @Override
    public Map<Integer, List<Person>> groupByAge() {
        return personList.stream()
                .collect(Collectors.groupingBy(Person::getAge));
    }
}