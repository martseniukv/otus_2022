package ru.otus.atm.service.integration;

import org.junit.jupiter.api.Test;
import ru.otus.atm.model.ATMCashCell;
import ru.otus.atm.model.ATMResponse;
import ru.otus.atm.model.Banknote;
import ru.otus.atm.model.enums.Denomination;
import ru.otus.atm.service.ATM;
import ru.otus.atm.service.holder.ATMCellHolderImpl;
import ru.otus.atm.service.impl.ATMImpl;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static ru.otus.atm.model.enums.ATMErrorCode.*;
import static ru.otus.atm.model.enums.Denomination.*;

class ATMIntegrationTest {

    @Test
    void atm_withdraw_200_by_two_banknotes(){

        int quantityBanknotes = 5;
        Set<Denomination> denominations = Set.of(TWO_HUNDRED);
        ATMCellHolderImpl atmCashCellHolder = getAtmCellHolder(quantityBanknotes, denominations);

        ATM atm = new ATMImpl(atmCashCellHolder);
        assertEquals(1000, atm.getBalance().getData());

        //act
        ATMResponse<Set<Banknote>> withdraw = atm.withdraw(200);

        //assert
        assertNotNull(withdraw.getData());
        assertEquals(1, withdraw.getData().size());
        assertEquals(800, atm.getBalance().getData());
    }

    @Test
    void atm_withdraw_negative_amount(){

        int quantityBanknotes = 2;
        Set<Denomination> denominations = Set.of(ONE_THOUSAND, FIVE_HUNDRED);
        ATMCellHolderImpl atmCashCellHolder = getAtmCellHolder(quantityBanknotes, denominations);

        ATM atm = new ATMImpl(atmCashCellHolder);
        assertEquals(3000, atm.getBalance().getData());

        //act
        ATMResponse<Set<Banknote>> withdraw = atm.withdraw(-3500);

        //assert
        assertEquals(1, withdraw.getErrors().size());
        assertEquals(INCORRECT_AMOUNT.getCode(), withdraw.getErrors().get(0).getCode());
        assertNull(withdraw.getData());
    }

    @Test
    void atm_withdraw_with_empty_denomination(){

        ATMCellHolderImpl atmCashCellHolder = new ATMCellHolderImpl();

        ATM atm = new ATMImpl(atmCashCellHolder);
        assertEquals(0, atm.getBalance().getData());

        //act
        ATMResponse<Set<Banknote>> withdraw = atm.withdraw(3500);

        //assert
        assertEquals(1, withdraw.getErrors().size());
        assertEquals(EMPTY_DENOMINATION.getCode(), withdraw.getErrors().get(0).getCode());
        assertNull(withdraw.getData());
    }

    @Test
    void atm_withdraw_1500_from_balance_1000(){

        int quantityBanknotes = 2;
        Set<Denomination> denominations = Set.of(ONE_THOUSAND, FIVE_HUNDRED);
        ATMCellHolderImpl atmCashCellHolder = getAtmCellHolder(quantityBanknotes, denominations);

        ATM atm = new ATMImpl(atmCashCellHolder);
        assertEquals(3000, atm.getBalance().getData());

        //act
        ATMResponse<Set<Banknote>> withdraw = atm.withdraw(3500);

        //assert
        assertEquals(1, withdraw.getErrors().size());
        assertEquals(NOT_ENOUGH_MONEY.getCode(), withdraw.getErrors().get(0).getCode());
        assertNull(withdraw.getData());
    }

    @Test
    void atm_withdraw_2250_from_balance_2200(){

        int quantityBanknotes = 2;
        Set<Denomination> denominations = Set.of(ONE_THOUSAND, ONE_HUNDRED);
        ATMCellHolderImpl atmCashCellHolder = getAtmCellHolder(quantityBanknotes, denominations);

        ATM atm = new ATMImpl(atmCashCellHolder);
        assertEquals(2200, atm.getBalance().getData());

        //act
        ATMResponse<Set<Banknote>> withdraw = atm.withdraw(1250);

        //assert
        assertEquals(1, withdraw.getErrors().size());
        assertEquals(NON_COMPLIANCE_MIN_DENOMINATION.getCode(), withdraw.getErrors().get(0).getCode());
        assertNull(withdraw.getData());
    }

    @Test
    void atm_withdraw_1220_from_balance_2200(){

        int quantityBanknotes = 2;
        Set<Denomination> denominations = Set.of(ONE_THOUSAND, ONE_HUNDRED);
        ATMCellHolderImpl atmCashCellHolder = getAtmCellHolder(quantityBanknotes, denominations);

        ATM atm = new ATMImpl(atmCashCellHolder);
        assertEquals(2200, atm.getBalance().getData());

        //act
        ATMResponse<Set<Banknote>> withdraw = atm.withdraw(1250);

        //assert
        assertEquals(1, withdraw.getErrors().size());
        assertEquals(NON_COMPLIANCE_MIN_DENOMINATION.getCode(), withdraw.getErrors().get(0).getCode());
        assertNull(withdraw.getData());
    }

    private static ATMCellHolderImpl getAtmCellHolder(int quantityBanknotes, Set<Denomination> denominations) {
        ATMCellHolderImpl atmCashCellHolder = new ATMCellHolderImpl();
        denominations.forEach(denomination -> {
            Set<Banknote> banknotes = IntStream.range(0, quantityBanknotes)
                    .mapToObj(i -> new Banknote(denomination.value() + i, denomination))
                    .collect(Collectors.toSet());
            atmCashCellHolder.addCell(new ATMCashCell(denomination, banknotes));
        });
        return atmCashCellHolder;
    }
}
