package com.vasylyshyn.finanseTracker.dtos;

import lombok.Data;

@Data
public class PasswordChangeDto {
    private String oldPassword;
    private String newPassword;
}
