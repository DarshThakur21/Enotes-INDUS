package com.enotes.Enotes_INDUS.service.impl.feature;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ChatContextStore {
    private static final int MAX_CHARS = 8000;
    private final Map<String, StringBuilder> store = new ConcurrentHashMap<>();

    public String append(String username, String message) {

        store.putIfAbsent(username, new StringBuilder());
        StringBuilder sb = store.get(username);

        sb.append(message).append("\n");

        if (sb.length() > MAX_CHARS) {
            sb.delete(0, sb.length() - MAX_CHARS);
        }

        return sb.toString();
    }

    public void clear(String username) {
        store.remove(username);
    }
}
