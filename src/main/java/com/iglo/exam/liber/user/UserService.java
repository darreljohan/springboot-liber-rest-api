package com.iglo.exam.liber.user;

import com.iglo.exam.liber.error.exception.ResourceNotFound;
import com.iglo.exam.liber.role.Role;
import com.iglo.exam.liber.role.RoleRepository;
import com.iglo.exam.liber.user.dto.AuthRegisterRequest;
import com.iglo.exam.liber.user.dto.UserDetailResponse;
import com.iglo.exam.liber.user.dto.UserResponse;
import com.iglo.exam.liber.utils.NameUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    public final UserRepository userRepository;
    public final RoleRepository roleRepository;
    public final UserDtoMapper userDtoMapper;
    public final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, UserDtoMapper userDtoMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userDtoMapper = userDtoMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public Page<UserResponse> findAllUsers(Pageable pageable, String fullname) {
        String[] nameParts = NameUtils.splitFullName(fullname);
        return userRepository.findByDeactivatedFalseByFullName(pageable, nameParts[0], nameParts[1])
                    .map(userDtoMapper::toUserResponse);
    }

    public UserDetailResponse findUserByUsername(String username) {
        User user = userRepository.findByUsernameAndDeactivatedFalse(username)
                .orElseThrow(() -> new ResourceNotFound("User not found with username: " + username));
        return userDtoMapper.toUserDetailResponse(user);
    }

    public UserDetailResponse activateUser(String username) {
        User user = userRepository.findByUsernameAndDeactivatedFalse(username)
                .orElseThrow(() -> new ResourceNotFound("User not found with username: " + username));
        user.setDeactivated(false);
        userRepository.save(user);
        return userDtoMapper.toUserDetailResponse(user);
    }

    public UserDetailResponse deactivateUser(String username) {
        User user = userRepository.findByUsernameAndDeactivatedFalse(username)
                .orElseThrow(() -> new ResourceNotFound("User not found with username: " + username));
        user.setDeactivated(true);
        userRepository.save(user);
        return userDtoMapper.toUserDetailResponse(user);
    }

    public UserDetailResponse registerUser(AuthRegisterRequest authRegisterRequest) {
        User user = userDtoMapper.toUser(authRegisterRequest);
        user.setPassword(passwordEncoder.encode(authRegisterRequest.getPassword()));
        Role role = roleRepository.findByRoleName(authRegisterRequest.getRole())
                .orElseThrow(() -> new ResourceNotFound("Role not found: " + authRegisterRequest.getRole()));
        user.setRole(role);
        User userSaved = userRepository.save(user);
        return userDtoMapper.toUserDetailResponse(userSaved);
    }
}
