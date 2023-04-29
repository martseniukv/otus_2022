package ru.otus.atm;

import ru.otus.atm.model.ATMCashCell;
import ru.otus.atm.model.Banknote;
import ru.otus.atm.model.enums.Denomination;
import ru.otus.atm.service.ATM;
import ru.otus.atm.service.holder.ATMCellHolderImpl;
import ru.otus.atm.service.impl.ATMImpl;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ATMDemo {

    public static void main(String[] args) {

        ATMCellHolderImpl atmCashCellHolder = new ATMCellHolderImpl();

        Arrays.stream(Denomination.values()).forEach(denomination -> {

            Set<Banknote> banknotes = IntStream.range(0, 5)
                    .mapToObj(i -> new Banknote(denomination.value() + i, denomination))
                    .collect(Collectors.toSet());

            atmCashCellHolder.addCell(new ATMCashCell(denomination, banknotes));
        });

        ATM atm = new ATMImpl(atmCashCellHolder);

        String atmBalance = "ATM balance: ";
        System.out.println(atmBalance + atm.getBalance());
        System.out.println("ATM withdraw 250: " + atm.withdraw(250));
        System.out.println(atmBalance + atm.getBalance());

        System.out.println("ATM withdraw 5150: " + atm.withdraw(5150));
        System.out.println(atmBalance + atm.getBalance());

        System.out.println("ATM withdraw 230: " + atm.withdraw(230));
        System.out.println(atmBalance + atm.getBalance());
    }
}