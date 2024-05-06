package com.timokoehler.aiassistant.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Message {
    @Id
    @GeneratedValue
    private long id;
    private String question;
    private String answer;
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "chat_id")
    @JsonManagedReference
    private Chat chat;

    public Message(String question, String answer, LocalDateTime createdAt, Chat chat) {
        this.question = question;
        this.answer = answer;
        this.createdAt = LocalDateTime.now();
        this.chat = chat;
    }
}
