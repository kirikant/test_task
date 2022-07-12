package com.antipov.task.crypto.util.convertors;

import com.antipov.task.crypto.dto.CryptoCurrencyDto;
import com.antipov.task.crypto.entity.CryptoCurrencyEntity;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CryptoCurrencyDtoToEntity implements Converter<CryptoCurrencyDto, CryptoCurrencyEntity> {


    @Override
    public CryptoCurrencyEntity convert(CryptoCurrencyDto source) {
        CryptoCurrencyEntity cryptoCurrencyEntity = new CryptoCurrencyEntity();
        cryptoCurrencyEntity.setId(source.getId());
        cryptoCurrencyEntity.setName(source.getName());
        cryptoCurrencyEntity.setPrice(source.getPrice());
        cryptoCurrencyEntity.setSymbol(source.getSymbol());
        return cryptoCurrencyEntity;
    }
}
