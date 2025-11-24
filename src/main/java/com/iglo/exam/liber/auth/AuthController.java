package com.iglo.exam.liber.auth;

import com.iglo.exam.liber.auth.dto.AuthJwtRequest;
import com.iglo.exam.liber.auth.dto.AuthJwtResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    public final AuthService authService;

    @GetMapping("{username}")
    public ResponseEntity<Authentication> getByUsername(@PathVariable String username, Authentication authentication){
        return ResponseEntity.ok(authentication);
    }

    @GetMapping("create-token")
    public ResponseEntity<AuthJwtResponse> createToken(@RequestBody  AuthJwtRequest authJwtRequest) {
       return ResponseEntity.ok(authService.createToken(authJwtRequest));
    }
}
