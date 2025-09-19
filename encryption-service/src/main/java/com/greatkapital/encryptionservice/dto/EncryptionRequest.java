package com.greatkapital.encryptionservice.dto;

import lombok.Data;

@Data
public class EncryptionRequest {
    private String message;
    private String encryptionType; // "AES" or "RSA"
}