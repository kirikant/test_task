package com.antipov.task.crypto.repo;

import com.antipov.task.crypto.entity.UserRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRequestRepo extends JpaRepository<UserRequestEntity, UUID> {

    Optional<List<UserRequestEntity>> findAllBySymbol(String symbol);

}
