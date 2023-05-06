package com.explore.mainservice.user.mapper;

import com.explore.mainservice.user.dto.NewUserRequestDto;
import com.explore.mainservice.user.dto.UserDto;
import com.explore.mainservice.user.dto.UserShortDto;
import com.explore.mainservice.user.model.User;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface UserMapper {

    User toUser(NewUserRequestDto newUserRequestDto);

    UserDto toUserDto(User user);

    UserShortDto toUserShortDto(User user);
}
