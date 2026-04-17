package com.nexchat.NexChat.config;

import com.nexchat.NexChat.security.JwtUtil;
import com.nexchat.NexChat.service.CustomeUserDetailsService;
import lombok.NonNull;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class WebSocketInterceptor implements ChannelInterceptor {

    private final JwtUtil jwtUtil;
    private final CustomeUserDetailsService customeUserDetailsService;

    public WebSocketInterceptor(JwtUtil jwtUtil, CustomeUserDetailsService customeUserDetailsService) {
        this.jwtUtil = jwtUtil;
        this.customeUserDetailsService = customeUserDetailsService;
    }

    @Override
    public  Message<?> preSend(Message<?> message, MessageChannel channel) {

        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if(StompCommand.CONNECT.equals(accessor.getCommand())){
            String authHeader = accessor.getFirstNativeHeader("Authorization");

            if(authHeader == null || !authHeader.startsWith("Bearer") ){
                throw new MessagingException("Missing or invalid authorization");
            }
            String token = authHeader.substring(7);
            String username = jwtUtil.extractUsername(token);
            UserDetails userDetails = customeUserDetailsService.loadUserByUsername(username);

            if(username == null || !jwtUtil.validateToken(token,userDetails)){
                throw new MessagingException("Invalid JWT token");
            }

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
            accessor.setUser(authentication);


        }

        return message;
    }

}
