package com.example.passwordmanager.Conatiners;

import com.example.passwordmanager.Dto.TokenDto;
import com.example.passwordmanager.Dto.UnverifiedUserToken;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentMap;

@Service
public class UnverifiedUserContainer {
    private final Map<String, Object> tokens = new HashMap<>(); // General map for tokens

    public void addToken(String key, Object token) {
        tokens.put(key, token);
    }

    public Object removeToken(String key) {
        return tokens.remove(key);
    }

    public static String generateUUID() {
        return UUID.randomUUID().toString();
    }
}

