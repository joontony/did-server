package org.snubi.did.main.rabbitmq;

import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SocketSessionManager {

	private final Map<String, List<String>> userSessionsMap = new HashMap<>();

    public void addSession(String userId, String sessionId) {
        userSessionsMap.computeIfAbsent(userId, key -> new ArrayList<>()).add(sessionId);        
        System.out.println("WebSocketSessionManager addSession userSessionsMap " + userSessionsMap.size());
    }

    public void removeSession(String userId, String sessionId) {
        List<String> userSessions = userSessionsMap.get(userId);
        if (userSessions != null) {
            userSessions.remove(sessionId);
            if (userSessions.isEmpty()) {
                userSessionsMap.remove(userId);
            }
        }
    }

    public List<String> getUserSessions(String userId) {
        return userSessionsMap.getOrDefault(userId, new ArrayList<>());
    }

    public List<String> getAllSessions() {
        List<String> allSessions = new ArrayList<>();
        userSessionsMap.values().forEach(allSessions::addAll);
        return allSessions;
    }
    
    public Map<String, List<String>> getAllUserSessions() {    	
    	System.out.println("SocketSessionManager getAllUserSessions userSessionsMap " + userSessionsMap.size());    	
        return Collections.unmodifiableMap(userSessionsMap);
    }
}