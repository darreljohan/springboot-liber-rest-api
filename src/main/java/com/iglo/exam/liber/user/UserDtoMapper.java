package com.iglo.exam.liber.user;

import com.iglo.exam.liber.user.dto.UserDetailResponse;
import com.iglo.exam.liber.user.dto.UserResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "Spring")
public interface UserDtoMapper {
    UserResponse toUserResponse(User user);
    UserDetailResponse toUserDetailResponse(User user);
}
