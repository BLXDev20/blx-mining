package com.cc.blox.springconfig;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;
 

@SuppressWarnings("deprecation")
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {
	
	@Override
	public void configureMessageBroker(MessageBrokerRegistry config) {
		config.enableSimpleBroker("/topic");
		config.setApplicationDestinationPrefixes("/app");
	}
	
	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/track").withSockJS();
	}
	
	@Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registration) {
		registration.setSendBufferSizeLimit(Integer.MAX_VALUE);
        registration.setMessageSizeLimit(Integer.MAX_VALUE);
        registration.setSendTimeLimit(Integer.MAX_VALUE);
        registration.setSendTimeLimit(60 * 1000);
	        
    }
	
    
}
