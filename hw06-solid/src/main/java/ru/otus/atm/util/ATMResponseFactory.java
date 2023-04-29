package ru.otus.atm.util;

import ru.otus.atm.model.ATMError;
import ru.otus.atm.model.ATMResponse;

import java.util.ArrayList;
import java.util.List;

public class ATMResponseFactory {

    private ATMResponseFactory(){}

    public static <T> ATMResponse<T> getATMResponse(ATMError error){
        ATMResponse<T> atmResponse = new ATMResponse<>();
        atmResponse.setErrors(List.of(error));
        return atmResponse;
    }

    public static <T> ATMResponse<T> getATMResponse(T data){
        ATMResponse<T> atmResponse = new ATMResponse<>();
        atmResponse.setData(data);
        atmResponse.setErrors(new ArrayList<>());
        return atmResponse;
    }
}