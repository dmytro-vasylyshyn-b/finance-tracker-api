package com.vasylyshyn.finanseTracker.dtos;

import lombok.Data;

@Data
public class PasswordChangeDto {
    private String currentPassword;
    private String newPassword;
}
