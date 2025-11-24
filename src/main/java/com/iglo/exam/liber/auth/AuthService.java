package com.iglo.exam.liber.auth;

import com.iglo.exam.liber.auth.dto.AuthJwtRequest;
import com.iglo.exam.liber.auth.dto.AuthJwtResponse;
import com.iglo.exam.liber.user.User;
import com.iglo.exam.liber.user.UserRepository;
import com.iglo.exam.liber.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    public final UserRepository userRepository;
    public final JwtService jwtService;

    public final AuthJwtResponse createToken(AuthJwtRequest authJwtRequest) {

        User user = userRepository.findByUsernameAndDeactivatedFalse(authJwtRequest.getUsername()).orElseThrow(() -> new RuntimeException("User not found"));
        String token = jwtService.generateToken(user);
        return AuthJwtResponse.builder().token(token).build();  // Logic to create JWT token for the user
    }
}
