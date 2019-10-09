package com.whatsappsim.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.whatsappsim.dto.MessageDTO;
import com.whatsappsim.dto.TelephoneDTO;
import com.whatsappsim.mongodb.model.Telephone;
import com.whatsappsim.mongodb.repository.MessagesRepository;
import com.whatsappsim.mongodb.repository.TelephonesRepository;
import com.whatsappsim.websocket.controller.WebSocketConnectionsListener;
import com.whatsappsim.websocket.controller.WebSocketTopics;

@Service
public class TelephonesService {

	private static final Logger logger = LoggerFactory.getLogger(TelephonesService.class);
	
	@Autowired
	private MessagesRepository messagesRepository;
	
	@Autowired
	private TelephonesRepository telephonesRepository;
	
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
	
    @Autowired
    private ObjectMapper objectMapper;
    
	/**
	 * Add advices to the contrary menu if not connected.
	 */
	public void updateRequestsAdvices(String communicationId, String telephone, String telephoneTo){
		TelephoneDTO notificationDTO = new TelephoneDTO();
		notificationDTO.setCommunicationId(communicationId);
		notificationDTO.setAdvices(telephonesRepository.updateAdvicesByCommunicationId(communicationId, telephoneTo));
    	messagingTemplate.convertAndSend(WebSocketTopics.topicTelephones + telephoneTo, notificationDTO);
    	logger.info("notified: " + WebSocketTopics.topicTelephones + telephoneTo +", message: "+notificationDTO);
	}
    
	/**
	 * Notify the delayed reception to the contrary
	 * Update the delayed lecturer menu and the messages advice from contrary
	 */
	public void receiveMessageDelayed(String communicationId, String telephone, String telephoneTo){
		messagesRepository.updateMessage(communicationId, telephoneTo);
		telephonesRepository.removeAdvicesByCommunicationId(communicationId);
    	String notifyMessagesTopic = WebSocketTopics.topicMessages + communicationId + "/" + telephoneTo + "/" + telephone;
    	boolean isReceiverSubscribed =  WebSocketConnectionsListener.getMessagesSuscriptors().containsValue(notifyMessagesTopic);
	    if (isReceiverSubscribed) {
	    	MessageDTO messageDTO = new MessageDTO();
	    	messageDTO.setType("messageDelayed");
	    	messagingTemplate.convertAndSend(notifyMessagesTopic, messageDTO);
        	logger.info("notified: "+notifyMessagesTopic +", messageDelayed");
    	}
	}
	
	/**
	 * Notify the delayed reception to the contrary
	 * Update the delayed lecturer menu and the messages advice from contrary
	 */
	public TelephoneDTO addModifyTelephone(TelephoneDTO telephoneDTO){
		Telephone telephone = objectMapper.convertValue(telephoneDTO, Telephone.class);
		telephonesRepository.updateTelephone(telephone);
		if (null == telephone.getCommunicationId()){
			telephonesRepository.addTelephonesCommunication(telephone);
			notifyTelephoneTo(telephone);
		}
		return objectMapper.convertValue(telephone, TelephoneDTO.class);
	}
	
	private void notifyTelephoneTo(Telephone telephone) {
		TelephoneDTO sendTelephone = new TelephoneDTO();
		sendTelephone.setCommunicationId(telephone.getCommunicationId());
		sendTelephone.setTelephone(telephone.getTelephoneTo());
		sendTelephone.setTelephoneTo(telephone.getTelephone());
		sendTelephone.setDescription(telephone.getTelephone());
		messagingTemplate.convertAndSend(WebSocketTopics.topicTelephones + telephone.getTelephoneTo(), sendTelephone);
	}
	
}
