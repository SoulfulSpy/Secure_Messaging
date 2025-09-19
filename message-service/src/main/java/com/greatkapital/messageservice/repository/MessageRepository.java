package com.greatkapital.messageservice.repository;

import com.greatkapital.messageservice.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findBySenderOrRecipientOrderByTimestampAsc(String sender, String recipient);
    
    @Query("SELECT m FROM Message m WHERE (m.sender = :username OR m.recipient = :username) ORDER BY m.timestamp ASC")
    List<Message> findMessagesForUser(@Param("username") String username);
    
    @Query("SELECT m FROM Message m WHERE m.sender = :username ORDER BY m.timestamp ASC")
    List<Message> findSentMessagesForUser(@Param("username") String username);
    
    @Query("SELECT m FROM Message m WHERE m.recipient = :username ORDER BY m.timestamp ASC")
    List<Message> findReceivedMessagesForUser(@Param("username") String username);
}