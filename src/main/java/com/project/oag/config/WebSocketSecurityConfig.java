package com.project.oag.config;

import com.project.oag.app.service.auth.JwtService;
import com.project.oag.app.service.UserInfoUserDetailsService;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.util.List;
import java.util.Map;

@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
public class WebSocketSecurityConfig implements WebSocketMessageBrokerConfigurer {

    private final JwtService jwtService;
    private final UserInfoUserDetailsService userDetailsService;

    public WebSocketSecurityConfig(JwtService jwtService, UserInfoUserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
                if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {
                    String token = resolveToken(accessor);
                    if (StringUtils.hasText(token)) {
                        String username = jwtService.extractUsername(token);
                        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                        if (jwtService.validateToken(token, userDetails)) {
                            accessor.setUser(new UsernamePasswordAuthenticationToken(
                                    userDetails, null, userDetails.getAuthorities()));
                        }
                    }
                }
                return message;
            }
        });
    }

    private String resolveToken(StompHeaderAccessor accessor) {
        List<String> auth = accessor.getNativeHeader("Authorization");
        if (auth != null && !auth.isEmpty()) {
            String header = auth.get(0);
            if (header != null && header.startsWith("Bearer ")) {
                return header.substring(7);
            }
        }
        Map<String, Object> session = accessor.getSessionAttributes();
        if (session != null && session.get("token") instanceof String sessionToken) {
            return sessionToken;
        }
        return null;
    }
}
