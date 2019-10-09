package com.whatsappsim.websocket.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import com.whatsappsim.dto.UserSessionDTO;

@Controller
public class WebSocketAddSession {

	private static final Logger logger = LoggerFactory.getLogger(WebSocketAddSession.class);
	
    @MessageMapping("/add/user")
    public void addUser(@Payload UserSessionDTO userDTO,
                               SimpMessageHeaderAccessor headerAccessor) {
        // Add username in web socket session
    	headerAccessor.getSessionAttributes().put("telephone", userDTO.getTelephone());
        logger.info("user connected: "+userDTO.getTelephone());
    }
	
}
