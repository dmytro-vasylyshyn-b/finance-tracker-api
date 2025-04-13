package com.vasylyshyn.finanseTracker.dtos;

import lombok.Data;

@Data
public class UpdateProfileDto {
    private String firstName;
    private String lastName;
    private String middleName;
    private String preferredLanguage;
    private String preferredTheme;
}