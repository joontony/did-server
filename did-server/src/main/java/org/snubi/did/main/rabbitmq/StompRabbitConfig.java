package org.snubi.did.main.rabbitmq;

import org.snubi.did.main.config.CustomConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker 
public class StompRabbitConfig implements WebSocketMessageBrokerConfigurer {

	@Autowired private SocketInterceptor socketInterceptor;
	
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/queue");        
        config.setApplicationDestinationPrefixes("/app"); 
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {    	
    	String[] allowedOrigins = CustomConfig.allowedOrigins.toArray(new String[CustomConfig.allowedOrigins.size()]);    	
    	registry.addEndpoint("/ws")  
    	.setAllowedOriginPatterns(allowedOrigins)
    	.withSockJS(); 
    }
    
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(socketInterceptor);
    }
}
