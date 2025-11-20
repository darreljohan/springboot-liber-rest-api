package com.iglo.exam.liber.user;

import com.iglo.exam.liber.user.dto.UserDetailResponse;
import com.iglo.exam.liber.user.dto.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users")
public class UserController {
    public final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public ResponseEntity<Page<UserResponse>> findAllUsers(@PageableDefault(sort = "email", size = 10, page=0) Pageable pageable,
                                                           @RequestParam(required = false) String fullname) {
        return ResponseEntity.ok(userService.findAllUsers(pageable, fullname));
    }

    @GetMapping("{username}")
    public ResponseEntity<UserDetailResponse> findUserByUsername(@PathVariable String username) {
        return ResponseEntity.ok(userService.findUserByUsername(username));
    }

    @PatchMapping("{username}/activate")
    public ResponseEntity<UserDetailResponse> activateUser(@PathVariable String username) {
        return ResponseEntity.ok(userService.activateUser(username));
    }

    @PatchMapping("{username}/deactivate")
    public ResponseEntity<UserDetailResponse> deactivateUser(@PathVariable String username) {
        return ResponseEntity.ok(userService.deactivateUser(username));
    }
}
