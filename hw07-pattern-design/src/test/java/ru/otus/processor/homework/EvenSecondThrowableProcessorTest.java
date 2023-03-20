package ru.otus.processor.homework;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.otus.model.Message;
import ru.otus.processor.Processor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EvenSecondThrowableProcessorTest {

    Processor processor;
    DateTimeProvider dateTimeProvider;

    @BeforeEach
    void setUp(){
        dateTimeProvider = mock(DateTimeProviderImpl.class);
        processor = new EvenSecondThrowableProcessor(dateTimeProvider);
    }

    @Test
    void process_with_even_second() {

        Message message = mock(Message.class);
        LocalDateTime localDateTime = LocalDateTime.of(LocalDate.now(), LocalTime.of(1, 2, 2));

        when(dateTimeProvider.now()).thenReturn(localDateTime);

        assertThrows(IllegalStateException.class, () -> processor.process(message));

        verify(dateTimeProvider).now();
    }

    @Test
    void process_with_odd_second() {

        final int id = 1;
        Message message = new Message.Builder(id).build();
        LocalDateTime localDateTime = LocalDateTime.of(LocalDate.now(), LocalTime.of(1, 2, 3));

        when(dateTimeProvider.now()).thenReturn(localDateTime);

        assertEquals(message, processor.process(message));

        verify(dateTimeProvider).now();
    }
}