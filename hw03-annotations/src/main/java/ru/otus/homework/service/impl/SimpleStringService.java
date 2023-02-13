package ru.otus.homework.service.impl;

import ru.otus.homework.service.StringService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

public class SimpleStringService implements StringService {

    @Override
    public String getFirstStringWithMinLength(Set<String> values) {
        return values.stream()
                .min(comparing(String::length))
                .orElse(null);
    }

    @Override
    public String getFirstStringWithMaxLength(Set<String> values) {
        return values.stream()
                .max(comparing(String::length))
                .orElse(null);
    }

    @Override
    public List<String> removeDuplicate(List<String> values) {
        final Set<String> hashSet = new HashSet<>();
        return values.stream()
                .filter(hashSet::add)
                .collect(toList());
    }

    @Override
    public String getReverseString(String value) {
        return new StringBuilder(value).reverse().toString();
    }
}