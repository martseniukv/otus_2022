package ru.otus.dataprocessor.processor.impl;

import ru.otus.dataprocessor.processor.Processor;
import ru.otus.model.Measurement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static java.util.Objects.isNull;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summingDouble;

public class ProcessorAggregator implements Processor {

    @Override
    public Map<String, Double> process(List<Measurement> data) {

        if (isNull(data)) {
            return new HashMap<>();
        }
        return data.stream().collect(groupingBy(Measurement::getName, TreeMap::new, summingDouble(Measurement::getValue)));
    }
}