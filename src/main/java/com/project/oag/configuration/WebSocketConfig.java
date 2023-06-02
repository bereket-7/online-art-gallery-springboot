package com.project.oag.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import com.project.oag.service.BidHandler;
//@Configuration
//@EnableWebSocketMessageBroker
//@EnableWebSocket
//public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer  {

//    @Override
//    public void configureMessageBroker(MessageBrokerRegistry registry) {
//        registry.enableSimpleBroker("/topic");
//        registry.setApplicationDestinationPrefixes("/app");
//    }
//
//    @Override
//    public void registerStompEndpoints(StompEndpointRegistry registry) {
//        registry.addEndpoint("/websocket").withSockJS();
//    }
//    @Override
//    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
//        registry.addHandler(bidHandler(), "/bidHandler").setAllowedOrigins("*");
//    }
//
//    @Bean
//    public WebSocketHandler bidHandler() {
//        return new BidHandler();
//    }
//}
    
   /* @Bean
    public SimpMessagingTemplate messagingTemplate() {
        return new SimpMessagingTemplate(new WebSocketClientSockJsSessionTaskScheduler());
  }
    @Bean
    public SimpMessagingTemplate messagingTemplate() {
        return new SimpMessagingTemplate(new WebSocketClientSockJsSessionTaskScheduler());
    }
/*

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws").withSockJS();
    }

    @Bean
    public SimpMessagingTemplate messagingTemplate() {
        return new SimpMessagingTemplate(new WebSocketClientSockJsSessionTaskScheduler());
    }
}*/



