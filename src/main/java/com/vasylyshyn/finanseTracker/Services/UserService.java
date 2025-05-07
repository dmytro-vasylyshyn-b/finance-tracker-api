package com.vasylyshyn.finanseTracker.Services;
import com.vasylyshyn.finanseTracker.Entitys.Users;
import com.vasylyshyn.finanseTracker.Repositorys.UserRepository;
import com.vasylyshyn.finanseTracker.dtos.PasswordChangeDto;
import com.vasylyshyn.finanseTracker.dtos.UpdateProfileDto;
import com.vasylyshyn.finanseTracker.dtos.UserPreferencesDTO;
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
import java.util.UUID;

@Service
@Transactional
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final String uploadDir = "uploads/";

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


    public void changePassword(Authentication auth, PasswordChangeDto dto) {
        Users user_auth = (Users) auth.getPrincipal();
        Users user = userRepository.findByEmail(user_auth.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(dto.getOldPassword(), user.getPassword())) {
            throw new RuntimeException("Old password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(user);
    }

    public String uploadProfilePhoto(Authentication auth, MultipartFile file) {
        Users userAuth = (Users) auth.getPrincipal();
        Users user = userRepository.findByEmail(userAuth.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        try {
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                throw new IllegalArgumentException("Дозволено лише зображення");
            }

            if (file.getSize() > 5 * 1024 * 1024) {
                throw new IllegalArgumentException("Файл занадто великий");
            }

            Path uploadPath = Paths.get(uploadDir, "profile-photos");
            Files.createDirectories(uploadPath);
            String originalFilename = Paths.get(file.getOriginalFilename()).getFileName().toString();
            String filename = userAuth.getEmail().replaceAll("[^a-zA-Z0-9]", "_") + "_" + UUID.randomUUID() + "_" + originalFilename;
            Path filePath = uploadPath.resolve(filename);
            Files.write(filePath, file.getBytes());
            String relativePath = "/uploads/profile-photos/" + filename;
            System.out.println();
            System.out.println(relativePath);
            System.out.println();
            user.setProfileImagePath(relativePath);
            userRepository.save(user);
            System.out.println(user.getProfileImagePath());
            return "Фото профілю оновлено";
        } catch (IOException e) {
            throw new RuntimeException("Помилка при збереженні фото", e);
        }
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

    public void updateLanguageAndTheme(UserPreferencesDTO dto, Authentication authentication){
        Users userAuth = (Users) authentication.getPrincipal();
        Users user = userRepository.findByEmail(userAuth.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setPreferredTheme(dto.getTheme());
        user.setPreferredLanguage(dto.getLanguage());
        userRepository.save(user);
    }

    public UserPreferencesDTO getLanguageAndTheme(Authentication authentication){
        Users userAuth = (Users) authentication.getPrincipal();
        Users user = userRepository.findByEmail(userAuth.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));
        return new UserPreferencesDTO(user.getPreferredTheme(), user.getPreferredLanguage());
    }
}
