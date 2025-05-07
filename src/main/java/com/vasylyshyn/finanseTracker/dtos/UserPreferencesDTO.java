package com.vasylyshyn.finanseTracker.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class UserPreferencesDTO {
    private String theme;
    private String language;
}
