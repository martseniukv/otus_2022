package ru.otus;

import ru.otus.service.CalculationService;
import ru.otus.service.impl.CalculationServiceImpl;

import static ru.otus.proxy.Ioc.createCalculationService;

public class Starter {

    public static void main(String[] args) {
        final CalculationService calculationService = createCalculationService(new CalculationServiceImpl());
        calculationService.abs(-10d);
        calculationService.add(13d, 10d);
        calculationService.add(Double.valueOf("12"), Double.valueOf("100"));
        calculationService.add(13d, 10d, 19d);
    }
}
