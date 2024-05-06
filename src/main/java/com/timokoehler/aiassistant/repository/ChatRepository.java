package com.timokoehler.aiassistant.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.timokoehler.aiassistant.model.Chat;

public interface ChatRepository extends JpaRepository<Chat, Long> {

}
