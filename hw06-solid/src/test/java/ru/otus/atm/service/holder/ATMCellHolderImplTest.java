package ru.otus.atm.service.holder;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.otus.atm.model.ATMCashCell;
import ru.otus.atm.model.Banknote;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static ru.otus.atm.model.enums.Denomination.FIFTY;
import static ru.otus.atm.model.enums.Denomination.ONE_THOUSAND;

class ATMCellHolderImplTest {

    private ATMCellHolderImpl atmCellHolder;

    @BeforeEach
    void setUp () {
        atmCellHolder = new ATMCellHolderImpl();
    }

    @Test
    void addCell_with_null_cashCell() {

        //act
        boolean response = atmCellHolder.addCell(null);

        //assert
        assertFalse(response);
    }

    @Test
    void addCell() {

        var thousandBanknote = new Banknote(ONE_THOUSAND.value() + 10, ONE_THOUSAND);
        var cashCell = new ATMCashCell(ONE_THOUSAND, Set.of(thousandBanknote));

        //act
        boolean response = atmCellHolder.addCell(cashCell);

        //assert
        var cellByDenomination = atmCellHolder.getCellByDenomination(ONE_THOUSAND);

        assertTrue(response);
        assertTrue(cellByDenomination.isPresent());
        assertEquals(cashCell, cellByDenomination.get());
    }

    @Test
    void extractCell_with_non_existing_denomination() {

        var thousandBanknote = new Banknote(ONE_THOUSAND.value() + 10, ONE_THOUSAND);
        var cashCell = new ATMCashCell(ONE_THOUSAND, Set.of(thousandBanknote));
        atmCellHolder.addCell(cashCell);

        //act
        var response = atmCellHolder.extractCell(FIFTY);

        //assert
        var cellByDenomination = atmCellHolder.getCellByDenomination(ONE_THOUSAND);
        assertTrue(response.isEmpty());
        assertTrue(cellByDenomination.isPresent());
    }

    @Test
    void extractCell() {

        var thousandBanknote = new Banknote(ONE_THOUSAND.value() + 10, ONE_THOUSAND);
        var cashCell = new ATMCashCell(ONE_THOUSAND, Set.of(thousandBanknote));
        atmCellHolder.addCell(cashCell);

        //act
        var response = atmCellHolder.extractCell(ONE_THOUSAND);

        //assert
        var cellByDenomination = atmCellHolder.getCellByDenomination(ONE_THOUSAND);
        assertTrue(response.isPresent());
        assertTrue(cellByDenomination.isEmpty());
    }

    @Test
    void getBanknotesByDenomination() {

        var thousandBanknote = new Banknote(ONE_THOUSAND.value() + 10, ONE_THOUSAND);
        var banknotes = Set.of(thousandBanknote);
        var cashCell = new ATMCashCell(ONE_THOUSAND, Set.of(thousandBanknote));
        atmCellHolder.addCell(cashCell);

        //act
        var response = atmCellHolder.getBanknotesByDenomination(ONE_THOUSAND);

        //assert
        assertEquals(banknotes, response);
    }

    @Test
    void getCellByDenomination() {

        var thousandBanknote = new Banknote(ONE_THOUSAND.value() + 10, ONE_THOUSAND);
        var cashCell = new ATMCashCell(ONE_THOUSAND, Set.of(thousandBanknote));
        atmCellHolder.addCell(cashCell);

        //act
        var response = atmCellHolder.getCellByDenomination(ONE_THOUSAND);

        //act
        assertTrue(response.isPresent());
        assertEquals(cashCell, response.get());
    }

    @Test
    void getAvailableDenomination() {

        var thousandBanknote = new Banknote(ONE_THOUSAND.value() + 10, ONE_THOUSAND);
        var cashCell = new ATMCashCell(ONE_THOUSAND, Set.of(thousandBanknote));
        atmCellHolder.addCell(cashCell);

        //act
        var response = atmCellHolder.getAvailableDenomination();

        //assert
        assertNotNull(response);
        assertEquals(1, response.size());

        var denomination = response.stream().findFirst();
        assertTrue(denomination.isPresent());
        assertEquals(ONE_THOUSAND, denomination.get());
    }

    @Test
    void getCashBalance() {

        var thousandBanknote = new Banknote(ONE_THOUSAND.value() + 10, ONE_THOUSAND);
        var cashCell = new ATMCashCell(ONE_THOUSAND, Set.of(thousandBanknote));
        atmCellHolder.addCell(cashCell);

        //act
        int response = atmCellHolder.getCashBalance();

        //assert
        assertEquals(1000, response);
    }
}