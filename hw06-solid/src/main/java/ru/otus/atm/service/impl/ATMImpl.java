package ru.otus.atm.service.impl;

import ru.otus.atm.model.ATMCashCell;
import ru.otus.atm.model.ATMResponse;
import ru.otus.atm.model.Banknote;
import ru.otus.atm.model.enums.Denomination;
import ru.otus.atm.service.ATM;
import ru.otus.atm.service.holder.ATMCellHolder;

import java.util.*;

import static ru.otus.atm.model.enums.ATMErrorCode.*;
import static ru.otus.atm.util.ATMResponseFactory.getATMResponse;

public class ATMImpl implements ATM {

    private final Comparator<Denomination> comparator = Comparator.comparing(Denomination::value).reversed();

    private final ATMCellHolder atmCellHolder;

    public ATMImpl(ATMCellHolder atmCellHolder) {
        this.atmCellHolder = atmCellHolder;
    }

    @Override
    public ATMResponse<Set<Banknote>> withdraw(int sum) {

        if (sum <= 0) {
            return getATMResponse(INCORRECT_AMOUNT.getATMError());
        }

        TreeSet<Denomination> availableDenomination = new TreeSet<>(comparator);
        availableDenomination.addAll(atmCellHolder.getAvailableDenomination());

        if (availableDenomination.isEmpty()) {
            return getATMResponse(EMPTY_DENOMINATION.getATMError());
        }
        if (sum % availableDenomination.last().value() != 0) {
            return getATMResponse(NON_COMPLIANCE_MIN_DENOMINATION.getATMError());
        }
        if (sum > atmCellHolder.getCashBalance()) {
            return getATMResponse(NOT_ENOUGH_MONEY.getATMError());
        }

        int resultSum = 0;
        Set<Banknote> resultCash = new HashSet<>();
        for (var denomination : availableDenomination) {

            int countBanknote = (sum - resultSum) / denomination.value();
            if (countBanknote > 0 && resultSum < sum) {

                Optional<ATMCashCell> cellByDenomination = atmCellHolder.getCellByDenomination(denomination);
                cellByDenomination.ifPresent(atmCashCell -> resultCash.addAll(atmCashCell.getBanknotes(countBanknote)));
                resultSum += countBanknote * denomination.value();
            }
        }
        return getATMResponse(resultCash);
    }

    @Override
    public ATMResponse<Integer> getBalance() {
        return getATMResponse(atmCellHolder.getCashBalance());
    }
}