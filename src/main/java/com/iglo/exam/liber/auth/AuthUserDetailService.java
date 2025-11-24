package com.iglo.exam.liber.auth;

import com.iglo.exam.liber.auth.dto.AuthJwtResponse;
import com.iglo.exam.liber.user.User;
import com.iglo.exam.liber.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthUserDetailService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByUsernameAndDeactivatedFalse(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        return AuthUserDetails.builder().user(user).build();
    }

}
