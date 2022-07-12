package com.antipov.task.crypto.util.convertors;

import com.antipov.task.crypto.dto.CryptoCurrencyDto;
import com.antipov.task.crypto.entity.CryptoCurrencyEntity;
import org.springframework.stereotype.Component;
import org.springframework.core.convert.converter.Converter;

@Component
public class CryptoCurrencyEntityToDto implements Converter<CryptoCurrencyEntity, CryptoCurrencyDto>  {

    @Override
    public CryptoCurrencyDto convert(CryptoCurrencyEntity source) {
        CryptoCurrencyDto cryptoCurrencyDto = new CryptoCurrencyDto();
        cryptoCurrencyDto.setId(source.getId());
        cryptoCurrencyDto.setName(source.getName());
        cryptoCurrencyDto.setPrice(source.getPrice());
        cryptoCurrencyDto.setSymbol(source.getSymbol());
        return cryptoCurrencyDto;
    }
}
