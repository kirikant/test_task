package com.antipov.task.crypto.service;

import com.antipov.task.crypto.entity.CryptoCurrencyEntity;
import com.antipov.task.crypto.entity.UserRequestEntity;
import com.antipov.task.crypto.repo.CryptoCurrencyRepo;
import com.antipov.task.crypto.repo.UserRequestRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

@Service
public class CryptoChangesChecker {

    @Value("${trigger.percent}")
    private String percent;
    private Logger logger = LoggerFactory.getLogger(CryptoChangesChecker.class);
    private final CryptoCurrencyRepo cryptoCurrencyRepo;
    private final UserRequestRepo userRepo;

    public CryptoChangesChecker(CryptoCurrencyRepo cryptoCurrencyRepo, UserRequestRepo userRepo) {
        this.cryptoCurrencyRepo = cryptoCurrencyRepo;
        this.userRepo = userRepo;
    }

    public void checkChanges(List<CryptoCurrencyEntity> newCurrencies) {
        newCurrencies.forEach(currency->{
            Optional<CryptoCurrencyEntity> oldCurrency = cryptoCurrencyRepo.findById(currency.getId());
            if (oldCurrency.isPresent()){
                if (!currency.getPrice().equals(oldCurrency.get().getPrice()))
                    checkOneChange(currency);
            }
        });
    }

    @Async
    public void checkOneChange(CryptoCurrencyEntity newCurrency) {

        Optional<List<UserRequestEntity>> users = userRepo.findAllBySymbol(newCurrency.getSymbol());
        if (users.isPresent()) {
            users.get().forEach(user -> {
                BigDecimal change = calculateChange(newCurrency, user.getPrice());

                if (change.floatValue() >= Float.parseFloat(percent) ||
                        change.floatValue() <= -Float.parseFloat(percent)) {
                    logger.warn("Currency: " + newCurrency.getSymbol() + " ,username: "
                            + user.getUsername() + " ,price change:" + change + "%");
                }
            });
        }
    }

    private BigDecimal calculateChange(CryptoCurrencyEntity newCurrency,
                                       BigDecimal oldPrice) {
        return newCurrency.getPrice()
                .subtract(oldPrice)
                .divide(oldPrice, 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));
    }
}
