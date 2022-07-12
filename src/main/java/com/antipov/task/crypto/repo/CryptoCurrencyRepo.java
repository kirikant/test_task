package com.antipov.task.crypto.repo;

import com.antipov.task.crypto.entity.CryptoCurrencyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface CryptoCurrencyRepo extends JpaRepository<CryptoCurrencyEntity,Long> {
    Optional<CryptoCurrencyEntity> findBySymbol(String symbol);
}
