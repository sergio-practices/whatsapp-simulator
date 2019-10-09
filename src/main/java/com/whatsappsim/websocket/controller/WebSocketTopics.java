package com.whatsappsim.websocket.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class WebSocketTopics {

	// All topics used in the application
	public static final String topicTelephones = "/topic/telephones/";
	public static final String topicMessages = "/topic/messages/";
	
	/**
	 * No connections management on telephones topics, because  by now the message is always sended (not checked).
	 * The map controls connections and subscriptions from tifferent tabs
	 */
	private static Map<String, String> messagesSuscriptors = Collections.synchronizedMap(new HashMap<String, String>());
	
    public static Map<String, String> getMessagesSuscriptors() {
		return messagesSuscriptors;
	}

	public void addMessagesSuscriptor(String connectionSubscriptioId, String topicUri) {
		messagesSuscriptors.put(connectionSubscriptioId, topicUri);
	}
	public void removeMessagesSuscriptor(String connectionSubscriptioId) {
		messagesSuscriptors.remove(connectionSubscriptioId);
	}
	public void removeMessagesSuscriptorByConnectionId(String connectionSubscriptioId) {
		Iterator<String> it = messagesSuscriptors.keySet().iterator();
		while (it.hasNext()) {
			if (it.next().startsWith(connectionSubscriptioId)){
				it.remove();
			}
		}
	}
}
