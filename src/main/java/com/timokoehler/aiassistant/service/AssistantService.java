package com.timokoehler.aiassistant.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.data.domain.Limit;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.timokoehler.aiassistant.dto.AssistantRequest;
import com.timokoehler.aiassistant.model.Chat;
import com.timokoehler.aiassistant.model.Message;
import com.timokoehler.aiassistant.repository.ChatRepository;
import com.timokoehler.aiassistant.repository.MessageRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@Service
@RequiredArgsConstructor
@Log
public class AssistantService {
    private final ChatRepository chatRepository;
    private final MessageRepository messageRepository;

    private final ChatClient chatClient;

    public Message askAssistant(AssistantRequest request) {
        Chat chat;
        // get the create chat
        if (request.getChatId() == 0) {
            chat = chatRepository.save(new Chat(LocalDateTime.now()));
        } else {
            chat = chatRepository.findById(request.getChatId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        }

        List<Message> messages = messageRepository.findAllByChatOrderByCreatedAtDesc(chat, Limit.of(10));
        String history = formatMessages(messages);
        ChatResponse assistantResponse = askAI(history, request.getMessage());
        String response = assistantResponse.getResult().getOutput().getContent(); // get the response from the AI
        return messageRepository.save(new Message(request.getMessage(), response, LocalDateTime.now(), chat));

    }

    private ChatResponse askAI(String chatHistory, String question) {
        final String promptText = """
                You are a AI Personal Assitant like Siri, Alexa, or Google Assistant.
                You are designed to help users with their daily tasks and answer their questions.
                You're Input comes from a Speech-to-Text API so keep in mind that the input might not be perfect.
                Youre Response should be maximul 2-4 sentences long.
                Please respond in the language the user asked the question in.

                Chat History (newest first):
                {history}

                Question from the User:
                {question}
                """;

        final PromptTemplate promptTemplate = new PromptTemplate(promptText);
        promptTemplate.add("history", chatHistory);
        promptTemplate.add("question", question);

        log.info(promptTemplate.create().getContents());

        return chatClient.call(promptTemplate.create());
    }

    private String formatMessages(List<Message> messages) {
        StringBuilder formattedMessages = new StringBuilder();
        for (Message message : messages) {
            formattedMessages.append("Q: ");
            formattedMessages.append(message.getQuestion());
            formattedMessages.append("\n");
            formattedMessages.append("A: ");
            formattedMessages.append(message.getAnswer());
            formattedMessages.append("\n");
        }
        return formattedMessages.toString();
    }

}
