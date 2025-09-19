package com.greatkapital.messageservice.dto;

import lombok.Data;

@Data
public class MessageRequest {
    private String recipient;
    private String content;
}