package com.example.passwordmanager.Conatiners;

import com.example.passwordmanager.Dto.TokenDto;
import com.example.passwordmanager.Dto.UnverifiedUserToken;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

@Service
public class UnverifiedUserContainer {
    private final Map<String, UnverifiedUserToken> tokens = new HashMap<>();

    public void addToken(String key, UnverifiedUserToken token) {
        tokens.put(key, token);
    }

    public Object removeToken(String key) {
        return tokens.remove(key);
    }

    public static String generateUUID() {
        return UUID.randomUUID().toString();
    }

    @Scheduled(initialDelay =  60,fixedDelay =  60,timeUnit = TimeUnit.MINUTES)
    public void refreshContainer(){
        System.out.println("start schedule");
        Iterator<String> iterator = tokens.keySet().iterator();
        Instant now = Instant.now();
        while (iterator.hasNext()){
            String key = iterator.next();
            UnverifiedUserToken  unverifiedUserToken =  tokens.get(key);
            if (unverifiedUserToken.getDate().plus(15, ChronoUnit.MINUTES).isBefore(now)){
                this.removeToken(key);
            }
        }

    }


}

