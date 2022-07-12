package com.antipov.task.crypto.util.convertors;

import com.antipov.task.crypto.dto.UserRequestDto;
import com.antipov.task.crypto.entity.CryptoCurrencyEntity;
import com.antipov.task.crypto.entity.UserRequestEntity;
import com.antipov.task.crypto.exception.CryptoCurrencyNotFoundException;
import com.antipov.task.crypto.repo.CryptoCurrencyRepo;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserRequestDtoToEntity implements Converter<UserRequestDto, UserRequestEntity> {

    private final CryptoCurrencyRepo cryptoCurrencyRepo;

    public UserRequestDtoToEntity(CryptoCurrencyRepo cryptoCurrencyRepo) {
        this.cryptoCurrencyRepo = cryptoCurrencyRepo;
    }

    @Override
    public UserRequestEntity convert(UserRequestDto source) {
        UserRequestEntity userRequestEntity = new UserRequestEntity();
        userRequestEntity.setUsername(source.getUsername());
        userRequestEntity.setSymbol(source.getSymbol());

        CryptoCurrencyEntity cryptoCurrencyEntity = cryptoCurrencyRepo.findBySymbol(source.getSymbol())
                .orElseThrow(() -> new CryptoCurrencyNotFoundException("currency "
                        + source.getSymbol() + " was not found"));

        userRequestEntity.setPrice(cryptoCurrencyEntity.getPrice());
        return userRequestEntity ;
    }
}
