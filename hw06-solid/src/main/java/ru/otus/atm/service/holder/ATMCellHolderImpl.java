package ru.otus.atm.service.holder;

import ru.otus.atm.model.ATMCashCell;
import ru.otus.atm.model.Banknote;
import ru.otus.atm.model.enums.Denomination;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class ATMCellHolderImpl implements ATMCellHolder {

    private final Map<Denomination, ATMCashCell> cellMap = new EnumMap<>(Denomination.class);

    @Override
    public boolean addCell(ATMCashCell cashCell) {

        if (isNull(cashCell)) {
            return false;
        }
        cellMap.put(cashCell.getDenomination(), cashCell);
        return true;
    }

    @Override
    public Optional<ATMCashCell> extractCell(Denomination denomination) {

        if (!cellMap.containsKey(denomination)) {
            return Optional.empty();
        }
        return Optional.ofNullable(cellMap.remove(denomination));
    }

    @Override
    public Set<Banknote> getBanknotesByDenomination(Denomination denomination) {
        return Optional.ofNullable(cellMap.get(denomination))
                .map(ATMCashCell::banknotes)
                .orElse(new HashSet<>());
    }

    @Override
    public Optional<ATMCashCell> getCellByDenomination(Denomination denomination) {
        return Optional.ofNullable(cellMap.get(denomination));
    }

    @Override
    public Set<Denomination> getAvailableDenomination() {
        return cellMap.entrySet().stream()
                .filter(cashCellEntry -> nonNull(cashCellEntry.getKey()))
                .filter(cashCellEntry -> nonNull(cashCellEntry.getValue()))
                .filter(cashCellEntry -> !cashCellEntry.getValue().banknotes().isEmpty())
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
    }

    @Override
    public int getCashBalance() {
        return cellMap.entrySet().stream()
                .filter(cellEntry -> nonNull(cellEntry.getKey()) &&  nonNull(cellEntry.getValue()))
                .map(cellEntry -> {
                    Denomination key = cellEntry.getKey();
                    ATMCashCell value = cellEntry.getValue();
                    return key.value() * value.banknotes().size();
                }).reduce(0, Integer::sum);
    }
}