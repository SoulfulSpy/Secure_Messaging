package com.greatkapital.messageservice.dto;

import lombok.Data;

@Data
public class EncryptionRequest {
    private String message;
    private String encryptionType;
}