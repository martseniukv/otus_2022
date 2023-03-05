package ru.otus.service.impl;

import ru.otus.log.Log;
import ru.otus.service.StringService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.Comparator.comparing;

public class StringServiceImpl implements StringService {

    @Log
    @Override
    public String getFirstStringWithMinLength(Set<String> values) {
        return values.stream()
                .min(comparing(String::length))
                .orElse(null);
    }

    @Log
    @Override
    public String getFirstStringWithMaxLength(Set<String> values) {
        return values.stream()
                .max(comparing(String::length))
                .orElse(null);
    }

    @Log
    @Override
    public List<String> removeDuplicate(List<String> values) {
        final Set<String> hashSet = new HashSet<>();
        return values.stream()
                .filter(hashSet::add)
                .toList();
    }

    @Override
    public String getReverseString(String value) {
        return new StringBuilder(value).reverse().toString();
    }
}