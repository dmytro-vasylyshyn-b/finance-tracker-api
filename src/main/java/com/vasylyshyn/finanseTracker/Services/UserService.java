package com.vasylyshyn.finanseTracker.Services;
import com.vasylyshyn.finanseTracker.Entitys.Users;
import com.vasylyshyn.finanseTracker.Repositorys.UserRepository;
import com.vasylyshyn.finanseTracker.dtos.PasswordChangeDto;
import com.vasylyshyn.finanseTracker.dtos.UpdateProfileDto;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Optional;

@Service
@Transactional
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final String uploadDir = "uploads/profile-photos";

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Users updateProfile(UpdateProfileDto dto, Authentication auth) {
        Users user_auth = (Users) auth.getPrincipal();
        Users user = userRepository.findByEmail(user_auth.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setMiddleName(dto.getMiddleName());
        user.setPreferredLanguage(dto.getPreferredLanguage());
        user.setPreferredTheme(dto.getPreferredTheme());

        return userRepository.save(user);
    }


    public void changePassword(String email, PasswordChangeDto dto) {
        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(dto.getCurrentPassword(), user.getPassword())) {
            throw new RuntimeException("Old password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(user);
    }

    public String uploadProfilePhoto(String email, MultipartFile file) {
        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        try {
            Files.createDirectories(Paths.get(uploadDir));

            String filename = email.replaceAll("[^a-zA-Z0-9]", "_") + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(uploadDir, filename);
            Files.write(filePath, file.getBytes());

            user.setProfileImagePath(filePath.toString());
            userRepository.save(user);

            return "Фото профілю оновлено";
        } catch (IOException e) {
            throw new RuntimeException("Помилка при збереженні фото", e);
        }
    }

    private Users getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                new ArrayList<>()
        );
    }


    public Optional<Users> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

}
