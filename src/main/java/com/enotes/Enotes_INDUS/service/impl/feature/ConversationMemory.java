package com.enotes.Enotes_INDUS.service.impl.feature;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ConversationMemory     {
    private final Map<String, List<String>> memory=new ConcurrentHashMap<>();

    public String withHistory(String conversationId, String newPrompt) {
        List<String> history = memory.computeIfAbsent(conversationId, k -> new ArrayList<>());
        String fullPrompt = String.join("\n", history) + "\nUser: " + newPrompt;
        history.add("User: " + newPrompt);
        return fullPrompt;
    }
    public void addResponse(String conversationId, String response) {
        memory.get(conversationId).add("Assistant: " + response);
    }

//    conversationId = userId + ":" + noteId

}
