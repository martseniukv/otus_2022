package ru.otus.atm.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.otus.atm.model.ATMCashCell;
import ru.otus.atm.model.Banknote;
import ru.otus.atm.model.enums.Denomination;
import ru.otus.atm.service.holder.ATMCellHolderImpl;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static ru.otus.atm.model.enums.ATMErrorCode.*;
import static ru.otus.atm.model.enums.Denomination.*;

class ATMImplTest {

    private ATMImpl atm;
    private ATMCellHolderImpl atmCellHolderMock;

    @BeforeEach
    void setUp() {

        atmCellHolderMock = mock(ATMCellHolderImpl.class);
        atm = new ATMImpl(atmCellHolderMock);
    }

    @Test
    void withdraw_with_incorrect_amount_error() {

        //act
        var response = atm.withdraw(-200);

        //assert
        assertNotNull(response);
        assertNull(response.getData());
        assertFalse(response.getErrors().isEmpty());
        assertEquals(INCORRECT_AMOUNT.getCode(), response.getErrors().get(0).getCode());
        assertEquals(INCORRECT_AMOUNT.getMessage(), response.getErrors().get(0).getMessage());

        //verify
        verify(atmCellHolderMock, times(0)).getCashBalance();
    }

    @Test
    void withdraw_with_not_enough_money_error() {

        int atmBalance = 100;
        var denominationSet = Set.of(ONE_HUNDRED);

        //when
        when(atmCellHolderMock.getAvailableDenomination()).thenReturn(denominationSet);
        when(atmCellHolderMock.getCashBalance()).thenReturn(atmBalance);

        //act
        var response = atm.withdraw(200);

        //assert
        assertNotNull(response);
        assertNull(response.getData());
        assertFalse(response.getErrors().isEmpty());
        assertEquals(NOT_ENOUGH_MONEY.getCode(), response.getErrors().get(0).getCode());
        assertEquals(NOT_ENOUGH_MONEY.getMessage(), response.getErrors().get(0).getMessage());

        //verify
        verify(atmCellHolderMock).getAvailableDenomination();
        verify(atmCellHolderMock).getCashBalance();
    }

    @Test
    void withdraw_with_empty_denomination_error() {

        //when
        when(atmCellHolderMock.getAvailableDenomination()).thenReturn(new HashSet<>());

        //act
        var response = atm.withdraw(50);

        //assert
        assertNotNull(response);
        assertNull(response.getData());
        assertFalse(response.getErrors().isEmpty());
        assertEquals(EMPTY_DENOMINATION.getCode(), response.getErrors().get(0).getCode());
        assertEquals(EMPTY_DENOMINATION.getMessage(), response.getErrors().get(0).getMessage());

        //verify
        verify(atmCellHolderMock).getAvailableDenomination();
    }

    @Test
    void withdraw_with_non_complete_min_denomination_error() {

        var denominationSet = Set.of(ONE_HUNDRED);

        //when
        when(atmCellHolderMock.getAvailableDenomination()).thenReturn(denominationSet);

        //act
        var response = atm.withdraw(50);

        //assert
        assertNotNull(response);
        assertNull(response.getData());
        assertEquals(1, response.getErrors().size());
        assertEquals(NON_COMPLIANCE_MIN_DENOMINATION.getCode(), response.getErrors().get(0).getCode());
        assertEquals(NON_COMPLIANCE_MIN_DENOMINATION.getMessage(), response.getErrors().get(0).getMessage());

        //verify
        verify(atmCellHolderMock).getAvailableDenomination();
    }

    @Test
    void withdraw_5500() {

        int atmBalance = 6000;
        var denominationSet = Set.of(ONE_THOUSAND, FIVE_HUNDRED, FIFTY);
        var thousandCashCell = new ATMCashCell(ONE_THOUSAND, getBanknotes(ONE_THOUSAND, 5));
        var fiveHundredCashCell = new ATMCashCell(FIVE_HUNDRED, getBanknotes(FIVE_HUNDRED, 2));
        var fiftyCashCell = new ATMCashCell(FIFTY, getBanknotes(FIFTY, 5));

        //when
        when(atmCellHolderMock.getCashBalance()).thenReturn(atmBalance);
        when(atmCellHolderMock.getAvailableDenomination()).thenReturn(denominationSet);

        when(atmCellHolderMock.getCellByDenomination(ONE_THOUSAND)).thenReturn(Optional.of(thousandCashCell));
        when(atmCellHolderMock.getCellByDenomination(FIVE_HUNDRED)).thenReturn(Optional.of(fiveHundredCashCell));
        when(atmCellHolderMock.getCellByDenomination(FIFTY)).thenReturn(Optional.of(fiftyCashCell));

        //act
        var response = atm.withdraw(5550);

        //assert
        assertNotNull(response);
        assertTrue(response.getErrors().isEmpty());
        assertNotNull(response.getData());
        assertEquals(7, response.getData().size());

        //verify
        verify(atmCellHolderMock).getCashBalance();
        verify(atmCellHolderMock).getAvailableDenomination();

        verify(atmCellHolderMock).getCellByDenomination(ONE_THOUSAND);
        verify(atmCellHolderMock).getCellByDenomination(FIVE_HUNDRED);
        verify(atmCellHolderMock).getCellByDenomination(FIFTY);
    }


    @Test
    void getBalance() {

        int atmBalance = 100;

        //when
        when(atmCellHolderMock.getCashBalance()).thenReturn(atmBalance);

        var response = atm.getBalance();

        //assert
        assertNotNull(response);
        assertTrue(response.getErrors().isEmpty());
        assertEquals(atmBalance, response.getData());

        //verify
        verify(atmCellHolderMock).getCashBalance();
    }

    private static Set<Banknote> getBanknotes(Denomination denomination, int quintity) {
        return IntStream.range(0, quintity)
                .mapToObj(i -> new Banknote(denomination.value() + i, denomination))
                .collect(Collectors.toSet());
    }
}