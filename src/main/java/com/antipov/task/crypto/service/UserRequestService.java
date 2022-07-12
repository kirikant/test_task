package com.antipov.task.crypto.service;

import com.antipov.task.crypto.dto.UserRequestDto;
import com.antipov.task.crypto.repo.UserRequestRepo;
import com.antipov.task.crypto.service.api.IUserRequestService;
import com.antipov.task.crypto.util.convertors.UserRequestDtoToEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserRequestService implements IUserRequestService {

    private final UserRequestRepo userRequestRepo;
    private final UserRequestDtoToEntity userRequestDtoToEntity;

    public UserRequestService(UserRequestRepo userRequestRepo, UserRequestDtoToEntity userRequestDtoToEntity) {
        this.userRequestRepo = userRequestRepo;
        this.userRequestDtoToEntity = userRequestDtoToEntity;
    }

    @Transactional
    public void registerRequest(UserRequestDto userRequestDto) {
        userRequestRepo.save(userRequestDtoToEntity.convert(userRequestDto));
    }

}
