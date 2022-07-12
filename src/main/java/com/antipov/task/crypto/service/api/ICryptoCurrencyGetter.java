package com.antipov.task.crypto.service.api;

import com.antipov.task.crypto.dto.CryptoCurrencyDto;

import java.io.IOException;
import java.util.List;

public interface ICryptoCurrencyGetter {

     List<CryptoCurrencyDto> getAllCrypto();
     CryptoCurrencyDto getCryptoCurrency(String symbol);
     void update() throws IOException;
}
