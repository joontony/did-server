package org.snubi.did.main.websocket;

import org.snubi.did.main.entity.AgentClubWaiting;
import org.snubi.did.main.rabbitmq.SocketSessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;
import java.util.List;
import java.util.Map;
import javax.persistence.PostPersist;
import javax.persistence.PreUpdate;

@Slf4j
@Component
public class AgentClubWaitingListener {	

	@Autowired private SimpMessagingTemplate messagingTemplate;
	@Autowired private SocketSessionManager sessionManager;
	private final ApplicationEventPublisher eventPublisher;		
    public AgentClubWaitingListener(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }
    
    @PostPersist
    public void onPostPersist(AgentClubWaiting entity) { 
    	Map<String, List<String>> userSessionsMap = sessionManager.getAllUserSessions();    	
    	
    	for (Map.Entry<String, List<String>> entry : userSessionsMap.entrySet()) {
    	    String userId = entry.getKey();
    	    List<String> sessionList = entry.getValue();
    	    String destination = "/queue/waiting-message/" + userId;
    	    messagingTemplate.convertAndSend(destination, "memberId="+ userId );
    	    
    	    log.info("User ID,Sessions {},{}" , userId , sessionList); 
            log.info("sendMessageToUser destination : {}",destination);  
    	    for (String sessionId : sessionList) {
    	    	log.info("Session ID: " + sessionId);
    	    }
    	}    	
        eventPublisher.publishEvent(new NewDataEvent(entity));
    }
    
    @PreUpdate
    public void onPreUpdate(AgentClubWaiting entity) {
    	log.info("AgentClubWaitingListener onPreUpdate 디비내용이 실제 변경되어야 여기로온다.");   
        // 데이터 업데이트 이벤트 발생
        eventPublisher.publishEvent(new DataUpdatedEvent(entity));
    }
    

}
