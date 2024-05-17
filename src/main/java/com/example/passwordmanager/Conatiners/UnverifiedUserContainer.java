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
    private final Map<String, UnverifiedUserToken> unvertifedMap = new HashMap<>();
    public void addUnvertifedUser(String key ,UnverifiedUserToken unverifiedUserToken){
        unvertifedMap.put(key,unverifiedUserToken);
    }
    public UnverifiedUserToken removeUnvertifiedUser(String key){
        return unvertifedMap.remove(key);
    }
    public static String generateUUID(){
        String random  = UUID.randomUUID().toString();
        return  random;
    }
}
