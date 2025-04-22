package com.ftohbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PasswordResetRequest {

    private String email;
    private String otp;
    private String newPassword;
    private String userType;

    
}
