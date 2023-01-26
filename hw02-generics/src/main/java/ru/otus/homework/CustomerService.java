package ru.otus.homework;


import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

import static java.util.Map.entry;
import static java.util.Objects.isNull;

public class CustomerService {

    private final NavigableMap<Customer, String> customerStringTreeMap = new TreeMap<>();

    public Map.Entry<Customer, String> getSmallest() {
        final Map.Entry<Customer, String> customerStringEntry = customerStringTreeMap.firstEntry();
        if (isNull(customerStringEntry)) {
            return null;
        }
        return entry(new Customer(customerStringEntry.getKey()), customerStringEntry.getValue());
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        final Map.Entry<Customer, String> customerStringEntry = customerStringTreeMap.higherEntry(customer);
        if (isNull(customerStringEntry)) {
            return null;
        }
        return entry(new Customer(customerStringEntry.getKey()), customerStringEntry.getValue());
    }

    public void add(Customer customer, String data) {
        customerStringTreeMap.put(customer, data);
    }
}