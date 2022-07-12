package com.antipov.task.crypto.service;

import com.antipov.task.crypto.dto.CryptoCurrencyDto;
import com.antipov.task.crypto.entity.CryptoCurrencyEntity;
import com.antipov.task.crypto.exception.CryptoCurrencyNotFoundException;
import com.antipov.task.crypto.repo.CryptoCurrencyRepo;
import com.antipov.task.crypto.service.api.ICryptoCurrencyGetter;
import com.antipov.task.crypto.util.convertors.CryptoCurrencyDtoToEntity;
import com.antipov.task.crypto.util.convertors.CryptoCurrencyEntityToDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@EnableScheduling
@Transactional(readOnly = true)
public class CryptoCurrencyGetter implements ICryptoCurrencyGetter {

    @Value("${crypto.base.uri}")
    private String uri;
    private final StartLoader startLoader;
    private final ObjectMapper objectMapper;
    private final CryptoCurrencyRepo cryptoCurrencyRepo;
    private final CryptoCurrencyEntityToDto cryptoCurrencyEntityToDto;
    private final CryptoCurrencyDtoToEntity cryptoCurrencyDtoToEntity;
    private final CryptoChangesChecker cryptoChangesChecker;

    public CryptoCurrencyGetter(StartLoader startLoader, ObjectMapper objectMapper,
                                CryptoCurrencyRepo cryptoCurrencyRepo,
                                CryptoCurrencyEntityToDto cryptoCurrencyEntityToDto,
                                CryptoCurrencyDtoToEntity cryptoCurrencyDtoToEntity,
                                CryptoChangesChecker cryptoChangesChecker) {
        this.startLoader = startLoader;

        this.objectMapper = objectMapper;
        this.cryptoCurrencyRepo = cryptoCurrencyRepo;
        this.cryptoCurrencyEntityToDto = cryptoCurrencyEntityToDto;
        this.cryptoCurrencyDtoToEntity = cryptoCurrencyDtoToEntity;
        this.cryptoChangesChecker = cryptoChangesChecker;
    }

    public List<CryptoCurrencyDto> getAllCrypto() {
        List<CryptoCurrencyEntity> cryptoCurrencies = cryptoCurrencyRepo.findAll();
        if (cryptoCurrencies.isEmpty()) {
            throw new CryptoCurrencyNotFoundException("there is no currencies");
        } else
            return cryptoCurrencies.stream()
                    .map(cryptoCurrencyEntityToDto::convert)
                    .collect(Collectors.toList());
    }

    public CryptoCurrencyDto getCryptoCurrency(String symbol) {
        return cryptoCurrencyEntityToDto.convert(cryptoCurrencyRepo.findBySymbol(symbol)
                .orElseThrow(() -> new CryptoCurrencyNotFoundException("currency " + symbol + " was not found")));
    }

    @Transactional
    @Scheduled(fixedDelayString = "${trigger.time}")
    public void update() throws IOException {
        List<CryptoCurrencyEntity> cryptoCurrencies = cryptoCurrencyRepo.findAll();

        if (cryptoCurrencies.isEmpty()) {
            startLoader.preloadData();
            cryptoCurrencies = cryptoCurrencyRepo.findAll();

            List<String> currencyIds = cryptoCurrencies.stream()
                    .map(cryptoCurrencyEntity -> cryptoCurrencyEntity.getId().toString()).toList();
            List<CryptoCurrencyEntity> cryptoCurrenciesNew = updateRequest(currencyIds).stream()
                    .map(cryptoCurrencyDtoToEntity::convert).toList();
            cryptoCurrencyRepo.saveAll(cryptoCurrenciesNew);

        } else {
            List<String> currencyIds = cryptoCurrencies.stream()
                    .map(cryptoCurrencyEntity -> cryptoCurrencyEntity.getId().toString()).toList();
            List<CryptoCurrencyEntity> cryptoCurrenciesNew = updateRequest(currencyIds).stream()
                    .map(cryptoCurrencyDtoToEntity::convert).toList();
            cryptoChangesChecker.checkChanges(cryptoCurrenciesNew);
            cryptoCurrencyRepo.saveAll(cryptoCurrenciesNew);
        }
    }

    private List<CryptoCurrencyDto> updateRequest(List<String> currencyIds) throws IOException {
        ResponseEntity<String> response = new RestTemplate()
                .getForEntity(uri.concat(String.join(",", currencyIds)), String.class);
        return objectMapper.readValue(response.getBody(), new TypeReference<>() {
        });
    }


}
