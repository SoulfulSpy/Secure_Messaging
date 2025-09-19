package com.greatkapital.encryptionservice.controller;

import com.greatkapital.encryptionservice.dto.EncryptionRequest;
import com.greatkapital.encryptionservice.dto.EncryptionResponse;
import com.greatkapital.encryptionservice.service.EncryptionStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class EncryptionController {

    private final Map<String, EncryptionStrategy> encryptionStrategies;

    @Autowired
    public EncryptionController(List<EncryptionStrategy> strategies) {
        this.encryptionStrategies = strategies.stream()
                .collect(Collectors.toMap(EncryptionStrategy::getStrategyName, s -> s));
    }

    @PostMapping("/encrypt")
    public ResponseEntity<?> encryptMessage(@RequestBody EncryptionRequest request) {
        EncryptionStrategy strategy = encryptionStrategies.get(request.getEncryptionType().toUpperCase());

        if (strategy == null) {
            return ResponseEntity.badRequest().body("Unsupported encryption type.");
        }

        try {
            String encryptedMessage = strategy.encrypt(request.getMessage());
            return ResponseEntity.ok(new EncryptionResponse(encryptedMessage));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Encryption failed: " + e.getMessage());
        }
    }

    @PostMapping("/decrypt")
    public ResponseEntity<?> decryptMessage(@RequestBody EncryptionRequest request) {
        EncryptionStrategy strategy = encryptionStrategies.get(request.getEncryptionType().toUpperCase());

        if (strategy == null) {
            return ResponseEntity.badRequest().body("Unsupported encryption type.");
        }

        try {
            String decryptedMessage = strategy.decrypt(request.getMessage());
            return ResponseEntity.ok(new EncryptionResponse(decryptedMessage));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Decryption failed: " + e.getMessage());
        }
    }
}