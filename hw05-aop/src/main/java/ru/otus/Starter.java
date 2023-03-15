package ru.otus;

import ru.otus.service.CalculationService;
import ru.otus.service.StringService;
import ru.otus.service.impl.CalculationServiceImpl;
import ru.otus.service.impl.StringServiceImpl;

import java.util.List;
import java.util.Set;

import static ru.otus.proxy.Ioc.getProxyInstance;

public class Starter {

    public static void main(String[] args) {
        final CalculationService calculationService =
                getProxyInstance(CalculationService.class, new CalculationServiceImpl());

        calculationService.abs(-10d);
        calculationService.add(13d, 10d);
        calculationService.add(Double.valueOf("12"), Double.valueOf("100"));
        calculationService.add(13d, 10d, 19d);

        final StringService stringService =
                getProxyInstance(StringService.class, new StringServiceImpl());

        final Set<String> stringValueSet = Set.of("First", "Second");
        stringService.getFirstStringWithMinLength(stringValueSet);
        stringService.getFirstStringWithMaxLength(stringValueSet);
        stringService.getReverseString("Java");
        stringService.removeDuplicate(List.of("Java", "JS", "Python", "C", "C++", "Python", "C"));
    }
}
