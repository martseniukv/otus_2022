package ru.otus.homework.test;

import lombok.extern.slf4j.Slf4j;
import ru.otus.homework.annotations.After;
import ru.otus.homework.annotations.Before;
import ru.otus.homework.annotations.Test;
import ru.otus.homework.model.Person;
import ru.otus.homework.service.impl.SimplePersonService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class SimplePersonServiceTest {

    private SimplePersonService simplePersonService;

    @Before
    void setUp(){

        log.info("Before SimplePersonServiceTest");

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
        log.info("After SimplePersonServiceTest");
    }

    @Test
    void getById(){

        //given
        final long id = 3L;
        final int expectedAge = 25;
        final String expectedName = "Person3";

        //when
        final Optional<Person> response = simplePersonService.getById(id);

        //then
        assertThat(response).isPresent();
        assertThat(response.get().getId()).isEqualTo(id);
        assertThat(response.get().getName()).isEqualTo(expectedName);
        assertThat(response.get().getAge()).isEqualTo(expectedAge);
    }

    @Test
    void findAll(){

        //when
        final List<Person> response = simplePersonService.findAll();

        //then
        assertThat(response).isNotNull();
        assertThat(response.size()).isEqualTo(5);
    }

    @Test
    void groupByAge(){

        //when
        final Map<Integer, List<Person>> response = simplePersonService.groupByAge();

        //then
        assertThat(response).isNotNull();
        assertThat(response.get(24)).size().isEqualTo(1);
        assertThat(response.get(25)).size().isEqualTo(2);
        assertThat(response.get(27)).size().isEqualTo(1);
        assertThat(response.get(30)).size().isEqualTo(1);
    }
}