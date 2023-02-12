package ru.otus.homework;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.homework.test.executor.impl.TestExecutorImpl;

public class TestStarter {

    private static final Logger log = LoggerFactory.getLogger(TestStarter.class);

    /**
     *
     * @param args
     * ru.otus.homework.test.SimplePersonServiceTest
     * ru.otus.homework.test.SimpleStringServiceTest
     * ru.otus.homework.test.ErrorPersonServiceTest
     * ru.otus.homework.test.ErrorStringServiceTest
     */
    public static void main(String[] args) {

        for (var className : args) {
            try {
                new TestExecutorImpl().execute(className);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
    }
}