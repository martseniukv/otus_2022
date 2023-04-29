package ru.otus.atm.model;

import ru.otus.atm.model.enums.Denomination;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

public class ATMCashCell {

    private final Set<Banknote> banknotes;
    private final Denomination denomination;

    public ATMCashCell(Denomination denomination, Set<Banknote> banknotes) {

        requireNonNull(denomination);
        requireNonNull(banknotes);
        checkBanknotes(denomination, banknotes);

        this.denomination = denomination;
        this.banknotes = banknotes.stream()
                .filter(banknote -> denomination.equals(banknote.getDenomination()))
                .collect(Collectors.toSet());
    }

    public Set<Banknote> banknotes() {
        return banknotes;
    }

    public Denomination getDenomination() {
        return denomination;
    }
    public Set<Banknote> getBanknotes(int count) {
        Set<Banknote> resultBanknotes = banknotes.stream()
                .limit(count)
                .collect(Collectors.toSet());
        banknotes.removeAll(resultBanknotes);
        return resultBanknotes;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (ATMCashCell) obj;
        return Objects.equals(this.banknotes, that.banknotes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(banknotes);
    }

    @Override
    public String toString() {
        return "ATMCashCell[" +
                "banknotes=" + banknotes + ']';
    }

    private static void checkBanknotes(Denomination denomination, Set<Banknote> banknotes) {

        var incorrectBanknote = banknotes.stream()
                .filter(banknote -> !banknote.getDenomination().equals(denomination))
                .collect(Collectors.toSet());

        if (!incorrectBanknote.isEmpty()) {
            Set<Denomination> incorrectDenomination = incorrectBanknote.stream()
                    .filter(Objects::nonNull)
                    .map(Banknote::getDenomination)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());

            throw new IllegalArgumentException("Can not insert : " + incorrectDenomination + " to cell :" + denomination);
        }
    }
}