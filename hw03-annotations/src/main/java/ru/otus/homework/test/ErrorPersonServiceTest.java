package ru.otus.homework.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.homework.annotations.After;
import ru.otus.homework.annotations.Before;
import ru.otus.homework.annotations.Test;
import ru.otus.homework.model.Person;
import ru.otus.homework.service.impl.SimplePersonService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


public class ErrorPersonServiceTest {

    private static final Logger log = LoggerFactory.getLogger(ErrorPersonServiceTest.class);

    private SimplePersonService simplePersonService;

    @Before
    void setUp(){

        log.info("Before ErrorPersonServiceTest");

        List<Person> personList = new ArrayList<>();
        personList.add(new Person(1L, "Person1", 24));
        personList.add(new Person(2L, "Person2", 25));
        personList.add(new Person(3L, "Person3", 25));
        personList.add(new Person(4L, "Person4", 27));
        personList.add(new Person(5L, "Person5", 30));

        simplePersonService = new SimplePersonService(personList);
    }

    @After
    void tearDown(){
        log.info("After ErrorPersonServiceTest");
        throw new NullPointerException();
    }

    @Test
    void getById(){

        //given
        final long id = 12L;

        //when
        final Optional<Person> response = simplePersonService.getById(id);

        //then
        assertThat(response).isEmpty();
    }
}