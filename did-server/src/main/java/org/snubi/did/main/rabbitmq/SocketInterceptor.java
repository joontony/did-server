package org.snubi.did.main.rabbitmq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class SocketInterceptor implements ChannelInterceptor {

	@Autowired private SocketSessionManager sessionManager;
	
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
    	
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);          
        String sessionId = accessor.getSessionId();            
        String accessUserId = accessor.getFirstNativeHeader("sender"); 
        
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            log.info("SocketInterceptor 소켓접속연결됨 사용자온라인 : sessionId,accessUserId {},{} ", sessionId, accessUserId); 
            sessionManager.addSession(accessUserId, sessionId);
        } else if (StompCommand.DISCONNECT.equals(accessor.getCommand())) {
        	log.info("SocketInterceptor 소켓접속연결끊김 : 사용자오프라인 sessionId,accessUserId {},{} ", sessionId, accessUserId);
            sessionManager.removeSession(accessUserId, sessionId);
        } else if (StompCommand.SEND.equals(accessor.getCommand())) {  
            Object payload = message.getPayload();
            log.info("SocketInterceptor 소켓전송메시지 : {}" , payload);          
        }
        return message;
    }
    
    
}