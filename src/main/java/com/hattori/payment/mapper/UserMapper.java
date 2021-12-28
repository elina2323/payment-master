package com.hattori.payment.mapper;

import com.hattori.payment.model.dto.UserDto;
import com.hattori.payment.model.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    User mapToUser(UserDto userDto);

    UserDto mapToUserDto(User user);
}
