package com.vasylyshyn.finanseTracker.Controllers;

import com.vasylyshyn.finanseTracker.Entitys.Users;
import com.vasylyshyn.finanseTracker.Services.UserService;
import com.vasylyshyn.finanseTracker.dtos.PasswordChangeDto;
import com.vasylyshyn.finanseTracker.dtos.UpdateProfileDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@RestController
@RequestMapping("/api/profile")
@PreAuthorize("isAuthenticated()")
public class ProfileController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<Optional<Users>> getProfile(Authentication auth) {
        return ResponseEntity.ok(userService.getUserByEmail(auth.getName()));
    }

    @PutMapping
    public ResponseEntity<Users> updateProfile(@RequestBody UpdateProfileDto dto, Authentication auth) {
        return ResponseEntity.ok(userService.updateProfile(auth.getName(), dto));
    }

    @PutMapping("/password")
    public ResponseEntity<Void> changePassword(@RequestBody PasswordChangeDto dto, Authentication auth) {
        userService.changePassword(auth.getName(), dto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/photo")
    public ResponseEntity<String> uploadPhoto(@RequestParam("file") MultipartFile file, Authentication auth) {
        return ResponseEntity.ok(userService.uploadProfilePhoto(auth.getName(), file));
    }
}
