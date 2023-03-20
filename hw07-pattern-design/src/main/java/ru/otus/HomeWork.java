package ru.otus;

import ru.otus.handler.ComplexProcessor;
import ru.otus.listener.ListenerPrinterConsole;
import ru.otus.listener.homework.HistoryListener;
import ru.otus.model.Message;
import ru.otus.model.ObjectForMessage;
import ru.otus.processor.LoggerProcessor;
import ru.otus.processor.homework.DateTimeProviderImpl;
import ru.otus.processor.homework.EvenSecondThrowableProcessor;
import ru.otus.processor.homework.SwapField11AndField12Processor;

import java.util.List;

public class HomeWork {

    public static void main(String[] args) {

        var processors = List.of(new SwapField11AndField12Processor(),
                new LoggerProcessor(new EvenSecondThrowableProcessor(new DateTimeProviderImpl())));

        var complexProcessor = new ComplexProcessor(processors, ex -> System.out.printf("Exception handled: %s%n", ex.getMessage()));
        var listenerPrinter = new ListenerPrinterConsole();
        var historyListener = new HistoryListener();
        complexProcessor.addListener(listenerPrinter);
        complexProcessor.addListener(historyListener);

        var objectForMessage = new ObjectForMessage();
        objectForMessage.setData(List.of("field13"));

        var message = new Message.Builder(1L)
                .field1("field1")
                .field2("field2")
                .field3("field3")
                .field6("field6")
                .field10("field10")
                .field11("field11")
                .field12("field12")
                .field13(objectForMessage)
                .build();

        var result = complexProcessor.handle(message);
        System.out.println("result:" + result);

        complexProcessor.removeListener(listenerPrinter);
        complexProcessor.removeListener(historyListener);
    }
}
