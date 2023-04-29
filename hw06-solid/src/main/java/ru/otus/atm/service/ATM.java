package ru.otus.atm.service;

import ru.otus.atm.model.ATMResponse;
import ru.otus.atm.model.Banknote;

import java.util.Set;

public interface ATM {

    ATMResponse<Set<Banknote>> withdraw(int sum);

    ATMResponse<Integer> getBalance();
}