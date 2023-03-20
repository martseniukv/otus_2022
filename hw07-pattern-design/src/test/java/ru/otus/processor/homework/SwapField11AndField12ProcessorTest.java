package ru.otus.processor.homework;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.otus.model.Message;
import ru.otus.processor.Processor;

import static org.junit.jupiter.api.Assertions.*;

class SwapField11AndField12ProcessorTest {

    private Processor processor;

    @BeforeEach
    void setUp(){
        processor = new SwapField11AndField12Processor();
    }

    @Test
    void process() {

        final String field10 = "field10";
        final String field11 = "field11";
        final String field12 = "field12";

        final Message message = new Message.Builder(1L)
                .field10(field10)
                .field11(field11)
                .field12(field12)
                .build();

        final Message response = processor.process(message);

        assertEquals(field10, response.getField10());
        assertEquals(field12, response.getField11());
        assertEquals(field11, response.getField12());
    }
}