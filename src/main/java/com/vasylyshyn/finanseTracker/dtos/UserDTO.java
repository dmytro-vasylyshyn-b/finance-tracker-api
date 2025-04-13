package com.vasylyshyn.finanseTracker.dtos;

import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    private String email;
    private String phoneNumber;

    private String firstName;
    private String lastName;
    private String middleName;

    private String profileImageUrl;

    private String preferredTheme;
    private String preferredLanguage;

    private boolean phoneVerified;
    private boolean emailVerified;
}
