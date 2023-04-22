package com.explore.mainservice.user.service;

import com.explore.mainservice.exceptions.BadRequestException;
import com.explore.mainservice.exceptions.ConflictException;
import com.explore.mainservice.exceptions.NotFoundException;
import com.explore.mainservice.user.dto.NewUserRequestDto;
import com.explore.mainservice.user.dto.UserDto;
import com.explore.mainservice.user.dto.UserShortDto;
import com.explore.mainservice.user.jpa.UserPersistService;
import com.explore.mainservice.user.mapper.UserMapper;
import com.explore.mainservice.user.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserPersistService userPersistService;
    private final UserMapper userMapper;

    @Override
    public List<UserDto> getUsers(List<Long> ids, int from, int size) {

        var users = userPersistService.getUsers(ids, from, size).getContent();

        if (users.isEmpty()) {
            return Collections.emptyList();
        }

        return users.stream()
                .map(userMapper::toUserDto).collect(Collectors.toList());
    }

    @Override
    public UserDto createUser(NewUserRequestDto newUserRequestDto) {

        if (newUserRequestDto.getName() == null) {
            throw new BadRequestException("Bad request body", "User name is empty");
        }
        List<User> users = userPersistService.findUsersByName(newUserRequestDto.getName());
        if (!CollectionUtils.isEmpty(users)) {
            throw new ConflictException("Integrity constraint has been violated.", "User already exist ");
        }
        User user = userMapper.toUser(newUserRequestDto);

        userPersistService.createUser(user);

        return userMapper.toUserDto(userPersistService.createUser(userMapper.toUser(newUserRequestDto)));
    }

    @Override
    public void deleteUserById(Long id) {

        getUserShortById(id);

        userPersistService.deleteUserById(id);

    }

    public UserShortDto getUserShortById(Long id) {

        Optional<User> user = userPersistService.findUserById(id);

        if (user.isEmpty()) {
            throw new NotFoundException("The required object was not found.",
                    String.format("User with id = %s was not found.", id));
        }

        return userMapper.toUserShortDto(user.get());
    }
}
