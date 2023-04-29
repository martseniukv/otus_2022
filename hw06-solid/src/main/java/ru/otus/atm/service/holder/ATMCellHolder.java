package ru.otus.atm.service.holder;

import ru.otus.atm.model.ATMCashCell;
import ru.otus.atm.model.Banknote;
import ru.otus.atm.model.enums.Denomination;

import java.util.Optional;
import java.util.Set;

public interface ATMCellHolder {

    boolean addCell(ATMCashCell cashCell);

    int getCashBalance();

    Optional<ATMCashCell> extractCell(Denomination denomination);

    Set<Banknote> getBanknotesByDenomination(Denomination denomination);

    Optional<ATMCashCell> getCellByDenomination(Denomination denomination);

    Set<Denomination> getAvailableDenomination();
}