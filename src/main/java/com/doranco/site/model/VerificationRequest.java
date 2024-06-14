package com.doranco.site.model;

import lombok.Data;

@Data

public class VerificationRequest {
    private String email;
    private String verificationCode;
}