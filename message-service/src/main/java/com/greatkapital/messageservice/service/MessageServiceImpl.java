package com.greatkapital.messageservice.service;

import com.greatkapital.messageservice.dto.EncryptionRequest;
import com.greatkapital.messageservice.dto.EncryptionResponse;
import com.greatkapital.messageservice.dto.MessageResponse;
import com.greatkapital.messageservice.entity.Message;
import com.greatkapital.messageservice.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${encryption.service.url}")
    private String encryptionServiceUrl;

    @Override
    public MessageResponse sendMessage(String sender, String recipient, String content, String encryptionType)
            throws Exception {
        String encryptedContent = encryptContent(content, encryptionType);

        Message message = new Message();
        message.setSender(sender);
        message.setRecipient(recipient);
        message.setEncryptedContent(encryptedContent);
        message.setTimestamp(LocalDateTime.now().toString());

        Message savedMessage = messageRepository.save(message);

        return new MessageResponse(savedMessage.getId(), savedMessage.getSender(), savedMessage.getRecipient(),
                "Message sent successfully.");
    }

    @Override
    public List<MessageResponse> getMessagesForUser(String username) throws Exception {
        List<Message> messages = messageRepository.findMessagesForUser(username);

        return messages.stream()
                .filter(message -> {
                    // Additional security check: ensure user is either sender or recipient
                    boolean isAuthorized = username.equals(message.getSender()) || username.equals(message.getRecipient());
                    if (!isAuthorized) {
                        System.out.println("SECURITY WARNING: User " + username + " tried to access message " + message.getId() + 
                                         " from " + message.getSender() + " to " + message.getRecipient());
                    }
                    return isAuthorized;
                })
                .map(message -> {
                    try {
                        String decryptedContent = decryptContent(message.getEncryptedContent(), "AES");
                        return new MessageResponse(message.getId(), message.getSender(), message.getRecipient(),
                                decryptedContent);
                    } catch (Exception e) {
                        return new MessageResponse(message.getId(), message.getSender(), message.getRecipient(),
                                "Decryption failed.");
                    }
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<MessageResponse> getSentMessagesForUser(String username) throws Exception {
        List<Message> messages = messageRepository.findSentMessagesForUser(username);

        return messages.stream()
                .map(message -> {
                    try {
                        String decryptedContent = decryptContent(message.getEncryptedContent(), "AES");
                        return new MessageResponse(message.getId(), message.getSender(), message.getRecipient(),
                                decryptedContent);
                    } catch (Exception e) {
                        return new MessageResponse(message.getId(), message.getSender(), message.getRecipient(),
                                "Decryption failed.");
                    }
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<MessageResponse> getReceivedMessagesForUser(String username) throws Exception {
        List<Message> messages = messageRepository.findReceivedMessagesForUser(username);

        return messages.stream()
                .map(message -> {
                    try {
                        String decryptedContent = decryptContent(message.getEncryptedContent(), "AES");
                        return new MessageResponse(message.getId(), message.getSender(), message.getRecipient(),
                                decryptedContent);
                    } catch (Exception e) {
                        return new MessageResponse(message.getId(), message.getSender(), message.getRecipient(),
                                "Decryption failed.");
                    }
                })
                .collect(Collectors.toList());
    }

    private String encryptContent(String content, String encryptionType) {
        String url = encryptionServiceUrl + "/encrypt";
        EncryptionRequest request = new EncryptionRequest();
        request.setMessage(content);
        request.setEncryptionType(encryptionType);
        return restTemplate.postForObject(url, request, EncryptionResponse.class).getEncryptedMessage();
    }

    private String decryptContent(String encryptedContent, String encryptionType) {
        String url = encryptionServiceUrl + "/decrypt";
        EncryptionRequest request = new EncryptionRequest();
        request.setMessage(encryptedContent);
        request.setEncryptionType(encryptionType);
        return restTemplate.postForObject(url, request, EncryptionResponse.class).getEncryptedMessage();
    }
}