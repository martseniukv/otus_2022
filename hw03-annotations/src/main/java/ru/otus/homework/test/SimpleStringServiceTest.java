package ru.otus.homework.test;

import ru.otus.homework.annotations.Test;
import ru.otus.homework.service.impl.SimpleStringService;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class SimpleStringServiceTest {

    @Test
    void getReverseString(){

        //when
        String param = "param";
        final String expected = "marap";

        SimpleStringService simpleStringService = new SimpleStringService();
        final String response = simpleStringService.getReverseString(param);

        assertThat(response).isEqualTo(expected);
    }

    @Test
    void getReverseStringWithNullParam(){

        SimpleStringService simpleStringService = new SimpleStringService();

        assertThatThrownBy(() -> simpleStringService.getReverseString(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void getReverseStringWithEmptyParam(){

        SimpleStringService simpleStringService = new SimpleStringService();

        final String response = simpleStringService.getReverseString("");
        assertThat(response).isEqualTo("");
    }

    @Test
    void getStringWithMaxLength(){

        final String expected = "Samsung";
        SimpleStringService simpleStringService = new SimpleStringService();

        final Set<String> param = Set.of("Nokia", expected, "Apple", "Xiaomi");

        final String response = simpleStringService.getFirstStringWithMaxLength(param);
        assertThat(response).isEqualTo(expected);
    }

    @Test
    void getStringWithMinLength(){

        final String expected = "Ruby";
        SimpleStringService simpleStringService = new SimpleStringService();

        final Set<String> param = Set.of("Scale", "Python", "Java", expected);

        final String response = simpleStringService.getFirstStringWithMinLength(param);
        assertThat(response).isEqualTo(expected);
    }

    //Failed Test
    @Test
    void groupByFirstLetterWithNullParam(){

        SimpleStringService simpleStringService = new SimpleStringService();

        final List<String> response = simpleStringService.removeDuplicate(null);
        assertThat(response).isEmpty();
    }
}