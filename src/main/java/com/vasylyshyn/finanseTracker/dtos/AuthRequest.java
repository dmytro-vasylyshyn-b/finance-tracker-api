package com.vasylyshyn.finanseTracker.dtos;

import lombok.Data;

@Data
public class AuthRequest {
    private String email;
    private String password;
}