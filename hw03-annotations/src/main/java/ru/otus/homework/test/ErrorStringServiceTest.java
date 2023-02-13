package ru.otus.homework.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.homework.annotations.After;
import ru.otus.homework.annotations.Before;
import ru.otus.homework.annotations.Test;
import ru.otus.homework.service.impl.SimpleStringService;

import static org.assertj.core.api.Assertions.assertThat;

public class ErrorStringServiceTest {

    private static final Logger log = LoggerFactory.getLogger(ErrorStringServiceTest.class);
    @Test
    private void getReverseString(){

        //when
        String param = "param";
        final String expected = "marap";

        SimpleStringService simpleStringService = new SimpleStringService();
        final String response = simpleStringService.getReverseString(param);

        assertThat(response).isEqualTo(expected);
    }

    @Before
    private void setUp(){
        log.info("Before ErrorStringServiceTest");
        throw new UnsupportedOperationException();
    }

    @After
    private void tearDown(){
        log.info("After ErrorStringServiceTest");
        throw new UnsupportedOperationException();
    }
}