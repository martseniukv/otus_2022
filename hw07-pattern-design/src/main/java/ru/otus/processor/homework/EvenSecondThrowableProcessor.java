package ru.otus.processor.homework;

import ru.otus.model.Message;
import ru.otus.processor.Processor;

public class EvenSecondThrowableProcessor implements Processor {


    private final DateTimeProvider dateTimeProvider;

    public EvenSecondThrowableProcessor(DateTimeProvider dateTimeProvider) {
        this.dateTimeProvider = dateTimeProvider;
    }

    @Override
    public Message process(Message message) {

        if (dateTimeProvider.now().getSecond() % 2 == 0) {
            throw new IllegalStateException("Even second");
        }
        return message.toBuilder().build();
    }
}
