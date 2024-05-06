package com.timokoehler.aiassistant.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.timokoehler.aiassistant.dto.AssistantRequest;
import com.timokoehler.aiassistant.model.Message;
import com.timokoehler.aiassistant.service.AssistantService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/assistant")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class AssistantController {

    @Value("${assistant.auth.secret-key}")
    private String authKey;

    private final AssistantService assistantService;

    @PostMapping
    public Message askAssistant(@RequestHeader("authKey") String authKey, @RequestBody AssistantRequest request) {
        // validate authKey
        if (!this.authKey.equals(authKey)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid authKey");
        }

        return assistantService.askAssistant(request);
    }

}
