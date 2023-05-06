package com.explore.mainservice.user.service;

import com.explore.mainservice.user.dto.NewUserRequestDto;
import com.explore.mainservice.user.dto.UserDto;
import com.explore.mainservice.user.dto.UserShortDto;

import java.util.List;

public interface UserService {

    List<UserDto> getUsers(List<Long> ids, int from, int size);

    UserDto createUser(NewUserRequestDto newUserRequestDto);

    void deleteUserById(Long id);

    UserShortDto getUserShortById(Long id);
}
