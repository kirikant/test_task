package com.antipov.task.crypto.service.api;

import com.antipov.task.crypto.dto.UserRequestDto;

public interface IUserRequestService {
    void registerRequest(UserRequestDto userRequestDto);
}
