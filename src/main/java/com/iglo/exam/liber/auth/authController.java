package com.iglo.exam.liber.auth;

import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class authController {
    
    @GetMapping("{username}")
    public ResponseEntity<Authentication> getByUsername(@PathVariable String username, Authentication authentication){
        return ResponseEntity.ok(authentication);
    }
}
