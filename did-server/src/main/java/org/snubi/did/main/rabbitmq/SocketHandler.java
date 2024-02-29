package org.snubi.did.main.rabbitmq;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import com.google.gson.Gson;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class SocketHandler  extends TextWebSocketHandler {
    private List<WebSocketSession> sessionList = new ArrayList<>();
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        String sender = session.getHandshakeHeaders().get("sender").get(0);
        sessionList.add(session);
        log.info("sessionList : {}" , sessionList.size());
        sessionList.forEach(s-> {
            try {
                s.sendMessage(new TextMessage(sender+"님께서 입장하셨습니다."));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        super.handleTextMessage(session, message);
        Gson gson = new Gson();
        String sender = session.getHandshakeHeaders().get("sender").get(0);
        sessionList.forEach(s-> {
            try {
            	ChatMessageDto chatMessage = gson.fromJson(message.getPayload(), ChatMessageDto.class);
                s.sendMessage(new TextMessage(sender + " : "+chatMessage.getSender()+"["+chatMessage.getRoutingKey()+"]"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
        sessionList.remove(session);
        System.out.println("session = " + sessionList.size());
        String sender = session.getHandshakeHeaders().get("sender").get(0);
        sessionList.forEach(s-> {
            try {
                s.sendMessage(new TextMessage(sender+"님께서 퇴장하셨습니다."));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}