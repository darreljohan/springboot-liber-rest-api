package com.iglo.exam.liber.user;

import com.iglo.exam.liber.user.dto.AuthRegisterRequest;
import com.iglo.exam.liber.user.dto.UserDetailResponse;
import com.iglo.exam.liber.user.dto.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserDtoMapper {
    UserResponse toUserResponse(User user);
    UserDetailResponse toUserDetailResponse(User user);

    User toUser(AuthRegisterRequest authRegisterRequest);
}
