package com.antipov.task.crypto.exception;

import com.antipov.task.crypto.dto.UserRequestDto;
import org.springframework.stereotype.Component;

@Component
public class ValidationChecker {

    public void checkPageParams(UserRequestDto userRequestDto) throws ValidationException {
        ValidationException validationException = new ValidationException();
        if (userRequestDto.getUsername()==null) validationException
                .add(new ValidationError("username", "the username parameter was not given"));
        else if (userRequestDto.getUsername().isEmpty()) validationException
                .add(new ValidationError("username", "the username parameter is incorrect"));
        if (userRequestDto.getSymbol() == null) validationException
                .add(new ValidationError("symbol", "the symbol parameter was not given"));
        else if (userRequestDto.getSymbol().isEmpty()) validationException
                .add(new ValidationError("symbol", "the symbol parameter is incorrect"));
        if (validationException.getValidationExceptions().size() > 0) throw validationException;
    }


}

