package com.whatsappsim.websocket.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

@Component
public class WebSocketConnectionsListener extends WebSocketTopics{

    private static final Logger logger = LoggerFactory.getLogger(WebSocketConnectionsListener.class);
    
    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
    	StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
    	String connectionId = "/"+headerAccessor.getHeader(SimpMessageHeaderAccessor.SESSION_ID_HEADER).toString();
        removeMessagesSuscriptorByConnectionId(connectionId);
        logger.info("user disconnect: "+connectionId);
    }
    
    @EventListener
    public void handleSessionSubscribeEvent(SessionSubscribeEvent event) {
    	StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
    	 String simpDestination = (String) headerAccessor.getMessageHeaders().get("simpDestination");
         if (simpDestination.contains(topicMessages)) {
        	String uniqueSuscription = (String)  getUniqueSubscriptionId(headerAccessor);
        	addMessagesSuscriptor(uniqueSuscription, simpDestination);
            logger.info("subscribe to: " + uniqueSuscription +" : "+ simpDestination);
        }else {
        	logger.info("subscribe to: " + simpDestination);
        }
    }
    
    @EventListener
    public void handleSessionUnsubscribeEvent(SessionUnsubscribeEvent event) {
    	StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
    	String uniqueSuscription = (String)  getUniqueSubscriptionId(headerAccessor);
        removeMessagesSuscriptor(uniqueSuscription);
        logger.info("unsubscribe to: " + uniqueSuscription);
    }
    
    protected String getUniqueSubscriptionId(StompHeaderAccessor headerAccessor){
    	//headerAccessor.getSessionAttributes().get("telephone"); Session param
        return "/"+headerAccessor.getHeader(SimpMessageHeaderAccessor.SESSION_ID_HEADER).toString()
        		+"/"+headerAccessor.getSubscriptionId();
    }
    
}
