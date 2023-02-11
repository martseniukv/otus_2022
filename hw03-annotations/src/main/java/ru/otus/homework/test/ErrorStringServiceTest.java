package ru.otus.homework.test;

import lombok.extern.slf4j.Slf4j;
import ru.otus.homework.annotations.Before;
import ru.otus.homework.annotations.Test;
import ru.otus.homework.service.impl.SimpleStringService;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class ErrorStringServiceTest {

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
}