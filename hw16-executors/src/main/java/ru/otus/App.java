package ru.otus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.util.concurrent.Executors.newFixedThreadPool;


public class App {

    private static final Logger log = LoggerFactory.getLogger(App.class);

    private static int last = 2;

    public static void main(String[] args) {

        var minBorder = 1;
        var maxBorder = 10;
        var countIteration = 2;

        var app = new App();
        var executor = newFixedThreadPool(2);

        executor.submit(() -> app.printNumbers(1, minBorder, maxBorder, countIteration));
        executor.submit(() -> app.printNumbers(2, minBorder, maxBorder, countIteration));

        executor.shutdown();
    }

    private synchronized void printNumbers(int threadNum, int minBorder, int maxBorder, int countIteration) {
        int value = minBorder;
        int current = maxBorder;
        int currentIteration = 0;
        while (currentIteration < countIteration) {

            try {
                while (last == threadNum) {
                    this.wait();
                }
                if (value == current) {
                    currentIteration++;
                    if (current == minBorder) {
                        current = maxBorder;
                    } else {
                        current = minBorder;
                    }
                }
                var result = (value < current) ? value++ : value--;
                log.info("result: {}", result);

                last = threadNum;
                sleep();
                notifyAll();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private static void sleep() {
        try {
            Thread.sleep(5_00);
        } catch (InterruptedException e) {
            log.error(e.getMessage());
            Thread.currentThread().interrupt();
        }
    }
}