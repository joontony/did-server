package org.snubi.did.main.cron;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpSession;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class BatchScheduler {
	
	@Autowired private SimpUserRegistry simpUserRegistry;

    @Autowired private SimpMessagingTemplate messagingTemplate;
	
	//@Scheduled(fixedRate = 5000) // 5초마다 실행
    public void updateDataAndNotifyUsers() {
        // 데이터베이스 갱신 로직 (예: 실제로는 데이터베이스에서 데이터를 가져오거나 갱신)

        // 갱신된 데이터
        String updatedData = "This is updated data from the scheduler.";
        System.out.println("updatedData " + updatedData);
        // 현재 연결된 사용자에게 갱신된 데이터 전송
        sendUpdatedDataToConnectedUsers(updatedData);
    }

    private void sendUpdatedDataToConnectedUsers(String updatedData) {
        // 현재 연결된 사용자 목록 확인
        List<String> connectedUserSessions = simpUserRegistry.getUsers()
                .stream()
                .flatMap(user -> user.getSessions().stream())
                .map(SimpSession::getId)
                .collect(Collectors.toList());
        
        System.out.println("connectedUserSessions " + connectedUserSessions.size());

        // 연결된 사용자에게 메시지 전송
        for (String sessionId : connectedUserSessions) {
        	System.out.println("sessionId " + sessionId);
        	SimpMessageHeaderAccessor accessor = SimpMessageHeaderAccessor.create();
            accessor.setSessionId(sessionId);        
            // 사용자 에게 메시지 전송
            messagingTemplate.convertAndSendToUser(sessionId, "/topic/greetings", updatedData , accessor.getMessageHeaders());
            
            //messagingTemplate.convertAndSendToUser(session, "/queue/data-update", updatedData);
        }
    }
}
