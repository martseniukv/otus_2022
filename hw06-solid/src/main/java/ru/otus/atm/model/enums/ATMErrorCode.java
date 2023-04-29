package ru.otus.atm.model.enums;

import ru.otus.atm.model.ATMError;

public enum ATMErrorCode {

    EMPTY_DENOMINATION(1L, "Error: ATMImpl there is not a single denomination."),
    NOT_ENOUGH_MONEY(2L, "Error: There is not enough money."),
    INCORRECT_AMOUNT(3L, "Error: incorrect sum."),
    NON_COMPLIANCE_MIN_DENOMINATION(4L, "Error: requested amount is not a multiple of the minimum denomination")
    ;

    ATMErrorCode(long code, String message) {
        this.code = code;
        this.message = message;
    }

    private final long code;
    private final String message;

    public long getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public ATMError getATMError(){
        return new ATMError(code, message);
    }
}