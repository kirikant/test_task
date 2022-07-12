package com.antipov.task.crypto.service;

import com.antipov.task.crypto.entity.CryptoCurrencyEntity;
import com.antipov.task.crypto.repo.CryptoCurrencyRepo;
import com.antipov.task.crypto.service.api.IStartLoader;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StartLoader implements IStartLoader {

    @Value("${data.path}")
    private String path;
    private final ObjectMapper objectMapper;
    private final CryptoCurrencyRepo cryptoCurrencyRepo;

    public StartLoader(ObjectMapper objectMapper, CryptoCurrencyRepo cryptoCurrencyRepo) {
        this.objectMapper = objectMapper;
        this.cryptoCurrencyRepo = cryptoCurrencyRepo;
    }

    @Transactional
    public void preloadData(){
        try (BufferedReader reader=new BufferedReader(new FileReader(path))){
            String dataJson = reader.lines().collect(Collectors.joining());
            List<CryptoCurrencyEntity> cryptoCurrencies = objectMapper.readValue(dataJson,new TypeReference<>() {
            });
            cryptoCurrencyRepo.saveAll(cryptoCurrencies);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
