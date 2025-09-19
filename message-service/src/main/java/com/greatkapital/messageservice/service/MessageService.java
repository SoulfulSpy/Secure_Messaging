package com.greatkapital.messageservice.service;

import com.greatkapital.messageservice.dto.MessageResponse;
import java.util.List;

public interface MessageService {
    MessageResponse sendMessage(String sender, String recipient, String content, String encryptionType)
            throws Exception;

    List<MessageResponse> getMessagesForUser(String username) throws Exception;
    
    List<MessageResponse> getSentMessagesForUser(String username) throws Exception;
    
    List<MessageResponse> getReceivedMessagesForUser(String username) throws Exception;
}