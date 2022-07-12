package com.antipov.task.crypto.exception;

import javax.persistence.EntityNotFoundException;

public class CryptoCurrencyNotFoundException extends EntityNotFoundException {

    public CryptoCurrencyNotFoundException() {
    }

    public CryptoCurrencyNotFoundException(String message) {
        super(message);
    }
}
