package ru.otus.service;

import java.util.List;
import java.util.Set;

public interface StringService {

    String getFirstStringWithMinLength(Set<String> values);

    String getFirstStringWithMaxLength(Set<String> values);

    List<String> removeDuplicate(List<String> values);

    String getReverseString(String value);
}