package com.timokoehler.aiassistant.repository;

import java.util.List;

import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;

import com.timokoehler.aiassistant.model.Chat;
import com.timokoehler.aiassistant.model.Message;

public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findAllByChatOrderByCreatedAtDesc(Chat chat, Limit limit);
}
