package ru.otus.service.impl;

import ru.otus.log.Log;
import ru.otus.service.CalculationService;

public class CalculationServiceImpl implements CalculationService {

    @Log
    @Override
    public double abs(double vav1) {
        return Math.abs(vav1);
    }

    @Log
    @Override
    public double add(double vav1, double val2) {
        return vav1 + val2;
    }

    @Override
    public double add(Double vav1, Double val2) {
        return vav1 + val2;
    }

    @Log
    @Override
    public double add(double vav1, double val2, double val3) {
        return vav1 + val2 + val3;
    }
}